package com.uisrael.proyectofinal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class Pelicula {
	@GetMapping
	public String home() {
		return "redirect:/index";
	}
	
	@GetMapping("/index")
	public String mostrarPeliculas(){
		return "index";
	}
	
	@GetMapping("/movies")
    public String listarPeliculas(){
        return "movies";
    }
	
	
	@GetMapping("/movies/new")
    public String agregarPelicula(){
        return "register";
    }
	
	
	@GetMapping("/movies/registration_form")
	public String registrarUsuario() {
		return "login_registration_form";
	}
	
	@GetMapping("/movies/login")
	public String inicioSesion() {
		return "login";
	}
}
