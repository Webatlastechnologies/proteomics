package guru.springframework.controllers;

import java.util.GregorianCalendar;
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
import guru.springframework.domain.Project;
import guru.springframework.repositories.ExperimentRepository;

@Controller
@RequestMapping("/experiment")
public class ExperimentController {
	
	@Autowired
	ExperimentRepository experimentRepository;
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String getDetails(@RequestParam("project_id") String project_id,Model model) {
		System.out.println("inside experiment get details project_id : "+project_id);
		model.addAttribute("project_id",project_id);
		return "experiment";
	}

	@RequestMapping(value="/read/{project_id}", method = RequestMethod.POST)
	public @ResponseBody List<Experiment> read(@PathVariable("project_id") long project_id,Model model){
		Project p=new Project();
		p.setProject_id(project_id);
		return experimentRepository.findByProject(p);
	}
	
	 @RequestMapping(value = {"/update","/save"}, method = RequestMethod.POST)
		 public @ResponseBody Experiment update(@Valid @ModelAttribute Experiment experiment, BindingResult errors) {
			 if(experiment.getExperiment_id()==0){
				 experiment.setCreateDate(GregorianCalendar.getInstance().getTime());
				 	experiment.setDate(GregorianCalendar.getInstance().getTime());
					experimentRepository.save(experiment);
			 }
	        return experiment;
	    }

	 @RequestMapping(value = "/delete", method = RequestMethod.POST)
	    public @ResponseBody String update(@RequestParam long experiment_id) {
		 Experiment experiment=experimentRepository.findOne(experiment_id);
		 	experimentRepository.delete(experiment);
	        return "";
	    }
	 
	 
}
