package com.example.demo.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import com.example.demo.h2utils.H2JDBCUtils;
import com.example.demo.model.User_movie;
import com.example.demo.sql.CRUD;


@RestController()
@RequestMapping("/movie")
public class DemoController {

	private static final String INSERT_USERS_SQL = "INSERT INTO user_movie"
			+ "  (userid , movieid , favorite , personal_rating , notes ) VALUES " + " (?, ?, ?, ?, ?);";
	private static final String UPDATE_USERS_SQL = "UPDATE user_movie SET favorite = ?, personal_rating = ?, notes = ? WHERE userid = ? AND movieid = ?;";

	// COLECTIVE

	@GetMapping("/topRated")
	public ResponseEntity<String> getTopRated() {
		String url = "https://api.themoviedb.org/3/movie/top_rated?language=es-ES";
		return CRUD.inicializador(url);
	}

	@GetMapping("/topPopular")
	public ResponseEntity<String> getTopPopular() {
		String url = "https://api.themoviedb.org/3/movie/top_popular?language=es-ES";
		return CRUD.inicializador(url);
	}

	// INDIVIDUALS

//	@GetMapping("/{id}")
//	public ResponseEntity<String> getFilm(@PathVariable int id) {
//		String url = "https://api.themoviedb.org/3/movie/" + id + "?language=es-ES";
//		return CRUD.inicializador(url);
//	}
	
	@GetMapping("/{movie_id}")
    public ResponseEntity<JsonObject> getMovie(@PathVariable String movie_id) throws SQLException {
        String urlString="https://api.themoviedb.org/3/movie/"+movie_id+"?language=es-ES";
        
        ResponseEntity<String> responseEntity = CRUD.inicializador(urlString);
        String jsonResponse = responseEntity.getBody();
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
        String currentUserName = authentication.getName();
        int userId = CRUD.retrieveUserId(currentUserName);
        User_movie userMovie = CRUD.getUser_movieByID(Integer.parseInt(movie_id), userId);
        
        if(userMovie == null) {
             String json = jsonResponse.substring(0, jsonResponse.length()-1).concat(",\"favourite\": \"false\", \"personal_rating\":\"null\", \"notes\": \"null\"}");
        
            JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
            
            return responseEntity.ok(jsonObject);
        }
        else {
            String json = jsonResponse.substring(0, jsonResponse.length()-1).concat(",\"favourite\": \"" + userMovie.isFavourite() + "\", \"personal_rating\":\"" + userMovie.getPersonal_rating() + "\", \"notes\": \"" + userMovie.getNotes() + "\"}");
            
            JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
            return responseEntity.ok(jsonObject);
        }
        
         }
        return responseEntity.ok(new JsonParser().parse("{}").getAsJsonObject());
    }

	@GetMapping("/{id}/credits")
	public ResponseEntity<String> getFilmCredits(@PathVariable int id) {
		String url = "https://api.themoviedb.org/3/movie/" + id + "/credits?language=es-ES";
		return CRUD.inicializador(url);
	}

	@GetMapping("/{id}/images")
	public ResponseEntity<String> getFilmImages(@PathVariable int id) {
		String url = "https://api.themoviedb.org/3/movie/" + id + "/images";
		return CRUD.inicializador(url);
	}

	@GetMapping("/{id}/keywords")
	public ResponseEntity<String> getFilmKeywords(@PathVariable int id) {
		String url = "https://api.themoviedb.org/3/movie/" + id + "/keywords";
		return CRUD.inicializador(url);
	}

	@GetMapping("/{id}/recommendations")
	public ResponseEntity<String> getFilmRecommendations(@PathVariable int id) {
		String url = "https://api.themoviedb.org/3/movie/" + id + "/recommendations";
		return CRUD.inicializador(url);
	}

	@GetMapping("/{id}/similar")
	public ResponseEntity<String> getFilmSimilar(@PathVariable int id) {
		String url = "https://api.themoviedb.org/3/movie/" + id + "/similar";
		return CRUD.inicializador(url);
	}

	// Patch
	@PatchMapping("/{movie_id}")
	public ResponseEntity<String> postMovie(@PathVariable int movie_id, @RequestBody User_movie user_movie)
			throws SQLException {
		String urlString = "https://api.themoviedb.org/3/movie/" + movie_id + "?language=es-ES";
		// Recuperacion del nombre de usuario
		String currentUserName = "";

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			// hay autentificación
			currentUserName = authentication.getName();
			System.out.println(currentUserName);
			int userId = CRUD.retrieveUserId(currentUserName);
			user_movie.setUserid(userId);
			 try {
	                
	                User_movie user = CRUD.getUser_movieByID(movie_id, userId);
	                
	                if(user == null) {
	                    CRUD.insertRecord(user_movie,movie_id);
	                }
	                else {
	                    CRUD.updateRecord(user_movie,movie_id);
	                }               
	   
	            } catch (SQLException e) {
	                
	                e.printStackTrace();
	            }
		}

		return CRUD.inicializador(urlString);
	}
	

	// General Functions
//	public ResponseEntity<String> inicializador(String url){
//		
//		RestTemplate restTemplate = new RestTemplate();
//		HttpHeaders headers = new HttpHeaders();
//		headers.setBearerAuth("eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI4YmY1YzQ0YzczMGY4ODBhYWZjNjYwMjAzODFlMjJkNyIsInN1YiI6IjYzMTVhZGU3NTUwN2U5MDA4MmIyZjFiOCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.XI-AStf_R2eUyj8MWXcYpR2c6d-kdFQAq5ZkL6y8rrE");
//		
//		HttpEntity request = new HttpEntity(headers);
//		ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET,request,String.class);
//		return responseEntity;
//	}
//	
//	
//	public void insertRecord(User_movie user_movie,int id_movie) throws SQLException {
//        // Step 1: Establishing a Connection
//        try (Connection connection = H2JDBCUtils.getConnection();
//            // Step 2:Create a statement using connection object
//            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USERS_SQL)) {
//            preparedStatement.setInt(1, user_movie.getUserid() );
//            preparedStatement.setInt(2,id_movie);
//            preparedStatement.setBoolean(3, user_movie.isFavourite());
//            preparedStatement.setInt(4, user_movie.getPersonal_rating());
//            preparedStatement.setString(5, user_movie.getNotes());
//            // Step 3: Execute the query or update query
//            preparedStatement.executeUpdate();
//        } catch (SQLException e) {
//
//        }
//
//    }
//	
//	 public int retrieveUserId(String userName) throws SQLException {
//	        String consulta = "select userid from users where username = '" + userName +"'";
//	        try(Connection connection = H2JDBCUtils.getConnection();
//	                Statement statement = connection.createStatement()){
//	            
//	            ResultSet rs = statement.executeQuery(consulta);
//	            
//	            if(rs.next()) {
//	                return rs.getInt("userid");
//	            }
//	        return 0;
//	        }
//	    }

}
