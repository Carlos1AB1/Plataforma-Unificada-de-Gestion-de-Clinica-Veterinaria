package com.veterinary.clinic.reportservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.util.List;

@FeignClient(name = "patient-service")
public interface PatientClient {

    @GetMapping("/api/patients")
    List<PatientResponseDTO> getAllPatients();

    @GetMapping("/api/patients/{id}")
    PatientResponseDTO getPatientById(@PathVariable("id") Long id);

    @GetMapping("/api/patients/count/total")
    Long getTotalPatientsCount();

    @GetMapping("/api/patients/count/month")
    Long getNewPatientsThisMonthCount();

    @GetMapping("/api/patients/statistics/species")
    List<SpeciesStatisticsDTO> getPatientsBySpeciesStatistics();

    class PatientResponseDTO {
        private Long id;
        private String name;
        private String species;
        private String breed;
        private Integer age;
        private Double weight;
        private Long ownerId;
        private String ownerName;
        private LocalDateTime createdAt;

        // Constructors
        public PatientResponseDTO() {}

        // Getters and Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getSpecies() { return species; }
        public void setSpecies(String species) { this.species = species; }

        public String getBreed() { return breed; }
        public void setBreed(String breed) { this.breed = breed; }

        public Integer getAge() { return age; }
        public void setAge(Integer age) { this.age = age; }

        public Double getWeight() { return weight; }
        public void setWeight(Double weight) { this.weight = weight; }

        public Long getOwnerId() { return ownerId; }
        public void setOwnerId(Long ownerId) { this.ownerId = ownerId; }

        public String getOwnerName() { return ownerName; }
        public void setOwnerName(String ownerName) { this.ownerName = ownerName; }

        public LocalDateTime getCreatedAt() { return createdAt; }
        public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    }

    class SpeciesStatisticsDTO {
        private String species;
        private Long count;

        public SpeciesStatisticsDTO() {}

        public String getSpecies() { return species; }
        public void setSpecies(String species) { this.species = species; }

        public Long getCount() { return count; }
        public void setCount(Long count) { this.count = count; }
    }
}
