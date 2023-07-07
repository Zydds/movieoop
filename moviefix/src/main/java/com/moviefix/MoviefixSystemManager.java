package com.moviefix;

import java.sql.SQLException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;



public class MoviefixSystemManager {
    private final Connection connection;

    public MoviefixSystemManager(Connection connection) {
        this.connection = connection;
    }

    public void addMovie(String movieName, String movieGenre, String movieDuration) {
        try {
            String query = "INSERT INTO moviedb (movieName, movieGenre, movieDuration) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, movieName);
            statement.setString(2, movieGenre);
            statement.setString(3, movieDuration);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Movie added successfully!");
            } else {
                System.out.println("Failed to add movie.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean removeMovie(String movieName) {
        try {
            String query = "DELETE FROM moviedb WHERE movieName = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, movieName);

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<String> getMovieList() {
        List<String> movieList = new ArrayList<>();
        try {
            String query = "SELECT * FROM moviedb";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String movieName = resultSet.getString("movieName");
                String movieGenre = resultSet.getString("movieGenre");
                String movieDuration = resultSet.getString("movieDuration");
                movieList.add(movieName + "," + movieGenre + "," + movieDuration);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return movieList;
    }

    public void clearMovieList() {
        try {
            String query = "DELETE FROM moviedb";
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String getMovieName(String movie) {
        String[] movieDetails = movie.split(",");
        if (movieDetails.length >= 1) {
            return movieDetails[0];
        }
        return "";
    }
}
