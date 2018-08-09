package com.bridgeit.fundoonotes.notes.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bridgeit.fundoonotes.notes.model.NotesDTO;
import com.bridgeit.fundoonotes.notes.service.INotesService;

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
	
	@RequestMapping(value="/upload", method=RequestMethod.POST)
    public ResponseEntity<?> handleFileUpload( 
            @RequestParam("file") MultipartFile file){
		String filename=file.getOriginalFilename();
		System.out.println("file name "+filename);
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                BufferedOutputStream stream = 
                        new BufferedOutputStream(new FileOutputStream(new File("/home/bridgeit/eclipse-workspace-FundoApp/FundooNotes/src/main/java/com/bridgeit/fundoonotes/Profile/"+filename )));
                stream.write(bytes);
                stream.close();
                return new ResponseEntity<String>("You successfully uploaded " + filename + " into " + filename + "-uploaded !",HttpStatus.ACCEPTED);
            } catch (Exception e) {
                return new ResponseEntity<String>("You failed to upload " + filename + " => " + e.getMessage(),HttpStatus.CONFLICT);
            }
        } else {
            return new ResponseEntity<String>("http://localhost:8080/FundooNotes/image/" + filename + " because the file was empty.",HttpStatus.CONFLICT);
        }
    }
	
	@RequestMapping(value="/image/{filename}")
	public ResponseEntity<?> getFileImage(@PathVariable String imageName){
		System.out.println("image name "+imageName);
		return new ResponseEntity<String>("",HttpStatus.OK);
	}
}
