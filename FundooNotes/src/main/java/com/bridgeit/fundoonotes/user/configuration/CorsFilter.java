package com.bridgeit.fundoonotes.user.configuration;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Servlet Filter implementation class Crosilter
 */
public class CorsFilter extends OncePerRequestFilter implements Filter{
       
    /**
     * @see OncePerRequestFilter#OncePerRequestFilter()
     */
    public CorsFilter() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}


	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		System.out.println("in crosFilter");
		
//		    response.addHeader("Access-Control-Allow-Origin", "*");
//		    response.addHeader("Access-Control-Allow-Methods", "POST, GET, PUT,DELETE");
//		    //response.setHeader("Access-Control-Allow-Credentials", "true");
//            response.addHeader("Access-Control-Allow-Headers", "Content-Type");
//		    response.addHeader("Access-Control-Max-Age", "3600");
		 response.setHeader("Access-Control-Allow-Origin", "*");
		    response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT,DELETE");
		    response.setHeader("Access-Control-Allow-Credentials", "true");
         response.setHeader("Access-Control-Allow-Headers", "Content-Type");
		    response.setHeader("Access-Control-Max-Age", "3600");
		    filterChain.doFilter(request, response);
	//}
	}
}
