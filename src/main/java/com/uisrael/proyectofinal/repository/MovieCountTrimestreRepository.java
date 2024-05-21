package com.uisrael.proyectofinal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.uisrael.proyectofinal.entity.Movie;
import com.uisrael.proyectofinal.entity.MovieCountTrimestre;

public interface MovieCountTrimestreRepository extends CrudRepository<MovieCountTrimestre, Integer> {


	@Query(value = "WITH YearQuarters AS ("
	        + "    SELECT DISTINCT YEAR(relase_date) AS year"
	        + "    FROM Movies"
	        + "), Quarters AS ("
	        + "    SELECT 1 AS quarter UNION ALL"
	        + "    SELECT 2 UNION ALL"
	        + "    SELECT 3 UNION ALL"
	        + "    SELECT 4"
	        + "), AllYearQuarters AS ("
	        + "    SELECT yq.year, q.quarter"
	        + "    FROM YearQuarters yq"
	        + "    CROSS JOIN Quarters q"
	        + "), MovieCounts AS ("
	        + "    SELECT "
	        + "        ayq.year,"
	        + "        ayq.quarter,"
	        + "        COUNT(m.id) AS total_movies"
	        + "    FROM AllYearQuarters ayq"
	        + "    LEFT JOIN Movies m ON ayq.year = YEAR(m.relase_date) AND ayq.quarter = QUARTER(m.relase_date)"
	        + "    GROUP BY ayq.year, ayq.quarter"
	        + "    ORDER BY ayq.year, ayq.quarter"
	        + ")"
	        + "SELECT year, "
	        + "       SUM(CASE WHEN quarter = 1 THEN total_movies ELSE 0 END) AS Q1,"
	        + "       SUM(CASE WHEN quarter = 2 THEN total_movies ELSE 0 END) AS Q2,"
	        + "       SUM(CASE WHEN quarter = 3 THEN total_movies ELSE 0 END) AS Q3,"
	        + "       SUM(CASE WHEN quarter = 4 THEN total_movies ELSE 0 END) AS Q4"
	        + " FROM MovieCounts"
	        + " GROUP BY year"
	        + " ORDER BY year desc",  nativeQuery = true)
	public List<MovieCountTrimestre> findMovieQuarter();


}
