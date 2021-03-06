package com.bridgeit.fundoonotes.notes.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bridgeit.fundoonotes.notes.model.NotesDTO;
import com.bridgeit.fundoonotes.notes.service.INotesService;
import com.bridgeit.fundoonotes.user.model.Response;

@RestController
public class NotesController {

	@Autowired
	private INotesService iNotesService;
	
	@RequestMapping(value="/createnotes", method=RequestMethod.POST)
	public ResponseEntity<?> createNotes(HttpServletRequest request,@RequestBody NotesDTO dto){
		
		String token=request.getHeader("userid");
		
		NotesDTO note=iNotesService.createNotes(token , dto);
		
		if(note!=null) {
			
			return new ResponseEntity<NotesDTO>(note,HttpStatus.OK);
		}
		
		return new ResponseEntity<String>("note Not Created successfully ",HttpStatus.CONFLICT);
		
	}
	
	@RequestMapping(value="/getallnotesofuser",method=RequestMethod.GET)
	public ResponseEntity<List<NotesDTO>> getAllNotes(HttpServletRequest request){
		
		String token=request.getHeader("userid");
		
		List<NotesDTO> list=iNotesService.getAllNotes(token);
		System.out.println(" controller for getLakk records "+list); 
		if(list!=null) {
			return new ResponseEntity<List<NotesDTO>>(list,HttpStatus.OK);
		}
		
		return new ResponseEntity<List<NotesDTO>>(list,HttpStatus.CONFLICT);
	}
	
	@RequestMapping(value="/updatenotes/{id}",method=RequestMethod.PUT)
	public ResponseEntity<String> updateNotes(@PathVariable("id") long id,@RequestBody NotesDTO dto,HttpServletRequest request){
		String token=request.getHeader("userid");
		System.out.println("note come for update "+dto.getImage());
		boolean status=iNotesService.update(id, token,dto);
		if(status) {
			return new ResponseEntity<String>("updated Note ",HttpStatus.OK);
		}else {
			return new ResponseEntity<String>("Not Updated",HttpStatus.CONFLICT);
		}
		
	}
	
	@RequestMapping(value="deletnote/{id}",method=RequestMethod.DELETE)
	public ResponseEntity<String>deletNote(@PathVariable("id") long id,HttpServletRequest request){
		
		String token=request.getHeader("userid");

		boolean status=iNotesService.delete(id, token);
		
		if(status) {
			return new ResponseEntity<String>("note deleted successfully",HttpStatus.OK);
		}
		return new ResponseEntity<String>("this id's note not present ",HttpStatus.CONFLICT);
	}
	
	@RequestMapping(value="addcollaborator/{useremail:.+}",method=RequestMethod.POST)
	public ResponseEntity<?> collaborator(@PathVariable String useremail,@RequestBody NotesDTO dto, HttpServletRequest request){
		
		System.out.println("email for collaborator "+useremail);
		boolean flag=iNotesService.collaborator(useremail,dto);
		
		Response response=new Response();
		
		if(flag) {
			response.setMessage("succesfully collaborated");
			return new ResponseEntity<>(response,HttpStatus.OK);
		}
		response.setMessage("Not set collaboratedc succsfully ");
		return new ResponseEntity<>(response,HttpStatus.CONFLICT);
	}
	

	@RequestMapping(value="removecollaborator/{userId}",method=RequestMethod.POST)
	public ResponseEntity<?> removeCollaborator(@PathVariable long userId,@RequestBody NotesDTO dto,Response response ){
		iNotesService.removeCollaborator(userId,dto);
		response.setMessage("deleted collaborator successfully");
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	
	
	
	@RequestMapping(value="/upload", method=RequestMethod.POST)
    public ResponseEntity<?> handleFileUpload( 
            @RequestParam("file") MultipartFile file){
		Response response=new Response();
		String filename=file.getOriginalFilename();
		System.out.println("file file file file fiele");
		System.out.println("file name "+filename);
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                
                BufferedOutputStream stream = 
                        new BufferedOutputStream(new FileOutputStream(new File("/home/bridgeit/workspace/FunDooNotesApp/FundooNotes/src/main/java/com/bridgeit/fundoonotes/Profile/"+filename )));
                stream.write(bytes);
                stream.close();
                response.setMessage("http://localhost:8080/FundooNotes/image/"+ filename);
                return new ResponseEntity<Response>(response,HttpStatus.OK);
            } catch (Exception e) {
            	response.setMessage("You failed to upload " + filename + " => " + e.getMessage());
                return new ResponseEntity<Response>(response,HttpStatus.CONFLICT);
            }
        } else {
        	response.setMessage("error" + filename);
            return new ResponseEntity<Response>(response,HttpStatus.OK);
        }
    }
	
	@RequestMapping(value="image/{filename:.+}",method=RequestMethod.GET)
	public ResponseEntity<?> getFileImage(@PathVariable String filename,HttpServletRequest request){
		System.out.println("image name "+filename);
		Resource file = iNotesService.loadFile(filename);
		System.out.println("abcdefg "+file);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
				.body(file);
	}
	
	@RequestMapping(value="webscrapping",method=RequestMethod.POST)
	public ResponseEntity<?> webScrapping(@RequestBody String url){
		System.out.println("aaaaa "+url);
		ArrayList<String> links = new ArrayList<String>();
		 
		String regex = "\\(?\\b(https://|www[.])[-A-Za-z0-9+&amp;@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&amp;@#/%=~_()|]";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(url);
		while(m.find()) {
		String urlStr = m.group();
		if (urlStr.startsWith("(") && urlStr.endsWith(")"))
		{
		urlStr = urlStr.substring(1, urlStr.length() - 1);
		}
		links.add(urlStr);
		}
		System.out.println("links array "+links);
		for(String s:links){
			System.out.println("iterator "+s);
			URL url2;
					try {
						url2 = new URL(s);
						String host=url2.getHost();
						System.out.println("host name "+host);
						Document doc = Jsoup.connect(s).get();
						String title=doc.title();
						System.out.println("title "+title);
						Element productLink = doc.select("a").first();
						String href = productLink.attr("abs:href");
						System.out.println("images "+href);
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		}
		return new ResponseEntity<>(links,HttpStatus.CONFLICT);

	}
}
