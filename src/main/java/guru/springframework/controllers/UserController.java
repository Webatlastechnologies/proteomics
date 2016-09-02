package guru.springframework.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import guru.springframework.domain.User;
import guru.springframework.repositories.SiteUpdateMessageRepository;
import guru.springframework.repositories.UserRepository;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	SiteUpdateMessageRepository messageRepository;
	
	@RequestMapping({"/",""})
	public String get(){
		return "admin-user";
	}
	
	@RequestMapping(value="/read", method = RequestMethod.POST)
	public @ResponseBody List<User> read(){
		return userRepository.findAll();
	}
	
	 @RequestMapping(value = "/update", method = RequestMethod.POST)
		 public @ResponseBody User update(@Valid @ModelAttribute User user, BindingResult errors) {
			 userRepository.save(user);
	        return user;
	    }

	 @RequestMapping(value = "/delete", method = RequestMethod.POST)
	    public @ResponseBody String update(@RequestParam long user_id) {
		 User user=userRepository.findOne(user_id);
		 	userRepository.delete(user);
	        return "";
	    }
}
