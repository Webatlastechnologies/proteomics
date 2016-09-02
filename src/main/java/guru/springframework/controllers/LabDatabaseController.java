package guru.springframework.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/database")
public class LabDatabaseController {
	
	@RequestMapping(value="getDetails", method = RequestMethod.GET)
	public String getDetails(){
		return "database";
	}
}
