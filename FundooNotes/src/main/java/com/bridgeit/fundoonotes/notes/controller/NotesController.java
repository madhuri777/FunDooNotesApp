package com.bridgeit.fundoonotes.notes.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bridgeit.fundoonotes.notes.model.NotesDTO;
import com.bridgeit.fundoonotes.notes.service.INotesService;
import com.bridgeit.fundoonotes.user.exception.DataBaseException;

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
		
		return new ResponseEntity<List<NotesDTO>>(list,HttpStatus.OK);
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
}
