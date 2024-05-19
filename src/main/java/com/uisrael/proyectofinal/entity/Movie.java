package com.uisrael.proyectofinal.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "Movie")
@Table(name= "movies")
public class Movie {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	 @NotBlank(message = "El título no puede estar en blanco")
	@Column(  length = 150)
	public String title;
	 @NotBlank(message = "El Género no puede estar en blanco")
	@Column(length = 150)
	public String genre;
	 @NotBlank(message = "El nombre del director no puede estar en blanco")
	@Column( length = 150)
	public String director;
	@NotBlank
	 @NotBlank(message = "Ingrese un imagen referenta a la película (URL)")
	@Column(  length = 100000)
	public String poster;
 
	@Column(  length = 600)
	public String plot;
 

	@Column(columnDefinition = "float default 0")
	private double rating;
	
	@Column(length = 150)
	private String comment;
	
	

	@ManyToMany
	@JoinTable(name = "movies_users_ratings",
	      joinColumns = { @JoinColumn(name = "movie_id") },
	      inverseJoinColumns = { @JoinColumn(name = "user_id") })
	private Set<User> users = new HashSet<>();
	}
