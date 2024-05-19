package com.uisrael.proyectofinal.controller;

import jakarta.servlet.http.HttpSession;

import java.util.Arrays;
import java.util.List;

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
	public String home(Model model, HttpSession httpSession) {
		Long userId=(Long)httpSession.getAttribute("id");
		User userById = null;
		String userRol = null;
		
		if(userId != null) {
			userById = userRepository.findById(userId).get();
			
			if(userById != null) {
				userRol = userById.getRol();
			}
		}

		model.addAttribute("userById", userById);
		model.addAttribute("userRol", userRol);
		
		return "redirect:/MainPage";
	}
	
	
	@PostMapping("/register")
	public String registerUser(User model, HttpSession httpSession) {
		Long userId=(Long)httpSession.getAttribute("id");
		
		if(userId == null) {
			model.setRol("Cliente");
		}
		
		userRepository.saveAndFlush(model);
		return "login";
	}
	
	@GetMapping("/register")
    public String registerPage(Model model, HttpSession httpSession) {
		Long userId=(Long)httpSession.getAttribute("id");
		User userById = null;
		String userRol = null;
		
		if(userId != null) {
			userById = userRepository.findById(userId).get();
			
			if(userById != null) {
				userRol = userById.getRol();
			}
		}

		model.addAttribute("userById", userById);
		model.addAttribute("userRol", userRol);
		
		List<String> roles = Arrays.asList("Admin", "Cliente");
		model.addAttribute("roles", roles);
		
        return "Registration";
    }
	
	@GetMapping("/login")
    public String loginPage(Model model, HttpSession httpSession) {
		Long userId=(Long)httpSession.getAttribute("id");
		User userById = null;
		String userRol = null;
		
		if(userId != null) {
			userById = userRepository.findById(userId).get();
			
			if(userById != null) {
				userRol = userById.getRol();
			}
		}

		model.addAttribute("userById", userById);
		model.addAttribute("userRol", userRol);
		
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
	
	
	
	

