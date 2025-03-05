package rdf.projekt.gesundheitsdaten.Repository;


import org.springframework.data.mongodb.repository.MongoRepository;

import rdf.projekt.gesundheitsdaten.Data.HealthData;

public interface HealthDataRepository extends MongoRepository<HealthData, String> {
   
}