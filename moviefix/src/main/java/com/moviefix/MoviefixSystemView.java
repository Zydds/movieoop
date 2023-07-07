package com.moviefix;
import java.util.List;

public interface MoviefixSystemView {
    String getMovieName();

    String getMovieGenre();

    String getMovieDuration();

    String getSelectedMovie();

    void setMovieList(List<String> movieList);

    void setGenreOptions(List<String> genreOptions);

    void clearForm();

    void showErrorAlert(String message);

    void showInformationAlert(String message);

    void closeApplication();
}
