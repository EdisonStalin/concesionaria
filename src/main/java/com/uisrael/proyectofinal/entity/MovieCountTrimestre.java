package com.uisrael.proyectofinal.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
@Entity
@Setter
@Table
public class MovieCountTrimestre {

	@Id
	private int year;
    private int Q1;
    private int Q2;
    private int Q3;
    private int Q4;
}
