package com.example.thymeleafproject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

@Controller
public class WeatherController {
    private final String apiKey = "f0215c750224bad39b030e666f1236bb";
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/weather")
    public String getWeather(@RequestParam String city, Model model) {
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&APPID=" + apiKey;
        WeatherData weatherData = restTemplate.getForObject(url, WeatherData.class);

        double temperatureInCelsius = weatherData.getMain().getTemp() - 273.15; // Przelicz na stopnie Celsiusza z Kelwina
        double roundedTemperature = Math.round(temperatureInCelsius * 10.0) / 10.0; // Zaokrąglij do jednego miejsca po przecinku

        String weatherType = weatherData.getWeather().get(0).getMain();
        model.addAttribute("weatherType", weatherType);


        model.addAttribute("city", city);
        model.addAttribute("temperature", roundedTemperature); // Przelicz na stopnie Celsiusza
        model.addAttribute("windSpeed", weatherData.getWind().getSpeed());

        return "weather";
    }
}
