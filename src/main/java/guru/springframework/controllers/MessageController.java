package guru.springframework.controllers;

import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import guru.springframework.domain.SiteUpdateMessage;
import guru.springframework.repositories.SiteUpdateMessageRepository;
import guru.springframework.repositories.UserRepository;

@Controller
@RequestMapping("/message")
public class MessageController {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	SiteUpdateMessageRepository messageRepository;
	
	@RequestMapping({"/",""})
	public String get(){
		return "admin-message";
	}
	
	@RequestMapping(value="/read", method = RequestMethod.POST)
	public @ResponseBody List<SiteUpdateMessage> read(){
		return messageRepository.findAll();
	}
	
	 @RequestMapping(value = "/update", method = RequestMethod.POST)
	    public @ResponseBody SiteUpdateMessage update(@RequestParam String updateText,@RequestParam long updateId) {
		 SiteUpdateMessage msg=messageRepository.findOne(updateId);
		 msg.setUpdateText(updateText);
		 	messageRepository.save(msg);
	        return msg;
	    }
	 @RequestMapping(value = "/save", method = RequestMethod.POST)
	    public @ResponseBody SiteUpdateMessage update(@RequestParam String updateText) {
		 	SiteUpdateMessage msg=new SiteUpdateMessage();
		 	msg.setUpdateText(updateText);
		 	msg.setUpdateDate(GregorianCalendar.getInstance().getTime());
			 messageRepository.save(msg);
	        return msg;
	    }

	 @RequestMapping(value = "/delete", method = RequestMethod.POST)
	    public @ResponseBody String update(@RequestParam long updateId) {
		 SiteUpdateMessage msg=messageRepository.findOne(updateId);
		 	messageRepository.delete(msg);
	        return "";
	    }
}
