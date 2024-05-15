package com.uisrael.proyectofinal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.uisrael.proyectofinal.entity.User;

public interface UserRepository extends JpaRepository<User, Long>{

}
