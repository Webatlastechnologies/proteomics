package guru.springframework.controllers;

import javax.mail.internet.AddressException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import guru.springframework.domain.Lab;
import guru.springframework.domain.User;
import guru.springframework.repositories.LabRepository;
import guru.springframework.repositories.SiteUpdateMessageRepository;
import guru.springframework.repositories.UserRepository;
import guru.springframework.services.Utils;

@Controller
public class IndexController {

	@Autowired
	UserRepository userRepository;
	@Autowired
	SiteUpdateMessageRepository messageRepository;
	@Autowired
	LabRepository labRepository;
	@Autowired
	Utils utils;
	@Value("${admin.email}")
	private String adminEmail;
	
	@RequestMapping({"/home","/"})
    String home(Model model){
    	System.out.println("inside home");
    	model.addAttribute("messages",messageRepository.findAll());
        return "home";
    }
    
    @RequestMapping(value = {"/login"}, method = RequestMethod.GET)
	public String login(@RequestParam(value = "error", required = false) String error,
		@RequestParam(value = "logout", required = false) String logout,Model model) {
    	System.out.println("inside login");
	  if (error != null) {
		model.addAttribute("danger", "Invalid username and password!");
	  }
	  if (logout != null) {
		model.addAttribute("info", "You've been logged out successfully.");
	  }
	  return "login";
	}
    
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String registration(Model model) {
        model.addAttribute("userForm", new User());
        model.addAttribute("labs", labRepository.findByApproved(true));
        return "register";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String registration(@ModelAttribute("userForm") User user, BindingResult bindingResult, Model model) {
    	user.setRole("ROLE_USER");
        userRepository.save(user);
        try {
			utils.sendEmail(user.getEmail(),"Registration Successfull",String.format("Dear %s, Thank you for registration!",user.getFirstName()));
		} catch (AddressException e) {
			model.addAttribute("danger",String.format("Error occured. Contact %s",adminEmail));
		}
        model.addAttribute("info","Thank you for registration!");
        return "login";
    }
    
    @RequestMapping(value = "/lab-register", method = RequestMethod.GET)
    public String labRegistration(Model model) {
        model.addAttribute("labForm", new Lab());
        return "laboratory-request";
    }

    @RequestMapping(value = "/lab-register", method = RequestMethod.POST)
    public String labRegistration(@ModelAttribute("labForm") Lab lab, BindingResult bindingResult, Model model) {
        labRepository.save(lab);
        try {
			utils.sendEmail(lab.getRequestorEmail(),"Request Submitted",String.format("Your lab \"%s\" addition request has been submitted successfully",lab.getLabName()));
			utils.sendEmail(adminEmail,"Lab Add Request Received",String.format("You Received a lab \"%s\" addition request.",lab.getLabName()));
        } catch (AddressException e) {
        	model.addAttribute("danger",String.format("Error occured. Contact %s",adminEmail));
		}
        model.addAttribute("info",String.format("Lab \"%s\" request submitted successfully",lab.getLabName()));
        return "login";
    }
    
    

	public UserRepository getUserRepository() {
		return userRepository;
	}

	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
    
    
}
