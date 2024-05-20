package com.uisrael.proyectofinal.controller;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
		    public String listMovies(Model model, String keyword, HttpSession session){
				List<Movie> movies = keyword == null ? movieRepository.findAll() : movieRepository.findByKeyword(keyword);
				boolean isLoggedIn = ((Long)session.getAttribute("id")) != null;
				
				System.out.println((Long)session.getAttribute("id"));
				
		        model.addAttribute("movies", movies);
		        model.addAttribute("isLoggedIn", isLoggedIn);
		        
		        System.out.println("HELLO: "+ isLoggedIn);
		        return "MainPage";
		    }
	
	@PostMapping("/addStarAndComment/{movieId}")
	public String addStarAndComment(@RequestParam Long rate, @RequestParam String comment, @PathVariable("movieId") Long movieId, HttpSession session) {
	    // Obtener el usuario de la sesión
	    Long userId = (Long) session.getAttribute("id");
	    User user = userRepository.findById(userId).orElse(null);

	    if (user == null) {
	        // Manejar el caso cuando el usuario no está autenticado
	        return "redirect:/login"; // Redirigir al formulario de inicio de sesión
	    }

	    // Obtener la película
	    Movie movie = movieRepository.findById(movieId).orElse(null);

	    if (movie != null) {
	        // Verificar si el usuario ya ha valorado la película
	        if (movie.getUsers().stream().noneMatch(u -> u.getId().equals(userId))) {
	            // Calcular el nuevo rating
	            double newRating = (movie.getRating() + rate) / 2;

	            // Asignar el nuevo rating y el comentario
	            movie.setRating(newRating);
	            movie.setComment(comment);

	            // Agregar el usuario a la lista de usuarios que han valorado la película
	            movie.getUsers().add(user);

	            // Guardar la película
	            movieRepository.save(movie);
	        }
	    }

	    return "redirect:/MainPage";
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
    public String addStar(@RequestParam Long rate, @RequestParam String comment, @PathVariable("movieId") Long movieId, HttpSession session, RedirectAttributes redirectAttributes) {
        Long userId = (Long) session.getAttribute("id");

        if (userId == null) {
            return "redirect:/login"; // Redirige al usuario a la página de inicio de sesión si no está autenticado
        }

        try {
            Optional<User> userOptional = userRepository.findById(userId);
            Optional<Movie> movieOptional = movieRepository.findById(movieId);

            if (userOptional.isEmpty() || movieOptional.isEmpty()) {
                return "redirect:/MainPage"; // Maneja el caso donde el usuario o la película no existen
            }

            User user = userOptional.get();
            Movie movie = movieOptional.get();

            boolean userAlreadyRated = movie.getUsers().stream().anyMatch(u -> u.getId().equals(userId));

            if (!userAlreadyRated) {
                // Calcula la nueva calificación y agrega la calificación y el comentario a la película
                double newRating = (movie.getRating() * movie.getUsers().size() + rate) / (movie.getUsers().size() + 1);
                movie.setRating(newRating);
                movie.setComment(comment);
                movie.getUsers().add(user);
                movieRepository.save(movie);
            } else {
                // Si el usuario ya ha valorado la película, redirige a una página informativa
                redirectAttributes.addFlashAttribute("message", "¡Ya has valorado esta película!");
                return "redirect:/MainPage";
            }

            return "redirect:/MainPage"; // Redirige al usuario a la página principal
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/MainPage"; // Maneja cualquier error que pueda ocurrir
        }
    }
	
	
	
	@PostMapping("/addComment/{movieId}")
	public String addComment(@RequestParam String comment, @PathVariable("movieId") Long movieId, HttpSession session) {
	    Long userId = (Long) session.getAttribute("id");

	    if (userId == null) {
	        // Manejar el caso cuando el usuario no está autenticado
	        return "redirect:/login"; // Redirigir al formulario de inicio de sesión
	    }

	    Movie movie = movieRepository.getOne(movieId); // Obtener una referencia a la entidad Movie
	    User user = userRepository.findById(userId).orElse(null);

	    if (movie != null && user != null) {
	        // Agregar el comentario al objeto Movie y guardar en la base de datos
	        movie.setComment(comment);
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
	public String mostrarPieChart(Model model) {
		List<Movie> movies =  movieRepository.findAll();
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