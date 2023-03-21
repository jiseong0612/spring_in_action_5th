package tacos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import tacos.data.UserRepository;

@Controller
@RequestMapping("/register")
public class RegistrationController {
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@GetMapping("/register")
	public String registerForm() {
		return "registration";
	}
	
	@PostMapping("/register")
	public String porcessRegistration(RegistrationForm form) {
		userRepo.save(form.toUser(passwordEncoder));
		return "redirect:/login";
	}
}
