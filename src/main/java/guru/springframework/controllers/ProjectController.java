package guru.springframework.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/project")
public class ProjectController {
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String getDetails() {
		return "project";
	}
}
