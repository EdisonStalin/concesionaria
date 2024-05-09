package com.uisrael.proyectofinal.controller;

import org.springframework.web.bind.annotation.GetMapping;

public class Pelicula {
	@GetMapping
	public String home() {
		return "redirect:/index";
	}
	
	@GetMapping("/index")
	public String listarReservas(){
		return "index";
	}
}
