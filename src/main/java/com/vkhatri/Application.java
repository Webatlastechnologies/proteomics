package com.vkhatri;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.vkhatri.modal.Transaction;
import com.vkhatri.modal.User;
import com.vkhatri.modal.constants.Gender;
import com.vkhatri.repository.TransactionRepository;
import com.vkhatri.repository.UserRepository;

@SpringBootApplication
@EntityScan("com.vkhatri.modal")
@EnableJpaRepositories(basePackages={"com.vkhatri.repository"})
@ComponentScan(basePackages={"com.vkhatri.controller","com.vkhatri.config","com.vkhatri.service"})
@EnableWebMvc
public class Application {
	
	private static final Logger log = LoggerFactory.getLogger(Application.class);
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	@Bean
	public CommandLineRunner demo(UserRepository userRepository,TransactionRepository transactionRepository) {
		return (args) -> {
			
			userRepository.save(new User("Vishal1","Khatri1","vishalsarkar20501@gmail.com","vishal1","vishal1","ADMIN",Gender.MALE));
			userRepository.save(new User("Vishal2","Khatri2","vishalsarkar20502@gmail.com","vishal2","vishal2","ADMIN",Gender.MALE));
			userRepository.save(new User("Vishal3","Khatri3","vishalsarkar20503@gmail.com","vishal3","vishal3","ADMIN",Gender.MALE));
			
			// save a couple of transactions
			transactionRepository.save(new Transaction("Vishal1",1.1));
			transactionRepository.save(new Transaction("Vishal2",1.2));
			transactionRepository.save(new Transaction("Vishal3",1.3));
						
			User u=userRepository.findOne(1L);
			Transaction t1=transactionRepository.findOne(1L);
			Transaction t2=transactionRepository.findOne(2L);
			t1.setUser(u);
			transactionRepository.save(t1);
			t2.setUser(u);
			transactionRepository.save(t2);
			
			
			// fetch all customers
			log.info("Persons found with findAll():");
			log.info("-------------------------------");
			for (User person: userRepository.findAll()) {
				log.info(person.toString());
			}
            log.info("");

			// fetch an individual customer by ID
			User person = userRepository.findOne(1L);
			log.info("Person found with findOne(1L):");
			log.info("--------------------------------");
			log.info(person.toString());
            log.info("");

			// fetch customers by last name
			log.info("Person found with findByLastName('Bauer'):");
			log.info("--------------------------------------------");
			for (User khatri3 : userRepository.findByLastName("Khatri3")) {
				log.info(khatri3.toString());
			}
            log.info("");
		};
	}
}
