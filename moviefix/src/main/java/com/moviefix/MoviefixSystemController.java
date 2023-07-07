package com.moviefix;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class MoviefixSystemController {
    private final MoviefixSystemView view;
    private final MoviefixSystemManager manager;

    private int idMovie = -1;

    public MoviefixSystemController(MoviefixSystemView view) {
        this.view = view;
        this.manager = new MoviefixSystemManager();
    }

    public void addMovie(String movieName, String movieGenre, String movieDuration) {
        try {
            Connection connection = manager.getConnection();
            String query = "INSERT INTO moviedb (movieName, movieGenre, movieDuration) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, movieName);
            statement.setString(2, movieGenre);
            statement.setString(3, movieDuration);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    idMovie = generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            view.showErrorAlert("Failed to add movie. Please try again.");
        }
    }

    public int getIdMovie() {
        return idMovie;
    }

    public void removeMovie() {
        String selectedMovie = view.getSelectedMovie();
        if (selectedMovie == null) {
            view.showErrorAlert("Please select a movie to remove.");
            return;
        }

        boolean removed = manager.removeMovie(selectedMovie);
        if (removed) {
            view.showInformationAlert("Movie removed successfully!");
            updateMovieList();
            view.clearForm();
        } else {
            view.showErrorAlert("Failed to remove movie. Please try again.");
        }
    }

    private String getMovieName(String movie) {
        String[] movieDetails = movie.split(",");
        if (movieDetails.length >= 1) {
            return movieDetails[0];
        }
        return "";
    }

    public void updateMovieList() {
        List<String> movieList = manager.getMovieList();
        view.setMovieList(movieList);
    }

    public void clearMovieList() {
        manager.clearMovieList();
        view.setMovieList(null);
    }

    public void closeApplication() {
        manager.close();
    }
}
