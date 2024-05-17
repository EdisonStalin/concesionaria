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
	
	@Column( unique=true, length = 150)
	public String title;
	
	@Column(length = 150)
	public String genre;
	
	@Column(nullable=false, length = 150)
	public String director;
	
	@Column(nullable=false, length = 100000)
	public String poster;
	
	@Column(nullable=false, length = 600)
	public String plot;
	
	@Column(nullable=false, length = 100000)
	private String trailerurl;
	
	@Column(columnDefinition = "float default 0")
	private double rating;
	
	

	@ManyToMany
	@JoinTable(name = "movies_users_ratings",
	      joinColumns = { @JoinColumn(name = "movie_id") },
	      inverseJoinColumns = { @JoinColumn(name = "user_id") })
	private Set<User> users = new HashSet<>();
	}
