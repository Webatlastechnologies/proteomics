package guru.springframework.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import guru.springframework.domain.Lab;
import guru.springframework.repositories.LabRepository;
import guru.springframework.repositories.SiteUpdateMessageRepository;

@Controller
@RequestMapping("/lab")
public class LabController {

	@Autowired
	LabRepository labRepository;
	
	@Autowired
	SiteUpdateMessageRepository messageRepository;
	
	@RequestMapping({"/",""})
	public String get(){
		return "admin-lab";
	}
	
	@RequestMapping(value="/read", method = RequestMethod.POST)
	public @ResponseBody List<Lab> read(){
		return labRepository.findAll();
	}
	 @RequestMapping(value = "/update", method = RequestMethod.POST)
		 public @ResponseBody Lab update(@Valid @ModelAttribute Lab lab, BindingResult errors) {
		 //	msg.setUpdateDate(GregorianCalendar.getInstance().getTime());
			 labRepository.save(lab);
	        return lab;
	    }

	 @RequestMapping(value = "/delete", method = RequestMethod.POST)
	    public @ResponseBody String update(@RequestParam long updateId) {
		 Lab msg=labRepository.findOne(updateId);
		 	labRepository.delete(msg);
	        return "";
	    }
}