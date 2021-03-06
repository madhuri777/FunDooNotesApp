package com.bridgeit.fundoonotes.user.configuration;

import java.util.Properties;

import javax.sql.DataSource;

import java.nio.charset.StandardCharsets;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.support.RetryTemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.bridgeit.fundoonotes.labels.model.Labels;
import com.bridgeit.fundoonotes.notes.model.Notes;
import com.bridgeit.fundoonotes.notes.model.NotesDTO;
import com.bridgeit.fundoonotes.notes.model.WebScrapping;
import com.bridgeit.fundoonotes.user.model.EmailTocken;
import com.bridgeit.fundoonotes.user.model.User;
import com.bridgeit.fundoonotes.user.utility.MailRabbitMQ;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@EnableWebMvc
@EnableTransactionManagement
@ComponentScan(basePackages = "com.bridgeit.fundoonotes")
@PropertySource("classpath:application.properties")
public class FundooNotesConfiguration {

	public static final String topicExchangeName = "Spring example";

	static final String QueueName = "EmailQueue";

	public FundooNotesConfiguration() {
		System.out.println("Working");
	}

	@Autowired
	private Environment environment;

	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(environment.getProperty("mysql.driver"));
		dataSource.setUrl(environment.getProperty("mysql.url"));
		dataSource.setUsername(environment.getProperty("mysql.user"));
		dataSource.setPassword(environment.getProperty("mysql.password"));
		return dataSource;
	}

	@Bean
	public LocalSessionFactoryBean getSessionFactory() {
		LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();

		Properties properties = new Properties();

		properties.put("hibernate.dialect", environment.getProperty("hibernate.dialect"));
		properties.put("hibernate.show_sql", environment.getProperty("hibernate.show_sql"));
		properties.put("hibernate.hbm2ddl.auto", environment.getProperty("hibernate.hbm2ddl.auto"));

		sessionFactoryBean.setDataSource(dataSource());
		sessionFactoryBean.setHibernateProperties(properties);
		sessionFactoryBean.setAnnotatedClasses(User.class,Notes.class,Labels.class,WebScrapping.class);
		return sessionFactoryBean;

	}

	@Bean
	public HibernateTransactionManager getTransactionmanager() {
		HibernateTransactionManager transactionManager = new HibernateTransactionManager();
		transactionManager.setSessionFactory(getSessionFactory().getObject());
		return transactionManager;
	}

	// -----------------------------------------------RabbitMQ------------------------------------------------------
	@Bean
	Queue queue() {
		return new Queue(QueueName, false);
	}

	@Bean
	TopicExchange topicExchange() {
		return new TopicExchange(topicExchangeName);
	}

	@Bean
	Binding binding(Queue queue, TopicExchange topicExchange) {
		return BindingBuilder.bind(queue).to(topicExchange).with("lazy.orange.#");
	}

	@Bean
	public SimpleMessageListenerContainer messageListenerContainer() {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(rabbitConnectionFactory());
		container.setQueueNames(QueueName);
		container.setMessageConverter(jsonMessageConverter());
		container.setMessageListener(exampleListener());
		return container;
	}

	@Bean
	public ConnectionFactory rabbitConnectionFactory() {
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory("localhost");
		connectionFactory.setUsername("guest");
		connectionFactory.setPassword("guest");
		return connectionFactory;
	}

	@Bean
	public MessageListener exampleListener() {
		return new MessageListener() {
			public void onMessage(Message message) {
				System.out.println(new String(message.getBody(), StandardCharsets.UTF_8));
				try {
					MailRabbitMQ.reciever(new ObjectMapper()
							.readValue(new String(message.getBody(), StandardCharsets.UTF_8), EmailTocken.class));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		};
	}

	@Bean
	public MessageConverter jsonMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}

	@Bean
	AmqpAdmin amqpAdmin() {
		AmqpAdmin amqpAdmin = new RabbitAdmin(rabbitConnectionFactory());
		amqpAdmin.declareQueue(queue());
		amqpAdmin.declareExchange(topicExchange());
		amqpAdmin.declareBinding(binding(queue(), topicExchange()));
		return amqpAdmin;
	}

	@Bean
	RabbitTemplate rabbitTemplate() {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(rabbitConnectionFactory());
		rabbitTemplate.setMessageConverter(jsonMessageConverter());
		RetryTemplate retryTemplate = new RetryTemplate();
		ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
		backOffPolicy.setInitialInterval(500);
		backOffPolicy.setMultiplier(10.0);
		backOffPolicy.setMaxInterval(10000);
		retryTemplate.setBackOffPolicy(backOffPolicy);
		rabbitTemplate.setRetryTemplate(retryTemplate);
		return rabbitTemplate;
	}

//---------------------------------------------------Spring Redis----------------------------------------------------

	@SuppressWarnings("deprecation")
	@Bean
	public RedisConnectionFactory redisConnectionFactory() {

		JedisConnectionFactory connectionFactory = new JedisConnectionFactory();
		connectionFactory.setHostName("localhost");
		connectionFactory.setPort(6379);

		return connectionFactory;
	}

	
	@Bean
	public RedisTemplate<String, String> redisTemplate() {
		RedisTemplate<String, String> redisTemplate = new RedisTemplate<String, String>();
		redisTemplate.setConnectionFactory(redisConnectionFactory());
		redisTemplate.setEnableTransactionSupport(true);
		return redisTemplate;
	}
	
//--------------------------------------------------Notes Configuration--------------------------------------	

//   @Bean
//   public Notes notes() {
//	   return new Notes();
//   }
//
   @Bean
   public NotesDTO notesDTO() {
	   return new NotesDTO();
   }

//-------------------------------------------------Cors Connection------------------------------------------

   @Bean
   public WebMvcConfigurer corsConfigurer() {
       return new WebMvcConfigurer() {
           @Override
           public void addCorsMappings(CorsRegistry registry) {
               registry.addMapping("/**").allowedMethods("HEAD", "GET", "PUT", "POST", "DELETE", "PATCH");
           }
       };
   }
 
//------------------------------------------------File Upload-----------------------------------------------   
   
   @Bean(name = "multipartResolver")
   public CommonsMultipartResolver multipartResolver() {
       CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
       multipartResolver.setMaxUploadSize(2000000);
       return multipartResolver;
   }
}
