package com.example.demo.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class DemoController {
	
	// COLECTIVE
	
	
	@GetMapping("/topRated")
	public ResponseEntity<String> getTopRated(){
		String url = "https://api.themoviedb.org/3/movie/top_rated?language=es-ES";
		return inicializador(url);
	}
	
	@GetMapping("/topPopular")
	public ResponseEntity<String> getTopPopular(){
		String url = "https://api.themoviedb.org/3/movie/top_popular?language=es-ES";
		return inicializador(url);
	}
	
	
	//INDIVIDUALS
	
	@GetMapping("/{id}")
	public ResponseEntity<String> getFilm(@PathVariable int id){
		String url = "https://api.themoviedb.org/3/movie/"+id+"&language=es-ES";
		return inicializador(url);
	}
	
	@GetMapping("/{id}/credits")
	public ResponseEntity<String> getFilmCredits(@PathVariable int id){
		String url = "https://api.themoviedb.org/3/movie/"+id+"/credits&language=es-ES";
		return inicializador(url);
	}
	
	@GetMapping("/{id}/images")
	public ResponseEntity<String> getFilmImages(@PathVariable int id){
		String url = "https://api.themoviedb.org/3/movie/"+id+"/images";
		return inicializador(url);
	}
	
	@GetMapping("/{id}/keywords")
	public ResponseEntity<String> getFilmKeywords(@PathVariable int id){
		String url = "https://api.themoviedb.org/3/movie/"+id+"/keywords";
		return inicializador(url);
	}
	
	@GetMapping("/{id}/recommendations")
	public ResponseEntity<String> getFilmRecommendations(@PathVariable int id){
		String url = "https://api.themoviedb.org/3/movie/"+id+"/recommendations";
		return inicializador(url);
	}
	
	@GetMapping("/{id}/similar")
	public ResponseEntity<String> getFilmSimilar(@PathVariable int id){
		String url = "https://api.themoviedb.org/3/movie/"+id+"/similar";
		return inicializador(url);
	}
	
	
	//	General Functions
	public ResponseEntity<String> inicializador(String url){
		
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth("eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI4YmY1YzQ0YzczMGY4ODBhYWZjNjYwMjAzODFlMjJkNyIsInN1YiI6IjYzMTVhZGU3NTUwN2U5MDA4MmIyZjFiOCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.XI-AStf_R2eUyj8MWXcYpR2c6d-kdFQAq5ZkL6y8rrE");
		
		HttpEntity request = new HttpEntity(headers);
		ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET,request,String.class);
		
		return responseEntity;
	}
}
