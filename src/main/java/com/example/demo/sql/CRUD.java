package com.example.demo.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.example.demo.h2utils.H2JDBCUtils;
import com.example.demo.model.User_movie;

public class CRUD {

	private static final String INSERT_USERS_SQL = "INSERT INTO user_movie"
			+ "  (userid , movieid , favorite , personal_rating , notes ) VALUES " + " (?, ?, ?, ?, ?);";
	private static final String UPDATE_USERS_SQL = "UPDATE user_movie SET favorite = ?, personal_rating = ?, notes = ? WHERE userid = ? AND movieid = ?;";
	private static final String QUERY = "select userid , movieid , favorite , personal_rating , notes from user_movie where userid =? and movieid=?";

	public static User_movie getUser_movieByID(int id_movie, int user_id) throws SQLException {
		System.out.println(QUERY);
		// Step 1: Establishing a Connection
		try (Connection connection = H2JDBCUtils.getConnection();

				// Step 2:Create a statement using connection object
				PreparedStatement preparedStatement = connection.prepareStatement(QUERY);) {
			preparedStatement.setInt(1, user_id);
			preparedStatement.setInt(2, id_movie);
			System.out.println(preparedStatement);
			// Step 3: Execute the query or update query
			ResultSet rs = preparedStatement.executeQuery();
			if (!rs.next()) {
				return null;
			} else {
				User_movie user_movie = new User_movie();
				int userid = rs.getInt("userid");
				int movieid = rs.getInt("movieid");
				boolean favorite = rs.getBoolean("favorite");
				int personal_rating = rs.getInt("personal_rating");
				String notes = rs.getString("notes");

				System.out.println(userid + "," + movieid + "," + favorite + "," + personal_rating + "," + notes);
				user_movie.setFavourite(favorite);
				user_movie.setNotes(notes);
				user_movie.setPersonal_rating(personal_rating);
				return user_movie;
			}
			// Step 4: Process the ResultSet object.

		} catch (SQLException e) {
			System.out.println("aa");
		}
		return null;

		// Step 4: try-with-resource statement will auto close the connection.
	}

	public static ResponseEntity<String> inicializador(String url) {

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(
				"eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI4YmY1YzQ0YzczMGY4ODBhYWZjNjYwMjAzODFlMjJkNyIsInN1YiI6IjYzMTVhZGU3NTUwN2U5MDA4MmIyZjFiOCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.XI-AStf_R2eUyj8MWXcYpR2c6d-kdFQAq5ZkL6y8rrE");

		HttpEntity request = new HttpEntity(headers);
		ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
		return responseEntity;
	}

	public static void insertRecord(User_movie user_movie, int id_movie) throws SQLException {
		// Step 1: Establishing a Connection
		try (Connection connection = H2JDBCUtils.getConnection();
				// Step 2:Create a statement using connection object
				PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USERS_SQL)) {

			preparedStatement.setInt(1, user_movie.getUserid());
			preparedStatement.setInt(2, id_movie);
			preparedStatement.setBoolean(3, user_movie.isFavourite());
			preparedStatement.setInt(4, user_movie.getPersonal_rating());
			preparedStatement.setString(5, user_movie.getNotes());
			// Step 3: Execute the query or update query
			preparedStatement.executeUpdate();
		} catch (SQLException e) {

		}

	}

	public static int retrieveUserId(String userName) throws SQLException {
		String consulta = "select userid from users where username = '" + userName + "'";
		try (Connection connection = H2JDBCUtils.getConnection(); Statement statement = connection.createStatement()) {

			ResultSet rs = statement.executeQuery(consulta);

			if (rs.next()) {
				return rs.getInt("userid");
			}
			return 0;
		}
	}
	
	public static void updateRecord(User_movie user_movie, int id_movie) throws SQLException {
        try (Connection connection = H2JDBCUtils.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_USERS_SQL)) {
            //"UPDATE user_movie SET favorite = ?, personal_rating = ?, notes = ? WHERE userid = ? AND movieid = ?;";
            preparedStatement.setInt(1,(user_movie.isFavourite()?1:0));
            preparedStatement.setInt(2, user_movie.getPersonal_rating());
            preparedStatement.setString(3, user_movie.getNotes());
            preparedStatement.setInt(4, user_movie.getUserid());
            preparedStatement.setInt(5, id_movie);
            
            preparedStatement.executeUpdate();
 }
 }
}
