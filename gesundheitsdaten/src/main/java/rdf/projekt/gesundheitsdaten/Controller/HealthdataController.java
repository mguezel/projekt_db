package rdf.projekt.gesundheitsdaten.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import rdf.projekt.gesundheitsdaten.Data.HealthData;
import rdf.projekt.gesundheitsdaten.Repository.HealthDataRepository;

@RestController
public class HealthdataController {

    @Autowired
    private HealthDataRepository healthDataRepository;


    public HealthdataController() {}
    
    @GetMapping("/api/healthdata")
    public List<HealthData> getHealthData() {
        return healthDataRepository.findAll();
    }
    
}
