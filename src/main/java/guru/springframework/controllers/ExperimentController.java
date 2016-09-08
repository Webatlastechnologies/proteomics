package guru.springframework.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import guru.springframework.domain.Experiment;
import guru.springframework.repositories.ExperimentRepository;

@Controller
@RequestMapping("/experiment")
public class ExperimentController {
	
	@Autowired
	ExperimentRepository experimentRepository;
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public String getDetails(@PathVariable("id") long id,Model model) {
		System.out.println("inside experiment get details");
		model.addAttribute("project_id",id);
		return "experiment";
	}

	@RequestMapping(value="/read", method = RequestMethod.POST)
	public @ResponseBody List<Experiment> read(){
		return experimentRepository.findAll();
	}
	
	 @RequestMapping(value = {"/update","/save"}, method = RequestMethod.POST)
		 public @ResponseBody Experiment update(@Valid @ModelAttribute Experiment experiment, BindingResult errors) {
			 experimentRepository.save(experiment);
	        return experiment;
	    }

	 @RequestMapping(value = "/delete", method = RequestMethod.POST)
	    public @ResponseBody String update(@RequestParam long experiment_id) {
		 Experiment experiment=experimentRepository.findOne(experiment_id);
		 	experimentRepository.delete(experiment);
	        return "";
	    }
	 
	 
}
