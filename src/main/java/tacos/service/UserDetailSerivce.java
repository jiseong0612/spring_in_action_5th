package tacos.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserDetailSerivce {
	UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
