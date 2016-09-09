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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
	public String addNewDatabase(Model model) {
		model.addAttribute("sourceList", sourceRepository.findAll());
		model.addAttribute("organismList", organismRepository.findAll());
		return "addNewDatabase";
	}
	
	@RequestMapping(value="/readDatabase", method = RequestMethod.GET)
	@ResponseBody
	public List<LabDatabase> read() {
		System.out.println("inside database read");
		List<LabDatabase> labDatabaseList = databaseRepository.findAll();
		if(labDatabaseList != null){
			for(LabDatabase labDatabase : labDatabaseList){
				labDatabase.setReleaseDateStr(getReleaseDate(labDatabase.getReleasedDate()));
				labDatabase.setUploadDateStr(getUploadedDate(labDatabase.getReleasedDate()));
			}
		}
		return labDatabaseList;
	}

	@RequestMapping(value = "/deleteDatabase", method = RequestMethod.POST)
	@ResponseBody
	public String delete(@RequestParam long database_id) {
		LabDatabase labDatabase = databaseRepository.findOne(database_id);
		databaseRepository.delete(labDatabase);
		System.out.println("inside database delete");
		return "";
	}
	
	@RequestMapping(value = "/addDatabase", method = RequestMethod.POST)
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
		
		Organism organism = organismRepository.findOne(Integer.parseInt(requestMap.get("organism")));
		if(organism == null){
			
		}
		labDatabase.setOrganism(organism);
		labDatabase.setProteinNum(0);
		
		Source source = sourceRepository.findOne(Integer.parseInt(requestMap.get("dbSource")));
		if(source == null){
			
		}
		labDatabase.setSource(source);
		
		String year = requestMap.get("year");
		String month = requestMap.get("month");
		String date = requestMap.get("date");
		try{
			labDatabase.setReleasedDate(getDate(year, month, date));
		}catch(ParseException e){
			e.printStackTrace();
		}
		
		labDatabase.setSizeInKb(0);
		labDatabase.setUploadedBy(userName);
		year = requestMap.get("year");
		
		Calendar calendar = Calendar.getInstance();
		
		labDatabase.setUploadedDate(calendar.getTime());
		labDatabase.setVersion(requestMap.get("version"));
		
		databaseRepository.save(labDatabase);
		
		System.out.println("inside database add");
		return new ModelAndView("redirect:/database");
	}
	
	@RequestMapping(value = "/viewAddNewOrganism", method = RequestMethod.GET)
	public String addNewOrganism() {
		return "addNewOrganism";
	}
	
	@RequestMapping(value = "/viewAddNewSource", method = RequestMethod.GET)
	public String addNewSource() {
		return "addNewSource";
	}
	
	@RequestMapping(value = "/addNewSource", method = RequestMethod.POST)
	public ModelAndView addNewSource(@RequestBody String sourceName, Model model, RedirectAttributes redir){
		sourceName = sourceName.substring(sourceName.indexOf("dbSource=") + "dbSource=".length());
		Source source = sourceRepository.findBySourceName(sourceName);
		if(source != null){
			redir.addFlashAttribute("error", "Source with given name already exists");
			return new ModelAndView("redirect:/viewAddNewSource");
		}else{
			Source newSource = new Source();
			newSource.setSourceName(sourceName);
			sourceRepository.save(newSource);
			return new ModelAndView("redirect:/viewAddNewDatabase");
		}
	}
	
	@RequestMapping(value = "/addNewOrganism", method = RequestMethod.POST)
	public ModelAndView addNewOrganism(@RequestBody String organismName, Model model, RedirectAttributes redir){
		organismName = organismName.substring(organismName.indexOf("organism=") + "organism=".length());
		Organism organism = organismRepository.findByOrganismName(organismName);
		if(organism != null){
			redir.addFlashAttribute("error", "Organism with given name already exists");
			return new ModelAndView("redirect:/viewAddNewOrganism");
		}else{
			Organism newOrganism = new Organism();
			newOrganism.setOrganismName(organismName);
			organismRepository.save(newOrganism);
			return new ModelAndView("redirect:/viewAddNewDatabase");
		}
	}
	
	public Date getDate(String year, String month, String date) throws ParseException{
	
		if(month.length() == 1){
			month = "0"+month;
		}
		
		if(date.length() == 1){
			date = "0"+date;
		}
		String dateStr = year + "-"+ month+ "-"+date;
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		return  formatter.parse(dateStr);
	}
	
	public String getReleaseDate(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(date.getTime());
		String year =  calendar.get(Calendar.YEAR) + "";
		String month = (calendar.get(Calendar.MONTH) + 1) + "";
		String dateStr = calendar.get(Calendar.DATE) + "";
		if(month.length() == 1){
			month = "0"+month;
		}
		if(dateStr.length() == 1){
			dateStr = "0"+dateStr;
		}
		return year + "-" + month + "-"+ dateStr;
	}
	
	public String getUploadedDate(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(date.getTime());
		String year =  calendar.get(Calendar.YEAR) + "";
		String month = (calendar.get(Calendar.MONTH) + 1) + "";
		if(month.length() == 1){
			month = "0"+month;
		}
		String dateStr = calendar.get(Calendar.DATE) + "";
		if(dateStr.length() == 1){
			dateStr = "0"+dateStr;
		}
		String hours = calendar.get(Calendar.HOUR) + "";
		String minutes = calendar.get(Calendar.MINUTE) + "";
		String seconds = calendar.get(Calendar.SECOND) + "";
		return year + "-" + month + "-"+ dateStr + " "+ hours + ":" + minutes + ":" + seconds + ".0";
	}
}
