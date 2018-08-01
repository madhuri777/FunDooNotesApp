package com.bridgeit.fundoonotes.labels.controller;

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

import com.bridgeit.fundoonotes.labels.model.LabelDTO;
import com.bridgeit.fundoonotes.labels.service.ILabelService;
import com.bridgeit.fundoonotes.user.model.Response;

@RestController
public class LabelController {

	@Autowired
	private ILabelService iLabelService;
	
	@RequestMapping(value="/createlabel",method=RequestMethod.POST)
	public ResponseEntity<Response> createlabel(HttpServletRequest request,@RequestBody LabelDTO ldto,Response response ){
		
		String token=request.getHeader("userid"); 
		   
		boolean flag=iLabelService.createLabel(token,ldto);
		if(flag) {
			response.setMessage("label created");
		return new ResponseEntity<Response>(response,HttpStatus.OK);
		}
		response.setMessage("label Not created or already exist ");
		return new ResponseEntity<Response>(response,HttpStatus.CONFLICT);
	}
	
	@RequestMapping(value="/getallabels",method=RequestMethod.GET)
	public ResponseEntity<List<LabelDTO>> getAllLabels(HttpServletRequest request){
		
		String token=request.getHeader("userid");
		
		List<LabelDTO> list=iLabelService.getAllLabels(token);
		
		return new ResponseEntity<List<LabelDTO>>(list,HttpStatus.OK);
	}
	
	@RequestMapping(value="/updatelabel/{labelId}",method=RequestMethod.PUT)
	public ResponseEntity<?> updateLabels(@PathVariable long labelId,@RequestBody LabelDTO dto,HttpServletRequest request){
		
		String token=request.getHeader("userid");
		return new ResponseEntity<String>("updated",HttpStatus.OK);
	}
}
