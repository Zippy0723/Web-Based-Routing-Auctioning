package edu.sru.thangiah.webrouting.services;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import edu.sru.thangiah.webrouting.domain.Role;
import edu.sru.thangiah.webrouting.domain.User;
import edu.sru.thangiah.webrouting.repository.UserRepository;

/**
 * Implements the UserDetailsService interface <br>
 * Used as a service for Spring Security
 * @author Logan Kirkwood	llk1005@sru.edu
 * @since 2/7/2022
 */

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;
	

	/**
	 * Finds the username using the userRepository. <br>
	 * Adds the user roll to the granted authority. <br>
	 * Returns the UserDetails of the user.
	 * @param username Username of the user
	 * @return UserDetails
	 */
	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) {
		User user = userRepository.findByUsername(username);
		if (user == null) throw new UsernameNotFoundException(username);
	
		if(!user.isEnabled()) {
			throw new DisabledException(("User is disabled"));
		}
	
		Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
		
		Role role = user.getRole();
		
        grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
		
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), grantedAuthorities);
	}
	

	
}
