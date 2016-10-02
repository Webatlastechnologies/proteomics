package guru.springframework.controllers;

import java.io.File;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
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

import guru.springframework.domain.DataFile;
import guru.springframework.domain.DtaFileDetails;
import guru.springframework.domain.Experiment;
import guru.springframework.domain.Instrument;
import guru.springframework.domain.Project;
import guru.springframework.repositories.DataFileRepository;
import guru.springframework.repositories.DtaFileDetailsRepository;
import guru.springframework.repositories.ExperimentRepository;
import guru.springframework.repositories.InstrumentRepository;
import guru.springframework.repositories.ProjectRepository;
import guru.springframework.services.StorageService;
import guru.springframework.services.UserDetailService;
import guru.springframework.util.ResultReader;

@Controller
public class ExperimentController {
	
	@Autowired
	ExperimentRepository experimentRepository;
	
	@Autowired
	InstrumentRepository instrumentRepository;
	
	@Autowired
	ProjectRepository projectRepository;
	
	@Autowired
	DataFileRepository dataFileRepository;

	@Autowired
	DtaFileDetailsRepository dtaFileDetailsRepository;
	
	@Autowired
	StorageService storageService;
	
	@Autowired
	ResultReader resultReader;
	
	@Autowired
	UserDetailService userDetailService;
	
    @ModelAttribute("loginuser")
    public String loginuser(){
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	return auth.getName();
    }
    
	@RequestMapping(value = "/experiment", method = RequestMethod.GET)
	public String getDetails(@RequestParam("project_id") String project_id,Model model) {
		System.out.println("inside experiment get details project_id : "+project_id);
		model.addAttribute("project_id",project_id);
		return "allexperiment";
	}

	@RequestMapping(value="/experiment/read/{project_id}", method = RequestMethod.POST)
	public @ResponseBody List<Experiment> read(@PathVariable("project_id") long project_id, @RequestParam(value="isArchive", required=false) Boolean isArchive, Model model){
		Project p=new Project();
		p.setProject_id(project_id);
		if(isArchive == null){
			isArchive = false;
		}
		return experimentRepository.findByProjectAndIsArchive(p, isArchive);
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
	public ModelAndView addNewInstrument(@RequestBody String instrumentName, Model model, RedirectAttributes redir){
		instrumentName = instrumentName.substring(instrumentName.indexOf("instrumentName=") + "instrumentName=".length());
		Instrument instrument = instrumentRepository.findByInstrumentName(instrumentName);
		if(instrument != null){
			redir.addFlashAttribute("error", "Instrument with given name already exists");
			return new ModelAndView("redirect:/viewAddNewInstrument");
		}else{
			Instrument newInstrumentName = new Instrument();
			newInstrumentName.setInstrumentName(instrumentName);
			instrumentRepository.save(newInstrumentName);
			return new ModelAndView("redirect:/viewAddNewExperiment");
		}
	}
	
	@RequestMapping(value = "/viewAddNewExperiment", method = RequestMethod.GET)
	public String addNewExperiment(Model model,@RequestParam(name="project_id", required=false) String project_id, @RequestParam(name="experiment_id", required=false) String experiment_id) {
		model.addAttribute("instrumentList", instrumentRepository.findAll());
		if(StringUtils.isEmpty(project_id)){
			project_id = "1";
		}
		model.addAttribute("project_id",project_id);
		if(!StringUtils.isEmpty(experiment_id)){
			Experiment experiment = experimentRepository.findOne(Long.parseLong(experiment_id));
			if(experiment != null){
				model.addAttribute("experiment",experiment);
			}
		}
		return "addNewExperiment";
	}
	
	@RequestMapping(value = "/addExperiment", method = RequestMethod.POST)
	public ModelAndView add(@Valid @ModelAttribute Experiment experiment, BindingResult errors, @RequestParam("project_id") String project_id, Principal principal) {
		Project project = projectRepository.findOne(Long.parseLong(project_id));
		experiment.setCreateDate(GregorianCalendar.getInstance().getTime());
	 	experiment.setExperimentDate(GregorianCalendar.getInstance().getTime());
	 	experiment.setProject(project);
	 	experimentRepository.save(experiment);
		return new ModelAndView("redirect:/experiment?project_id="+project_id);
	}
	
	@RequestMapping(value = "/updateArchiveStatus", method = RequestMethod.POST)
	@ResponseBody
	public int updateArchiveStatus(@RequestParam long experiment_id, @RequestParam boolean isArchive){
		return experimentRepository.setIsArchiveFor(isArchive, experiment_id);
	}
	
	@RequestMapping(value = "/viewExperimentDetails", method = RequestMethod.GET)
	public String viewExperimentDetails(@RequestParam long experiment_id, Model model){
		Experiment experiment = experimentRepository.findOne(experiment_id);
		model.addAttribute("experiment", experiment);
		return "experimentDetails";
	}
	
	@RequestMapping(value = "/addDataFiles", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Void> addDataFiles(@RequestParam long experiment_id, @ModelAttribute DataFile dataFile, BindingResult errors) throws Exception{
		boolean isDtaFile = dataFile.isDtaFile();
		Experiment experiment = experimentRepository.findOne(experiment_id);
		dataFile.setExperiment(experiment);
		
		String filePath = dataFile.getFilePath();
		String actualFileName = "";
		if(filePath.contains("/")){
			actualFileName = filePath.substring(filePath.lastIndexOf("/") + 1);
		}
		String localFilePath = storageService.getDefaultFilePath().toString() + File.separator + actualFileName;
		String fileName = dataFile.getFileName();
		File file = new File(localFilePath);
		
		dataFile.setUploadedBy(userDetailService.getLoggedInUser().getUsername());
		if(isDtaFile){
			if(file.exists() && file.isFile()){
				DtaFileDetails dtaFileDetails = resultReader.reader(file);
				Calendar cal = Calendar.getInstance();
				int year = cal.get(Calendar.YEAR);
				String yearString = "_" + String.valueOf(year);
				if(fileName.contains(yearString)){
					dtaFileDetails.setName(fileName.substring(0, fileName.indexOf(yearString)) + ".txt");
				}else{
					dtaFileDetails.setName(fileName);
				}
				dtaFileDetails.setDate(GregorianCalendar.getInstance().getTime());
				dtaFileDetails = dtaFileDetailsRepository.save(dtaFileDetails);
				dataFile.setDtaFileDetails(dtaFileDetails);
			}
		}
		
		dataFile = dataFileRepository.save(dataFile);
		storageService.delete(file);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
	@RequestMapping(value="/readDataFileDetails", method = RequestMethod.GET)
	@ResponseBody
	public List<DataFile> readDataFileDetails(@RequestParam long experiment_id){
		Experiment experiment = experimentRepository.findOne(experiment_id);
		List<DataFile> dataFiles = dataFileRepository.findByExperiment(experiment);
		
		List<DataFile> dataFileList = new ArrayList<DataFile>();	
		if(dataFiles != null){
			for(DataFile dataFile : dataFiles){
				DtaFileDetails dtaFileDetails = dataFile.getDtaFileDetails();
				if(dtaFileDetails == null){
					dataFileList.add(dataFile);
				}
				
			}
		}
		return dataFileList;
	}
	
	@RequestMapping(value="/readDtaFileDetails", method = RequestMethod.GET)
	@ResponseBody
	public List<DtaFileDetails> readDtaFileDetails(@RequestParam long experiment_id){
		Experiment experiment = experimentRepository.findOne(experiment_id);
		List<DataFile> dataFiles = dataFileRepository.findByExperiment(experiment);
		
		List<DtaFileDetails> dtaFileList = new ArrayList<DtaFileDetails>();	
		if(dataFiles != null){
			for(DataFile dataFile : dataFiles){
				DtaFileDetails dtaFileDetails = dataFile.getDtaFileDetails();
				if(dtaFileDetails != null){
					dtaFileDetails.setUploadedBy(dataFile.getUploadedBy());
					dtaFileList.add(dtaFileDetails);
				}
				
			}
		}
		return dtaFileList;
	}
}
