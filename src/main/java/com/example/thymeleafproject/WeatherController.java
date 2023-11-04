package com.example.thymeleafproject;

import com.example.thymeleafproject.readAPI.KeyReader;
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

           return "weather";
       }catch (HttpClientErrorException e){
           return "cityNotFound";
       }catch (RestClientException e){
           return "apiError";
       }
    }
}
