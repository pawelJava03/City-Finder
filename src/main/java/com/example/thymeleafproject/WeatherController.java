package com.example.thymeleafproject;

import com.example.thymeleafproject.readAPI.KeyReader;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Controller
public class WeatherController {
    private final String apiKey = KeyReader.getKey();
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/weather")
    public String getWeather(@RequestParam(name = "city", required = false, defaultValue = "Warsaw") String city, Model model) {
        try {
            String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&APPID=" + apiKey;
            WeatherData weatherData = restTemplate.getForObject(url, WeatherData.class);

            double temperatureInCelsius = weatherData.getMain().getTemp() - 273.15;
            double roundedTemperature = Math.round(temperatureInCelsius * 10.0) / 10.0;

            String weatherType = weatherData.getWeather().get(0).getMain();
            model.addAttribute("weatherType", weatherType);

            model.addAttribute("city", city);
            model.addAttribute("temperature", roundedTemperature);
            model.addAttribute("windSpeed", weatherData.getWind().getSpeed());

            String apiUrl = "https://en.wikipedia.org/w/api.php?action=query&format=json&prop=extracts&exintro=true&titles="+city;
            String response = restTemplate.getForObject(apiUrl, String.class);

            // Parsuj odpowiedź JSON
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response);

            // Pobierz opis Warszawy
            JsonNode pages = jsonNode.path("query").path("pages");
            String firstPageKey = pages.fieldNames().next();
            String cityDescription = pages.path(firstPageKey).path("extract").asText();

            // Usuń znaczniki HTML i formatuj tekst
            Document doc = Jsoup.parse(cityDescription);
            String plainText = doc.text();

            // Dodaj sformatowany tekst do modelu
            model.addAttribute("cityInfo", plainText);

            return "weather";
        }catch (HttpClientErrorException e){
            return "cityNotFound";
        }catch (RestClientException e){
            return "apiError";
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}