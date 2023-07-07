package com.moviefix;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import javafx.stage.Stage;

import java.sql.Connection;
import java.util.List;

public class MoviefixSystemGUI extends Application implements MoviefixSystemView {
    
    public MoviefixSystemGUI() {

        connection = null;
    }

    private TextField textFieldMovieName;
    private TextField textFieldMovieGenre;
    private TextField textFieldMovieDuration;
    private ListView<String> listViewMovie;

    private MoviefixSystemController controller;
    private final Connection connection;

    public static void main(String[] args) {
        launch(args);
    }

    public MoviefixSystemGUI(Connection connection) {
        this.connection = connection;
    }



    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Movie List Management");

        // Create an instance of the view interface
        MoviefixSystemView view = new MoviefixSystemGUI(connection);

        // Pass the view interface and the connection to the controller
        controller = new MoviefixSystemController(view);

        GridPane formPane = createFormPane();
        VBox listPane = createMovieListPane();

        BorderPane rootPane = new BorderPane();
        rootPane.setTop(formPane);
        rootPane.setCenter(listPane);
        rootPane.setPadding(new Insets(10));

        Scene scene = new Scene(rootPane, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setOnCloseRequest(event -> controller.closeApplication());
        primaryStage.show();
    }


    private GridPane createFormPane() {
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.TOP_LEFT);
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        Label labelMovieName = new Label("Movie Name:");
        textFieldMovieName = new TextField();

        Label labelMovieGenre = new Label("Genre:");
        textFieldMovieGenre = new TextField();

        Label labelMovieDuration = new Label("Duration:");

        // Create HBox for hours
        HBox hboxHours = new HBox();
        Label labelHours = new Label("hour");
        ChoiceBox<Integer> choiceBoxHours = new ChoiceBox<>();
        choiceBoxHours.getItems().addAll(0, 1, 2, 3); // Set the available hours options
        choiceBoxHours.setValue(0); // Set default selection
        hboxHours.getChildren().addAll(choiceBoxHours, labelHours);

        // Create HBox for minutes
        HBox hboxMinutes = new HBox();
        Label labelMinutes = new Label("minutes");
        ChoiceBox<Integer> choiceBoxMinutes = new ChoiceBox<>();
        for (int i = 0; i < 60; i++) {
            choiceBoxMinutes.getItems().add(i); // Add minutes from 0 to 59
        }
        choiceBoxMinutes.setValue(0); // Set default selection
        hboxMinutes.getChildren().addAll(choiceBoxMinutes, labelMinutes);

        Button buttonAdd = new Button("Add");
        buttonAdd.setStyle("-fx-background-color: #61afff; -fx-text-fill: white;");
        buttonAdd.setOnAction(event -> {
            String movieName = textFieldMovieName.getText();
            String movieGenre = textFieldMovieGenre.getText();
            int hours = choiceBoxHours.getValue();
            int minutes = choiceBoxMinutes.getValue();
            String movieDuration = String.format("%02d:%02d", hours, minutes);

            if (movieName.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Movie name field is empty");
                alert.setContentText("Please enter a movie name.");
                alert.showAndWait();
            } else {
                controller.addMovie(movieName, movieGenre, movieDuration);
            }
        });
        Button buttonRemove = new Button("Remove");
        buttonRemove.setOnAction(event -> controller.removeMovie());
        buttonRemove.setDisable(true);

        gridPane.add(labelMovieName, 0, 0);
        gridPane.add(textFieldMovieName, 1, 0);
        gridPane.add(labelMovieGenre, 0, 1);
        gridPane.add(textFieldMovieGenre, 1, 1);
        gridPane.add(labelMovieDuration, 0, 2);
        gridPane.add(hboxHours, 1, 2);
        gridPane.add(hboxMinutes, 2, 2);
        gridPane.add(buttonAdd, 0, 3);
        gridPane.add(buttonRemove, 1, 3);


        return gridPane;
    }

    private VBox createMovieListPane() {
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10));
        vbox.setAlignment(Pos.TOP_LEFT);
        vbox.setSpacing(10);

        Label labelMovieList = new Label("Movie List");

        listViewMovie = new ListView<>();
        listViewMovie.setPrefHeight(200);
        listViewMovie.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(String movie, boolean empty) {
                super.updateItem(movie, empty);
                if (empty || movie == null) {
                    setText(null);
                } else {
                    String[] movieDetails = movie.split(",");
                    if (movieDetails.length >= 4) {
                        VBox vbox = new VBox();
                        vbox.setSpacing(5);

                        Label nameLabel = new Label("Name: " + movieDetails[1]);
                        Label genreLabel = new Label("Genre: " + movieDetails[2]);
                        Label durationLabel = new Label("Duration: " + movieDetails[3]);

                        vbox.getChildren().addAll(nameLabel, genreLabel, durationLabel);
                        setGraphic(vbox);
                    } else {
                        setText(null);
                    }
                }
            }
        });

        vbox.getChildren().addAll(labelMovieList, listViewMovie);

        return vbox;
    }


    @Override
    public String getMovieName() {
        return textFieldMovieName.getText();
    }

    @Override
    public String getMovieGenre() {
        return textFieldMovieGenre.getText();
    }

    @Override
    public String getMovieDuration() {
        return textFieldMovieDuration.getText();
    }

    @Override
    public String getSelectedMovie() {
        return listViewMovie.getSelectionModel().getSelectedItem();
    }

    @Override
    public void setMovieList(List<String> movieList) {
        listViewMovie.setItems(FXCollections.observableArrayList(movieList));
    }

    @Override
    public void setGenreOptions(List<String> genreOptions) {
        // Not implemented for this example
    }

    @Override
    public void clearForm() {
        textFieldMovieName.clear();
        textFieldMovieGenre.clear();
        textFieldMovieDuration.clear();
    }

    @Override
    public void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void showInformationAlert(String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void closeApplication() {
        controller.closeApplication();
    }

}
