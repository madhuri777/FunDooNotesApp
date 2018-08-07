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
import com.bridgeit.fundoonotes.notes.model.NotesDTO;
import com.bridgeit.fundoonotes.user.model.Response;

@RestController
public class LabelController {

	@Autowired
	private ILabelService iLabelService;

	@RequestMapping(value = "/createlabel", method = RequestMethod.POST)
	public ResponseEntity<Response> createlabel(HttpServletRequest request, @RequestBody LabelDTO ldto,
			Response response) {

		String token = request.getHeader("userid");

		boolean flag = iLabelService.createLabel(token, ldto);
		if (flag) {
			response.setMessage("label created");
			return new ResponseEntity<Response>(response, HttpStatus.OK);
		}
		response.setMessage("label Not created or already exist ");
		return new ResponseEntity<Response>(response, HttpStatus.CONFLICT);
	}

	@RequestMapping(value = "/getallabels", method = RequestMethod.GET)
	public ResponseEntity<List<LabelDTO>> getAllLabels(HttpServletRequest request) {

		String token = request.getHeader("userid");

		List<LabelDTO> list = iLabelService.getAllLabels(token);

		return new ResponseEntity<List<LabelDTO>>(list, HttpStatus.OK);
	}

	@RequestMapping(value = "/updatelabel/{labelId}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateLabels(@PathVariable long labelId, @RequestBody LabelDTO dto,
			HttpServletRequest request, Response response) {

		String token = request.getHeader("userid");

		boolean flag = iLabelService.updateLabels(labelId, token, dto);

		if (flag) {

			response.setMessage("updated");
			return new ResponseEntity<Response>(response, HttpStatus.OK);

		}
		response.setMessage("Not updated");
		return new ResponseEntity<Response>(response, HttpStatus.CONFLICT);
	}

	@RequestMapping(value = "/deletelabel/{labelId}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteLabel(@PathVariable long labelId, HttpServletRequest request, Response response) {

		String token = request.getHeader("userid");
		boolean flag = iLabelService.deleteLabel(token, labelId);

		if (flag) {

			response.setMessage("Delete label successfully");

			return new ResponseEntity<Response>(response, HttpStatus.OK);

		}
		response.setMessage(" label Not deleted");
		return new ResponseEntity<Response>(response, HttpStatus.CONFLICT);
	}
	@RequestMapping(value="/addlabels/{labelid}",method=RequestMethod.POST)
	public ResponseEntity<?> addLabel(HttpServletRequest request,Response response, @RequestBody NotesDTO note, @PathVariable long labelid){

		String token=request.getHeader("userid");
		boolean flag=iLabelService.addLabels(token, note, labelid);
		if(flag) {
		response.setMessage("Added Suuccessfully");
		return new ResponseEntity<Response>(response,HttpStatus.OK);
	}
		response.setMessage("Not Added Suucfully");
		return new ResponseEntity<Response>(response,HttpStatus.CONFLICT);
	}		
	
	@RequestMapping(value="/removelabels/{labelId}",method=RequestMethod.POST)
	public ResponseEntity<?> removeLabels(HttpServletRequest request,Response response,@RequestBody NotesDTO notedto,@PathVariable long labelId){
		
		String token=request.getHeader("userid");
		boolean flag=iLabelService.removeLabels(token, notedto, labelId);
		System.out.println("controller of remove labels "+flag);
		
		return new ResponseEntity<Response>(response,HttpStatus.OK);
	}
}
