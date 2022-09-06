package com.example.demo.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.demo.h2utils.H2JDBCUtils;
import com.example.demo.model.User_movie;

@RestController
public class DemoController {
	
	private static final String INSERT_USERS_SQL = "INSERT INTO user_movie" +
            "  (userid , movieid , favorite , personal_rating , notes ) VALUES " +
            " (?, ?, ?, ?, ?);";
	
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
		String url = "https://api.themoviedb.org/3/movie/"+id+"?language=es-ES";
		return inicializador(url);
	}
	
	@GetMapping("/{id}/credits")
	public ResponseEntity<String> getFilmCredits(@PathVariable int id){
		String url = "https://api.themoviedb.org/3/movie/"+id+"/credits?language=es-ES";
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
	
	
	// Path
	@PatchMapping("/{movie_id}")
    public ResponseEntity<String> postMovie(@PathVariable int movie_id, @RequestBody User_movie user_movie) {
        String urlString="https://api.themoviedb.org/3/movie/"+movie_id+"?language=es-ES";    
        
        
        try {
            insertRecord(user_movie,movie_id);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return inicializador(urlString);
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
	
	
	public void insertRecord(User_movie user_movie,int id_movie) throws SQLException {
		
        System.out.println(INSERT_USERS_SQL);
        // Step 1: Establishing a Connection
        try (Connection connection = H2JDBCUtils.getConnection();
            // Step 2:Create a statement using connection object
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USERS_SQL)) {
            preparedStatement.setInt(1, 1 );
            preparedStatement.setInt(2,id_movie);
            preparedStatement.setBoolean(3, user_movie.isFavourite());
            preparedStatement.setInt(4, user_movie.getPersonal_rating());
            preparedStatement.setString(5, user_movie.getNotes());



           System.out.println(preparedStatement);
            // Step 3: Execute the query or update query
            preparedStatement.executeUpdate();
        } catch (SQLException e) {

        }

    }
	
}
