package com.uisrael.proyectofinal.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.uisrael.proyectofinal.entity.User;

import com.uisrael.proyectofinal.repository.UserRepository;

@Controller
public class AppController {
	
	private final UserRepository userRepository;
	
	
	public AppController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	@GetMapping
	public String home() {
		return "redirect:/MainPage";
	}
	
	
	@PostMapping("/register")
	public String registerUser(User model) {
		userRepository.saveAndFlush(model);
		return "login";
	}
	
	@GetMapping("/register")
    public String registerPage() {
        return "Registration";
    }
	
	@GetMapping("/login")
    public String loginPage() {
        return "login";
	}
	
	@PostMapping("/login")
    public String loginConfirm(@RequestParam("email") String email,@RequestParam("password") String password,
			HttpSession session) {
		
		User user = userRepository.findAll().stream().filter(u -> u.getEmail().equals(email) && u.getPassword().equals(password)).findFirst().orElse(null);
		
		if (user != null) {
			session.setAttribute("email",email);
			session.setAttribute("id", user.getId());
			 return "redirect:/MainPage";
		}
		
		return "login";
	}
	
	@PostMapping("/logout")
    public String logout(HttpSession session) {
		session.removeAttribute("email");
		session.setAttribute("id", null);
		
	
	return "redirect:/login";
	}
	
	

	
	
	
	
	
}
	
	
	
	

