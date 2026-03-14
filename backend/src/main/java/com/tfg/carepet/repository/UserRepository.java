package com.tfg.carepet.repository;

import com.tfg.carepet.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Consulta personalizada: Buscar usuario por email
    Optional<User> findByEmail(String email);

    // Consulta personalizada: Verificar si existe un email
    boolean existsByEmail(String email);
}
