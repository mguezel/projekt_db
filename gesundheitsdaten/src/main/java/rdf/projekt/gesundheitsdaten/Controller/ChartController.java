package rdf.projekt.gesundheitsdaten.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import rdf.projekt.gesundheitsdaten.Data.HealthData;
import rdf.projekt.gesundheitsdaten.Repository.HealthDataRepository;    


@Controller
public class ChartController {

    @Autowired
    private HealthDataRepository repository;

    @GetMapping("/")
    public String index(Model model) {
        List<HealthData> data = repository.findAll();

        // Bar Chart: Total Ausgaben by Jahr
        Map<Integer, Integer> ausgabenByJahr = data.stream()
            .filter(d -> !"Insgesamt".equals(d.getAusgabentraeger())) // Filter out the "Insgesamt" entry
            .collect(Collectors.groupingBy(
                HealthData::getJahr,
                Collectors.summingInt(HealthData::getAusgaben)
            ));
        List<ChartData> barChartData = ausgabenByJahr.entrySet().stream()            
            .map(entry -> new ChartData(entry.getKey(), entry.getValue()))
            .sorted((a, b) -> Integer.compare(a.jahr, b.jahr))
            .collect(Collectors.toList());

                // Pie Chart: Ausgaben by Ausgabentraeger for each Jahr (excluding "Insgesamt")
        Map<Integer, Map<String, Integer>> ausgabenByJahrAndTraeger = data.stream()
        .filter(d -> !"Insgesamt".equals(d.getAusgabentraeger())) // Filter out the "Insgesamt" entry
        .collect(Collectors.groupingBy(
            HealthData::getJahr,
            Collectors.groupingBy(
                HealthData::getAusgabentraeger,
                Collectors.summingInt(HealthData::getAusgaben)
            )
        ));

        // List of unique years for combobox
        List<Integer> years = data.stream()
            .map(HealthData::getJahr)
            .distinct()
            .sorted()
            .collect(Collectors.toList());     

        // Line Chart: Ausgaben by Ausgabenträger over years
        Map<String, Map<Integer, Integer>> ausgabenByTraegerAndJahr = data.stream()
            .filter(d -> !"Insgesamt".equals(d.getAusgabentraeger())) // Explicitly exclude total
            .collect(Collectors.groupingBy(
                HealthData::getAusgabentraeger,
                Collectors.groupingBy(
                    HealthData::getJahr,
                    Collectors.summingInt(HealthData::getAusgaben)
                )
            ));

        // Debug: Check if "Gesetzliche Krankenversicherung" is present
        System.out.println("Ausgaben by Traeger and Jahr: " + ausgabenByTraegerAndJahr);

        // Prepare series data for each Ausgabenträger
        List<Map<String, Object>> lineChartData = ausgabenByTraegerAndJahr.entrySet().stream()
        .map(entry -> {
            String traeger = entry.getKey();
            Map<Integer, Integer> yearlyData = entry.getValue();
            Map<String, Object> seriesData = new HashMap<>();
            seriesData.put("name", traeger);
            // Ensure data aligns with years list, filling missing years with 0
            List<Integer> dataForYears = years.stream()
                .map(year -> yearlyData.getOrDefault(year, 0))
                .collect(Collectors.toList());
            seriesData.put("data", dataForYears);
            // Debug: Log data for "Gesetzliche Krankenversicherung"
            if ("Gesetzliche Krankenversicherung".equals(traeger)) {
                System.out.println("Data for Gesetzliche Krankenversicherung: " + dataForYears);
            }
            return seriesData;
        })
        .collect(Collectors.toList());

        model.addAttribute("lineChartData", lineChartData);
        model.addAttribute("barChartData", barChartData);
        model.addAttribute("pieChartData", ausgabenByJahrAndTraeger);
        model.addAttribute("years", years);

        return "index";
    }

    public record ChartData(int jahr, int value) {}

}