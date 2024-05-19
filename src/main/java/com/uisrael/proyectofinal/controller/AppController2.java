package com.uisrael.proyectofinal.controller;


import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import com.uisrael.proyectofinal.entity.Movie;
import com.uisrael.proyectofinal.entity.User;
import com.uisrael.proyectofinal.repository.MovieRepository;
import com.uisrael.proyectofinal.repository.UserRepository;
@Controller
public class AppController2 {
	
	
		private final MovieRepository movieRepository;
		private final UserRepository userRepository;
		
		
		public AppController2(MovieRepository movieRepository, UserRepository userRepository) {
			this.movieRepository = movieRepository;
			this.userRepository=userRepository;
		}
	
		@PostMapping("/addMovie")
		public String showMovieForm(Movie movie) {
			movieRepository.saveAndFlush(movie);
			return "redirect:/MainPage";
		}
		
		@GetMapping("/addMovie")
		public String moviePage(Model model, HttpSession httpSession) {
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
			
	        return "AddMovie";
	    }
		
		@GetMapping("/EditProfile")
		public String editPage(Model model, HttpSession httpSession) {
			Long userId=(Long)httpSession.getAttribute("id");
			User userById = null;
			String userRol = null;
			
			if(userId != null) {
				userById = userRepository.findById(userId).get();
				
				if(userById != null) {
					userRol = userById.getRol();
				}
			}
	
			model.addAttribute("user", userById);

			System.out.println(userById);
			System.out.println(userRol);
			model.addAttribute("userById", userById);
			model.addAttribute("userRol", userRol);
	       
			return "EditProfile";
	    }
		
		@PostMapping("/EditProfile")
	    public String editPageConfirm(HttpSession httpSession, @ModelAttribute User user) {
			
	      user.setId((Long) httpSession.getAttribute("id"));
	      
	      httpSession.removeAttribute("email");
	       userRepository.save(user);
	       
	       
			return "redirect:/login";
	    }
		
		@DeleteMapping("/remove/{movieId}")
		public String removeList(HttpSession httpSession, @PathVariable String movieId) {
			Long userId=(Long)httpSession.getAttribute("id");
			
			User userById = userRepository.findById(userId).get();
			Movie movieById = movieRepository.findById(Long.valueOf(movieId)).get();
	
			userById.getMovies().remove(movieById);
			movieById.getUsers().remove(userById);
			
			userRepository.saveAndFlush(userById);
			movieRepository.saveAndFlush(movieById);
			
			return "redirect:/myList";
		
		}
	}		
		
	



