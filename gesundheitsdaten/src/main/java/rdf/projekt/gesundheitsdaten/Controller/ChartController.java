package rdf.projekt.gesundheitsdaten.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import rdf.projekt.gesundheitsdaten.Data.HealthData;
import rdf.projekt.gesundheitsdaten.Repository.HealthDataRepository;

@Controller
public class ChartController {

    @Autowired
    private HealthDataRepository repository;

    @GetMapping("/")
    public String index(Model model) {
        return updateModelWithData(model); // Initial page load
    }

    @GetMapping("/data")
    @ResponseBody
    public Map<String, Object> getChartData() {
        List<HealthData> data = repository.findAll();

        // Bar Chart: Total Ausgaben by Jahr
        Map<Integer, Integer> ausgabenByJahr = data.stream()
            .filter(d -> !"Insgesamt".equals(d.getAusgabentraeger()))
            .collect(Collectors.groupingBy(
                HealthData::getJahr,
                Collectors.summingInt(HealthData::getAusgaben)
            ));
        List<ChartData> barChartData = ausgabenByJahr.entrySet().stream()
            .map(entry -> new ChartData(entry.getKey(), entry.getValue()))
            .sorted((a, b) -> Integer.compare(a.jahr, b.jahr))
            .collect(Collectors.toList());

        // Pie Chart: Ausgaben by Ausgabentraeger for each Jahr
        Map<Integer, Map<String, Integer>> ausgabenByJahrAndTraeger = data.stream()
            .filter(d -> !"Insgesamt".equals(d.getAusgabentraeger()))
            .collect(Collectors.groupingBy(
                HealthData::getJahr,
                Collectors.groupingBy(
                    HealthData::getAusgabentraeger,
                    Collectors.summingInt(HealthData::getAusgaben)
                )
            ));

        // Unique years
        List<Integer> years = data.stream()
            .map(HealthData::getJahr)
            .distinct()
            .sorted()
            .collect(Collectors.toList());

        // Line Chart: Ausgaben by Ausgabenträger over years
        Map<String, Map<Integer, Integer>> ausgabenByTraegerAndJahr = data.stream()
            .filter(d -> !"Insgesamt".equals(d.getAusgabentraeger()))
            .collect(Collectors.groupingBy(
                HealthData::getAusgabentraeger,
                Collectors.groupingBy(
                    HealthData::getJahr,
                    Collectors.summingInt(HealthData::getAusgaben)
                )
            ));

        List<Map<String, Object>> lineChartData = ausgabenByTraegerAndJahr.entrySet().stream()
            .map(entry -> {
                String traeger = entry.getKey();
                Map<Integer, Integer> yearlyData = entry.getValue();
                Map<String, Object> seriesData = new HashMap<>();
                seriesData.put("name", traeger);
                List<Integer> dataForYears = years.stream()
                    .map(year -> yearlyData.getOrDefault(year, 0))
                    .collect(Collectors.toList());
                seriesData.put("data", dataForYears);
                return seriesData;
            })
            .collect(Collectors.toList());

        // Return data as JSON
        Map<String, Object> response = new HashMap<>();
        response.put("barChartData", barChartData);
        response.put("pieChartData", ausgabenByJahrAndTraeger);
        response.put("lineChartData", lineChartData);
        response.put("years", years);
        return response;
    }

    // Helper method for initial page load
    private String updateModelWithData(Model model) {
        List<HealthData> data = repository.findAll();

        Map<Integer, Integer> ausgabenByJahr = data.stream()
            .filter(d -> !"Insgesamt".equals(d.getAusgabentraeger()))
            .collect(Collectors.groupingBy(
                HealthData::getJahr,
                Collectors.summingInt(HealthData::getAusgaben)
            ));
        List<ChartData> barChartData = ausgabenByJahr.entrySet().stream()
            .map(entry -> new ChartData(entry.getKey(), entry.getValue()))
            .sorted((a, b) -> Integer.compare(a.jahr, b.jahr))
            .collect(Collectors.toList());

        Map<Integer, Map<String, Integer>> ausgabenByJahrAndTraeger = data.stream()
            .filter(d -> !"Insgesamt".equals(d.getAusgabentraeger()))
            .collect(Collectors.groupingBy(
                HealthData::getJahr,
                Collectors.groupingBy(
                    HealthData::getAusgabentraeger,
                    Collectors.summingInt(HealthData::getAusgaben)
                )
            ));

        List<Integer> years = data.stream()
            .map(HealthData::getJahr)
            .distinct()
            .sorted()
            .collect(Collectors.toList());

        Map<String, Map<Integer, Integer>> ausgabenByTraegerAndJahr = data.stream()
            .filter(d -> !"Insgesamt".equals(d.getAusgabentraeger()))
            .collect(Collectors.groupingBy(
                HealthData::getAusgabentraeger,
                Collectors.groupingBy(
                    HealthData::getJahr,
                    Collectors.summingInt(HealthData::getAusgaben)
                )
            ));

        List<Map<String, Object>> lineChartData = ausgabenByTraegerAndJahr.entrySet().stream()
            .map(entry -> {
                String traeger = entry.getKey();
                Map<Integer, Integer> yearlyData = entry.getValue();
                Map<String, Object> seriesData = new HashMap<>();
                seriesData.put("name", traeger);
                List<Integer> dataForYears = years.stream()
                    .map(year -> yearlyData.getOrDefault(year, 0))
                    .collect(Collectors.toList());
                seriesData.put("data", dataForYears);
                return seriesData;
            })
            .collect(Collectors.toList());

        model.addAttribute("barChartData", barChartData);
        model.addAttribute("pieChartData", ausgabenByJahrAndTraeger);
        model.addAttribute("lineChartData", lineChartData);
        model.addAttribute("years", years);

        return "index";
    }

    public record ChartData(int jahr, int value) {}
}