package com.tfg.carepet.repository;

import com.tfg.carepet.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {

    // Buscar mascotas por hogar
    List<Pet> findByHouseholdId(Long householdId);

    // Buscar mascotas por hogar y especie
    List<Pet> findByHouseholdIdAndSpecies(Long householdId, Pet.Species species);
}
