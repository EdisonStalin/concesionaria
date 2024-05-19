package com.uisrael.proyectofinal.controller;

import java.util.LinkedHashSet;
import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.uisrael.proyectofinal.entity.Movie;
import com.uisrael.proyectofinal.entity.User;
import com.uisrael.proyectofinal.repository.MovieRepository;
import com.uisrael.proyectofinal.repository.UserRepository;

@Controller
public class AppController3 {
	@Autowired
    private MovieRepository movieRepository;
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/MainPage")
	public String listMovies(Model model, String keyword, HttpSession httpSession){
		List<Movie> movies = keyword == null ? (List<Movie>) movieRepository.findAll() : movieRepository.findByKeyword(keyword);
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
		
        model.addAttribute("movies", movies);

        return "MainPage";
    }
	
	
	@GetMapping("/myList")
	public String myList(Model model, HttpSession session) {
		Long id=(Long)session.getAttribute("id");
	    User user = userRepository.findById(id).get();
		user.getMovies();
        model.addAttribute("movies", user.getMovies());
		return "MyList";
	}
	
	
	
	@PostMapping("/addStar/{movieId}")
	public String addStar(@RequestParam Long rate, @PathVariable("movieId") Long movieId, HttpSession session) {
		System.out.println("Rate: " + rate);
		System.out.println("Movie id: " + movieId);
		Long userId=(Long)session.getAttribute("id");
		
		User user = userRepository.findById(userId).get();
		Movie movie = movieRepository.findById(movieId).get();
		
		if (movie.getUsers().stream().filter(u -> u.getId() == userId).findFirst().orElse(null) == null) {
			double rating = movie.getRating() == 0 ? rate : ((movie.getRating() + rate) / 2);
			movie.setRating(rating);
			movie.getUsers().add(user);
			
			movieRepository.save(movie);
		}
		
		return "redirect:/MainPage";
	}
	
	@PostMapping("/addToFav/{movieId}")
	public String addToFav(HttpSession httpSession, @PathVariable("movieId") Long movieId) {
		Long userId=(Long)httpSession.getAttribute("id");
		
		User userById = userRepository.findById(userId).get();
		Movie movieById = movieRepository.findById(movieId).get();
		
		if (userById != null) {
			if (userById.getMovies() == null) {
				userById.setMovies(new LinkedHashSet<>());
			}
			
			Movie existsing = userById.getMovies().stream().filter(m -> m.getId() == movieId).findAny().orElse(null);
			
			if (existsing == null) {
				userById.getMovies().add(movieById);
				userRepository.save(userById);
				
				if (movieById.getUsers() == null) {
					movieById.setUsers(new LinkedHashSet<>());
				}
				
				movieById.getUsers().add(userById);
				movieRepository.save(movieById);
			}
		} 
		
		return "redirect:/MainPage";
	}
	

	@GetMapping("/report")
	public String mostrarPieChart(Model model, HttpSession httpSession) {
		List<Movie> movies =  (List<Movie>) movieRepository.findAll();
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
		
		model.addAttribute("movies", movies);
		 for (Movie movie : movies) {
		        System.out.println("Title: " + movie.getTitle() + ", Rating: " + movie.getRating());
		    }
		// Crear el JSON para el gráfico de Google Charts
        StringBuilder chartData = new StringBuilder("[[\"Title\", \"Rating\"]");
        for (Movie movie : movies) {
            chartData.append(", [\"")
                     .append(movie.getTitle())
                     .append("\", ")
                     .append(movie.getRating())
                     .append("]");
        }
        chartData.append("]");
        
        String chartDataString = chartData.toString();
        System.out.println("ChartData: " + chartDataString);
        model.addAttribute("chartData", chartDataString);
		return "Report";
	}

    }