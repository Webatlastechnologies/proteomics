package guru.springframework.controllers;

import java.security.Principal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import guru.springframework.domain.Lab;
import guru.springframework.domain.LabDatabase;
import guru.springframework.domain.Organism;
import guru.springframework.domain.Source;
import guru.springframework.domain.User;
import guru.springframework.domain.YesNo;
import guru.springframework.repositories.DatabaseRepository;
import guru.springframework.repositories.LabRepository;
import guru.springframework.repositories.OrganismRepository;
import guru.springframework.repositories.SourceRepository;
import guru.springframework.repositories.UserRepository;

@Controller
public class LabDatabaseController {

	@Autowired
	DatabaseRepository databaseRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	LabRepository labRepository;
	
	@Autowired
	OrganismRepository organismRepository;
	
	@Autowired
	SourceRepository sourceRepository;
	
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
	
	@RequestMapping(value = "/database/add", method = RequestMethod.POST)
	public ModelAndView add(@RequestParam Map<String, String> requestMap, Principal principal) {
		String userName = principal.getName();	
		User user = userRepository.findByUsernameOrEmail(userName, userName);
		Lab lab = labRepository.findOne(1l);
		if(lab == null){
			lab = new Lab();
			lab.setApproved(true);
			lab.setLabHeadEmail("a.d@email.com");
			lab.setLabHeadFirstName("A");
			lab.setLabHeadLastName("D");
			lab.setLabName("FirstLab");
			lab.setLabUrl("/url");
			lab.setRequestorEmail("a.d@email.com");
			Set<User> userSet = new HashSet<User>()	;
			userSet.add(user);
			lab.setUsers(userSet);
			labRepository.save(lab);
		}
		
		LabDatabase labDatabase = new LabDatabase();
		labDatabase.setLab(lab);
		labDatabase.setUser(user);
		String addConProtein = requestMap.get("contaminant");
		if(addConProtein != null && addConProtein.equalsIgnoreCase(YesNo.Yes.name())){
			labDatabase.setAddConProtein(YesNo.Yes);
		}else{
			labDatabase.setAddConProtein(YesNo.No);
		}
		
		labDatabase.setDescription(requestMap.get("desc"));
		labDatabase.setFilePath(requestMap.get("upload_file_name"));
		String reverse = requestMap.get("reverse");
		if(reverse != null && reverse.equalsIgnoreCase(YesNo.Yes.name())){
			labDatabase.setGenRevSeq(YesNo.Yes);
		}else{
			labDatabase.setGenRevSeq(YesNo.No);
		}
		
		Organism organism = organismRepository.findByOrganismName(requestMap.get("organism"));
		if(organism == null){
			organism = new Organism();
			organism.setOrganismName(requestMap.get("organism"));
			organismRepository.save(organism);
		}
		labDatabase.setOrganism(organism);
		labDatabase.setProteinNum(0);
		
		Source source = sourceRepository.findBySourceName(requestMap.get("organism"));
		if(source == null){
			source = new Source();
			source.setSourceName(requestMap.get("dbSource"));
			sourceRepository.save(source);
		}
		labDatabase.setSource(source);
		
		String year = requestMap.get("year");
		String month = requestMap.get("month");
		String date = requestMap.get("date");
		
		labDatabase.setReleasedDate(getSQLDate(year, month, date));
		labDatabase.setSizeInKb(0);
		labDatabase.setUploadedBy(userName);
		year = requestMap.get("year");
		
		Calendar calendar = Calendar.getInstance();
		year =  calendar.get(Calendar.YEAR) + "";
		month = (calendar.get(Calendar.MONTH) + 1) + "";
		date = calendar.get(Calendar.DATE) + "";
		
		labDatabase.setUploadedDate(getSQLDate(year, month, date));
		labDatabase.setVersion(requestMap.get("version"));
		
		databaseRepository.save(labDatabase);
		
		System.out.println("inside database add");
		return new ModelAndView("redirect:/database");
	}
	
	public java.sql.Date getSQLDate(String year, String month, String date){
		
		if(month.length() == 1){
			month = "0"+month;
		}
		
		if(date.length() == 1){
			date = "0"+date;
		}
		String dateStr = year + "-"+ month+ "-"+date;
		return java.sql.Date.valueOf(dateStr);
	}
}
