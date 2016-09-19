package guru.springframework.controllers;

import java.security.Principal;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import guru.springframework.domain.Experiment;
import guru.springframework.domain.Instrument;
import guru.springframework.domain.Project;
import guru.springframework.domain.Source;
import guru.springframework.repositories.ExperimentRepository;
import guru.springframework.repositories.InstrumentRepository;

@Controller
public class ExperimentController {
	
	@Autowired
	ExperimentRepository experimentRepository;
	
	@Autowired
	InstrumentRepository instrumentRepository;
	
	@RequestMapping(value = "/experiment", method = RequestMethod.GET)
	public String getDetails(@RequestParam("project_id") String project_id,Model model) {
		System.out.println("inside experiment get details project_id : "+project_id);
		model.addAttribute("project_id",project_id);
		return "allexperiment";
	}

	@RequestMapping(value="/experiment/read/{project_id}", method = RequestMethod.POST)
	public @ResponseBody List<Experiment> read(@PathVariable("project_id") long project_id,Model model){
		Project p=new Project();
		p.setProject_id(project_id);
		return experimentRepository.findByProject(p);
	}
	
	 @RequestMapping(value = {"/experiment/update","/save"}, method = RequestMethod.POST)
		 public @ResponseBody Experiment update(@Valid @ModelAttribute Experiment experiment, BindingResult errors) {
			 if(experiment.getExperiment_id()==0){
				 experiment.setCreateDate(GregorianCalendar.getInstance().getTime());
				 	experiment.setExperimentDate(GregorianCalendar.getInstance().getTime());
					experimentRepository.save(experiment);
			 }
	        return experiment;
	    }

	 @RequestMapping(value = "/experiment/delete", method = RequestMethod.POST)
	    public @ResponseBody String update(@RequestParam long experiment_id) {
		 Experiment experiment=experimentRepository.findOne(experiment_id);
		 	experimentRepository.delete(experiment);
	        return "";
	    }
	 
	@RequestMapping(value = "/viewAddNewInstrument", method = RequestMethod.GET)
	public String addNewInstrument() {
		return "addNewInstrument";
	}
	 
	@RequestMapping(value = "/addNewInstrument", method = RequestMethod.POST)
	public ModelAndView addNewSource(@RequestBody String instrumentName, Model model, RedirectAttributes redir){
		instrumentName = instrumentName.substring(instrumentName.indexOf("instrumentName=") + "instrumentName=".length());
		Instrument instrument = instrumentRepository.findByInstrumentName(instrumentName);
		if(instrument != null){
			redir.addFlashAttribute("error", "Instrument with given name already exists");
			return new ModelAndView("redirect:/viewAddNewInstrument");
		}else{
			Instrument newInstrumentName = new Instrument();
			newInstrumentName.setInstrumentName(instrumentName);
			instrumentRepository.save(newInstrumentName);
			return new ModelAndView("redirect:/viewAddNewDatabase");
		}
	}
	
	@RequestMapping(value = "/viewAddNewExperiment", method = RequestMethod.GET)
	public String addNewDatabase(Model model) {
		model.addAttribute("instrumentList", instrumentRepository.findAll());
		return "addNewExperiment";
	}
	
	@RequestMapping(value = "/addInstrument", method = RequestMethod.POST)
	public ModelAndView add(@Valid @ModelAttribute Experiment experiment, BindingResult errors, Principal principal) {
		 if(experiment.getExperiment_id()==0){
			experiment.setCreateDate(GregorianCalendar.getInstance().getTime());
		 	experiment.setExperimentDate(GregorianCalendar.getInstance().getTime());
			experimentRepository.save(experiment);
		 }
		return new ModelAndView("redirect:/experiment");
	}
}
