package guru.springframework.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import guru.springframework.domain.LabDatabase;
import guru.springframework.repositories.DatabaseRepository;

@Controller
public class LabDatabaseController {

	@Autowired
	DatabaseRepository databaseRepository;

	@RequestMapping(value = "/database", method = RequestMethod.GET)
	public String getDetails() {
		return "database";
	}
	
	@RequestMapping(value = "/viewAddNewDatabase", method = RequestMethod.GET)
	public String addNewDatabase() {
		return "addNewDatabase";
	}
	
	@RequestMapping(value="/database/read", method = RequestMethod.GET)
	@ResponseBody
	public List<LabDatabase> read() {
		System.out.println("inside database read");
		return databaseRepository.findAll();
	}

	@RequestMapping(value = "/database/delete", method = RequestMethod.DELETE)
	@ResponseBody
	public String delete(@RequestParam long updateId) {
		LabDatabase labDatabase = databaseRepository.findOne(updateId);
		databaseRepository.delete(labDatabase);
		System.out.println("inside database delete");
		return "success";
	}
	
}
