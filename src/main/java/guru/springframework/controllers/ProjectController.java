package guru.springframework.controllers;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;

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
import guru.springframework.repositories.ProjectRepository;
import guru.springframework.repositories.UserRepository;
import guru.springframework.services.UserDetailService;

@Controller
@RequestMapping("/project")
public class ProjectController {
	
	@Autowired
	ProjectRepository projectRepository;
	
	@Autowired
	UserDetailService userDetailService;
	
	@Autowired
	UserRepository userRepository;
	
	@ModelAttribute("login_user_id")
	public long getLoginUser(){
		return userDetailService.getLoggedInUser().getUser_id();
	}
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String getDetails() {
		return "project";
	}

	@RequestMapping(value="/read", method = RequestMethod.POST)
	public @ResponseBody List<Project> read(){
		Set<Project> sharedProjects=userDetailService.getLoggedInUser().getSharedProjects();
		Set<Project> ownedProjects = userDetailService.getLoggedInUser().getProjects();
		for(Project p:sharedProjects){
			p.setProjectOwner(p.getUser().getUser_id());
		}
		for(Project p:ownedProjects){
			p.setProjectOwner(p.getUser().getUser_id());
			p.setNoOfSharedUsers(p.getUsers().size());
		}
		List<Project> allProjects=new ArrayList<>();
		allProjects.addAll(sharedProjects);
		allProjects.addAll(ownedProjects);
		return allProjects;
	}
	
	 @RequestMapping(value = {"/update","/save"}, method = RequestMethod.POST)
		 public @ResponseBody Project update(@Valid @ModelAttribute Project project, BindingResult errors) {
		 	 project.setUser(userDetailService.getLoggedInUser());
		 	 if(project.getProject_id()==0){
		 		 project.setCreatedDate(GregorianCalendar.getInstance().getTime());
		 	 }
			 projectRepository.save(project);
	        return project;
	    }

	 @RequestMapping(value = "/delete", method = {RequestMethod.GET,RequestMethod.POST})
	    public @ResponseBody String update(@RequestParam long project_id) {
		 Project project=projectRepository.findOne(project_id);
		 	projectRepository.delete(project);
	        return "";
	    }
	 
	 
}
