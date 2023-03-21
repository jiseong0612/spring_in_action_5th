package tacos.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import tacos.data.UserRepository;
import tacos.domain.User;
@Service
public class UserRepositoryUserDetailsService implements UserDetailsService {
	private UserRepository userRepo;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepo.findByUsername(username);
		
		if(user != null) {
			return user;
		}
		throw new UsernameNotFoundException("user : " +username +" not found!!!");
	}
	
}
