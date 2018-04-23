package com.demo.springtask.service;

import com.demo.springtask.model.User;
import com.demo.springtask.repo.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Primary
public class UserService implements UserDetailsService {

	private final static Logger log = LoggerFactory.getLogger(UserService.class);
	private static BCryptPasswordEncoder encoder = new BCryptPasswordEncoder() ;
	private final UserRepository userRepository;

	@Autowired
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}


	@Override
	public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
//		log.info("чо пришло " + s);
		User user = userRepository.findByName(s);

//		log.info("user found " + user );
		if (user == null) {
			throw new UsernameNotFoundException(String.format("The username %s doesn't exist", s));
		}
//todo
		List<GrantedAuthority> authorities = new ArrayList<>();
		if (user.getRole().equalsIgnoreCase("DEVELOPER")) {
			authorities.add(new SimpleGrantedAuthority("DEVELOPER"));
		}
		if (user.getRole().equalsIgnoreCase("USER")) {
			authorities.add(new SimpleGrantedAuthority("USER"));
		}

		UserDetails userDetails = new org.springframework.security.core.userdetails.
				User(user.getName(), user.getPassword(), authorities);

		return userDetails;
	}

	public void init () {
		this.updatePass("Shama", "Shama123");
		this.updatePass("Stas", "Stas123");
		this.updatePass("Maga", "Maga123");
		log.info("все в базе ");
		String email = "shama@mail.ru";
		User user = userRepository.findUserByEmail(email);
		log.info("user is " + user);
	}

	private void updatePass(String userName, String password) {
		User user  = userRepository.findByName(userName);
//		log.info("user found " +   user.getName()  );
		user.setPassword( encoder.encode(password)) ;
		userRepository.save(user);
//		user = userRepository.findByName(userName);
//		log.info("user found " + user + "\n : " +  user.getPassword()  );
	}
}

