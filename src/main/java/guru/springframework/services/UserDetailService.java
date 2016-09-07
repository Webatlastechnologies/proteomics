package guru.springframework.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import guru.springframework.repositories.UserRepository;

@Component
public class UserDetailService implements UserDetailsService {

	private UserRepository repository;

	@Autowired
	public void setUserDetailService(UserRepository repository) {
		this.repository = repository;
	}

	@Override
	public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
		guru.springframework.domain.User user  = this.repository.findByUsername(usernameOrEmail);
		repository.findAll(new PageRequest(1, 20));
		return new User(user.getUsername(),user.getPassword(),AuthorityUtils.createAuthorityList(user.getRole()));
	}
	

}