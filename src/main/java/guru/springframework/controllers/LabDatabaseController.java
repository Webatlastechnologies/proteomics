package guru.springframework.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import guru.springframework.domain.LabDatabase;
import guru.springframework.repositories.DatabaseRepository;

@Controller
@RequestMapping("/database")
public class LabDatabaseController {

	@Autowired
	DatabaseRepository databaseRepository;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String getDetails() {
		return "database";
	}

	@RequestMapping(value="/read", method = RequestMethod.GET)
	@ResponseBody
	public List<LabDatabase> read() {
		return databaseRepository.findAll();
	}

	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	@ResponseBody
	public String delete(@RequestParam long updateId) {
		LabDatabase labDatabase = databaseRepository.findOne(updateId);
		databaseRepository.delete(labDatabase);
		return "success";
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public String add(@RequestBody LabDatabase labDatabase){
		databaseRepository.save(labDatabase);
		return "success";
	}
}
