package guru.springframework.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import guru.springframework.domain.User;
import guru.springframework.repositories.LabRepository;
import guru.springframework.repositories.SiteUpdateMessageRepository;
import guru.springframework.repositories.UserRepository;

@Controller
public class IndexController {

	@Autowired
	UserRepository userRepository;
	@Autowired
	SiteUpdateMessageRepository messageRepository;
	@Autowired
	LabRepository labRepository;
	
	
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
		model.addAttribute("error", "Invalid username and password!");
	  }
	  if (logout != null) {
		model.addAttribute("msg", "You've been logged out successfully.");
	  }
	  return "login";
	}
    
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String registration(Model model) {
        model.addAttribute("userForm", new User());
        model.addAttribute("labs", labRepository.findAll());
        return "register";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String registration(@ModelAttribute("userForm") User user, BindingResult bindingResult, Model model) {
    	user.setRole("USER");
        userRepository.save(user);
        model.addAttribute("msg","Registered Successfully");
        return "login";
    }

	public UserRepository getUserRepository() {
		return userRepository;
	}

	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
    
    
}
