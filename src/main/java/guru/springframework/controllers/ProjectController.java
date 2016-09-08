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

import guru.springframework.domain.Project;
import guru.springframework.domain.User;
import guru.springframework.repositories.ProjectRepository;

@Controller
@RequestMapping("/project")
public class ProjectController {
	
	@Autowired
	ProjectRepository projectRepository;
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String getDetails() {
		return "project";
	}

	@RequestMapping(value="/read", method = RequestMethod.POST)
	public @ResponseBody List<Project> read(){
		return projectRepository.findAll();
	}
	
	 @RequestMapping(value = {"/update","/save"}, method = RequestMethod.POST)
		 public @ResponseBody Project update(@Valid @ModelAttribute Project project, BindingResult errors) {
			 projectRepository.save(project);
	        return project;
	    }

	 @RequestMapping(value = "/delete", method = RequestMethod.POST)
	    public @ResponseBody String update(@RequestParam long project_id) {
		 Project project=projectRepository.findOne(project_id);
		 	projectRepository.delete(project);
	        return "";
	    }
	 
	 
}
