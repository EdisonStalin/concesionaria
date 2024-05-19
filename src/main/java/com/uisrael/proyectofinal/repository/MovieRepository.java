package com.uisrael.proyectofinal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.uisrael.proyectofinal.entity.Movie;

public interface MovieRepository extends CrudRepository<Movie, Long> {

	    @Query(value = "select m from Movie m WHERE m.title LIKE %?1% or m.plot LIKE %?1% or m.director LIKE %?1% or m.genre LIKE %?1%")
		public List<Movie> findByKeyword(String keyword);
	}

