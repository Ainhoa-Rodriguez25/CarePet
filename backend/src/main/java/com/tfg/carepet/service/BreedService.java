package com.tfg.carepet.service;

import com.tfg.carepet.dto.BreedResponse;
import com.tfg.carepet.model.Breed;
import com.tfg.carepet.repository.BreedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor // Qué hace???
public class BreedService {

    private final BreedRepository breedRepository;

    // Listar todas las razas
    public List<BreedResponse> getAllBreeds() {
        return breedRepository.findAll()
                .stream() // convierte la lista en stream para procesarla
                .map(this::convertToResponse) // convierte cada Breed en BreedResponse
                .collect(Collectors.toList()); // se vuelve a convertir el stream en una lista
    }

    // Listar razas por especie
    public List<BreedResponse> getBreedsBySpecies(String species) {
        Breed.Species speciesEnum = Breed.Species.valueOf(species);

        return breedRepository.findBySpeciesOrderByNameAsc(speciesEnum)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // Convertir Entity en DTO
    private BreedResponse convertToResponse(Breed breed) {
        return new BreedResponse(
                breed.getId(),
                breed.getName(),
                breed.getSpecies().name()
        );
    }
}
