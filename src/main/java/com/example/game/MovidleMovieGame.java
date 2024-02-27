package com.example.game;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class MovidleMovieGame extends Application {

    private static final int TITLE_TILE_SIZE_WIDHT = 200;
    private static final int TILE_SIZE_WIDHT = 100;
    private static final int TILE_SIZE_HEIGTH = 50;
    private static final int GRID_SIZE = 6;
    private static final int WINDOW_WIDTH = 1300;
    private static final int WINDOW_HEIGHT = 600;

    private static final int GUESS_FIELD_WIDTH = 250;
    private static final int GUESS_FIELD_HEIGHT = 30;
    private int remainingGuesses;
    public static int score;
    public static int scoreTemp;
    private Timer timer;
    private List<Movie> movies;
    private List<Movie> moviesTemp;
    private Movie currentMovie;
    private List<Tile> tiles;

    private TextField guessField;
    private Button guessButton;
    private GridPane gridPane;
    private GridPane gridPaneLeft;
    private VBox root;
    private Scene scene;
    private BorderPane borderPane;
    private HBox topBox;
    private Pane leftPane;
    private VBox leftBox;
    private Label labelScore;
    private Label labelRemaning;
    private Pane rightPane;
    private GridPane gridPaneMovieRectangles;
    private List<Movie> searchResults;
    private ImageView movieImage;
    private boolean flagGameOver ;
    private Stage primaryStage;
    private Label scoreLabel;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage=primaryStage;
        searchResults = new ArrayList<>();
        remainingGuesses = 5;
        flagGameOver = false;

        if(scoreTemp==0) {
            score = 0;
        }
        else {
            score = scoreTemp;
        }

        // Load movies from CSV file
        loadMovies();

        // Select a random movie
        selectRandomMovie();

        // Create GUI
        createGUI(primaryStage);

        primaryStage.show();
    }

    private void loadMovies() {
        movies = new ArrayList<>();
        boolean flagRead = false;
        try (BufferedReader br = new BufferedReader(new FileReader("./movies.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(";");
                if (data.length >= 5) {

                    if (flagRead) {
                        String title = data[1].trim();
                        String year = (data[2]).trim();
                        String genre = data[3].trim();
                        String origin = data[4].trim();
                        String director = data[5].trim();
                        String star = data[6].trim();
                        movies.add(new Movie(title, year, genre, origin, director, star));
                    }
                }
                flagRead = true;
            }
            moviesTemp = movies;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void selectRandomMovie() {
        Random random = new Random();
        int index = random.nextInt(movies.size());
        currentMovie = movies.get(index);
    }

    private void setStyleGuessField() {
        // guessField oluşturma
        guessField = new TextField();
        guessField.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-background-radius: 30;"); // Örnek stil ayarları
        guessField.setMinHeight(50); // Yükseklik ayarı
        guessField.setPadding(new Insets(10));
        guessField.setMinWidth(400); // genişlik ayarı
    }

    private void setStyleGuessButton() {
        // guessButton oluşturma
        guessButton = new Button("Guess");
        guessButton.setStyle("-fx-background-color: #4CAF50; -fx-font-size: 15px; -fx-text-fill: white; -fx-font-weight: bold;-fx-background-radius: 100;"); // Örnek stil ayarları
        guessButton.setMinHeight(50); // Yükseklik ayarı
        guessButton.setMinWidth(50);
        guessButton.setOnAction(event -> handleGuess());

    }

    private void setStyleTopBox() {
        topBox = new HBox();
        topBox.setPadding(new Insets(10, 10, 20, 10));
        topBox.setStyle("-fx-background-color: #eadddd;");
        topBox.setSpacing(10);
        topBox.setAlignment(Pos.CENTER);

    }

    private void leftContent() {
        // Sol tarafı oluşturma (200x800)
        leftPane = new Pane();
        leftPane.setPrefSize(200, 800);
        leftPane.setStyle("-fx-background-color: #280404;");

        //left box
        leftBox = new VBox();
        leftBox.setSpacing(15);
        leftBox.setPadding(new Insets(10));
        leftBox.setAlignment(Pos.CENTER_LEFT);
        leftBox.setMaxWidth(200); // VBox'ın maksimum genişliği 200 olarak ayarlandı

        labelScore = new Label("Score :" + String.valueOf(score));
        labelRemaning = new Label("Remaning :" + String.valueOf(remainingGuesses));

        // Label stil ayarları
        labelScore.setStyle("-fx-font-size: 15px; -fx-text-fill: white; -fx-font-weight: bold;-fx-background-color: #b91414 ; -fx-background-radius: 5");
        labelScore.setPadding(new Insets(7));
        labelRemaning.setStyle("-fx-font-size: 15px; -fx-text-fill: #d3d5da; -fx-font-weight: bold;-fx-background-color: #b91414 ;-fx-background-radius: 5");
        labelRemaning.setPadding(new Insets(7));
    }

    private void scoreAndRemaningResult() {
        labelScore.setText("Score :" + String.valueOf(score));
        labelRemaning.setText("Remaning :" + String.valueOf(remainingGuesses));
    }

    private void rightContent() {
        //right 1000x800
        rightPane = new Pane();
        rightPane.setStyle("-fx-background-color: #eadddd;");
        rightPane.setPrefSize(1000, 800);

        //  kutuları eklemek için gridPane oluşturma
        gridPaneMovieRectangles = new GridPane();
        gridPaneMovieRectangles.setHgap(5); // Yatay boşluk ayarı
        gridPaneMovieRectangles.setVgap(10); // Dikey boşluk ayarı
        gridPaneMovieRectangles.setPadding(new Insets(2));
        gridPaneMovieRectangles.setAlignment(Pos.CENTER);

    }

    private void setSceneAndStage(Stage primaryStage) {
        scene = new Scene(borderPane, 1200, 800);
        primaryStage.setTitle("Movidle Movie Guessing Game");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
    }

    private void createGUI(Stage primaryStage) {

        // BorderPane oluşturma
        borderPane = new BorderPane();

        // Üst tarafta yer alacak HBox oluşturma
        setStyleTopBox();
        // guessField oluşturma
        setStyleGuessField();
        // guessButton oluşturma
        setStyleGuessButton();
        //add to topBox
        topBox.getChildren().addAll(guessField, guessButton);
        //add  topBox to borderPane
        borderPane.setTop(topBox);

        // Sol tarafı oluşturma (200x800)
        // VBox oluşturma
        // İki label oluşturma (score)
        leftContent();
        // Label'ları VBox'a ekleme
        leftBox.getChildren().addAll(labelScore, labelRemaning);
        // Sol pane'e VBox'ı ekleme
        leftPane.getChildren().add(leftBox);

        borderPane.setLeft(leftPane);

        // Sağ tarafı oluşturma (1000x800)
        //  kutuları eklemek için gridPane oluşturma
        rightContent();
        borderPane.setRight(rightPane);
        rightPane.getChildren().add(gridPaneMovieRectangles);
        // create movie Rectangle
        //createMovieRectangle();

        // Scene ve Stage oluşturma
        setSceneAndStage(primaryStage);
        primaryStage.show();
    }


    private void handleGuess() {

        String guess = guessField.getText().trim();
        if(guess.equals("")) return;
        for (Movie movie : movies) {
            if (movie.getTitle().equalsIgnoreCase(guess)) {
                searchResults.add(movie);
                checkMovie(movie);

                if(score > 0 & (score % 50)==0) {
                    remainingGuesses++;
                    showSeenThenDisappearMessage("You got +1 guess");

                }
            }
        }
        showSearchResultMovieList();
        if (guess.equalsIgnoreCase(currentMovie.getTitle())) {
            flagGameOver=true;
            showWinMessage(Alert.AlertType.INFORMATION, "Congratulations!", "You Win!",false);
            score+=50;

        } else {
            remainingGuesses--;
            scoreAndRemaningResult();
            if (checkRemainingGuesses()) return;
        }

        guessField.clear();
    }

    private void checkMovie(Movie movie) {
        if(Objects.equals(movie.getTitle(), currentMovie.getTitle())) increaseScore();
        if(Objects.equals(movie.getDirector(), currentMovie.getDirector())) increaseScore();
        if(Objects.equals(movie.getYear(), currentMovie.getYear())) increaseScore();
        if(Objects.equals(movie.getStar(), currentMovie.getStar())) increaseScore();
        if(Objects.equals(movie.getGenre(), currentMovie.getGenre())) increaseScore();
        if(Objects.equals(movie.getOrigin(), currentMovie.getOrigin())) increaseScore();
        scoreTemp= score;
    }

    private void increaseScore() {
        score+=10;SoundUtil.playFindMovieItemsSound();
    }
    private void showSeenThenDisappearMessage(String message) {

        // Mesaj kutusunun boyutlarını ve konumunu ayarlama
        double messageWidth = 300; // Mesaj kutusu genişliği
        double messageHeight = 200; // Mesaj kutusu yüksekliği

            // Mesaj kutusunu sol üst köşeye konumlandırma
        double screenWidth = Screen.getPrimary().getVisualBounds().getWidth(); // Ekran genişliği
        double screenHeight = Screen.getPrimary().getVisualBounds().getHeight(); // Ekran yüksekliği
        double messageX = 40; // Mesaj kutusu X koordinatı
        double messageY = 30; // Mesaj kutusu Y koordinatı

        Stage messageStage = new Stage();
        messageStage.setWidth(messageWidth);
        messageStage.setHeight(messageHeight);
        messageStage.setX(primaryStage.getX() -messageX);
        messageStage.setY(primaryStage.getY()-messageY);
        messageStage.initOwner(getStage());
        messageStage.initModality(Modality.WINDOW_MODAL);
        messageStage.initStyle(StageStyle.TRANSPARENT); // Ekran çerçevesini kaldırma
        messageStage.setResizable(false);

        Label messageLabel = new Label(message);
        messageLabel.setStyle("-fx-font-size: 20px;-fx-font-color:#d30303 ; -fx-font-weight: bold; -fx-effect: dropshadow( gaussian , rgb(255,214,6) , 10,0,0,1 );");

        StackPane messageRoot = new StackPane();
        messageRoot.setPadding(new Insets(10));
        messageRoot.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
        messageRoot.getChildren().add(messageLabel);

        Scene messageScene = new Scene(messageRoot);
        messageScene.setFill(Color.TRANSPARENT);
        messageStage.setScene(messageScene);
        messageStage.show();
        //play sound
        SoundUtil.playGotGuessSound();
        PauseTransition delay = new PauseTransition(Duration.millis(1000));
        delay.setOnFinished(event -> messageStage.close());
        delay.play();
    }

    private Stage getStage() {
        return (Stage) primaryStage.getScene().getWindow();
    }


    private boolean checkRemainingGuesses() {
        if (remainingGuesses == 0) {
            showWinMessage(Alert.AlertType.WARNING, "Game", "Game Over !",true);
            return true;
        }
        return false;
    }

    public void clearGridPane(GridPane gridPane) {
        gridPane.getChildren().clear();

    }

    private void showSearchResultMovieList() {
        clearGridPane(gridPaneMovieRectangles);
        int row = 0;
        tiles = new ArrayList<>();
        for (Movie movie : searchResults) {
            int col = 0;
            Tile tile = createTile(movie.getTitle(), currentMovie.getTitle());
            tiles.add(tile);
            gridPaneMovieRectangles.add(tile, col, row);
            col++;
            Tile tile2 = createTile(movie.getYear(), currentMovie.getYear());
            tiles.add(tile2);
            gridPaneMovieRectangles.add(tile2, col, row);
            col++;
            Tile tile3 = createTile(movie.getGenre(), currentMovie.getGenre());
            tiles.add(tile3);
            gridPaneMovieRectangles.add(tile3, col, row);
            col++;
            Tile tile4 = createTile(movie.getOrigin(), currentMovie.getOrigin());
            tiles.add(tile4);
            gridPaneMovieRectangles.add(tile4, col, row);
            col++;
            Tile tile5 = createTile(movie.getDirector(), currentMovie.getDirector());
            tiles.add(tile5);
            gridPaneMovieRectangles.add(tile5, col, row);
            col++;
            Tile tile6 = createTile(movie.getStar(), currentMovie.getStar());
            tiles.add(tile6);
            gridPaneMovieRectangles.add(tile6, col, row);
            row++;
        }
    }

    private Tile createTile(String movieItem, String currentMovieItem) {
        Tile tile = new Tile(TILE_SIZE_WIDHT + movieItem.length() * 5, TILE_SIZE_HEIGTH, movieItem, currentMovieItem);
        return tile;
    }

    private void showWinMessage(Alert.AlertType information, String title, String message, boolean flagGameOver) {
        Alert alert = new Alert(information);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        showAlertButtons(alert);

    }
    private void showAlertButtons(Alert alert) {
        ButtonType replayButton = new ButtonType("Replay Play");
        ButtonType exitButton = new ButtonType("Exit Game");
        // add buttons
        alert.getButtonTypes().setAll(replayButton, exitButton);

        // show message and button
        alert.showAndWait().ifPresent(response -> {
            // return play game
            if (response == replayButton) {
                primaryStage.setScene(null); //
                primaryStage.hide(); //
                start(primaryStage); // start
            }
            // exit game
            else if (response == exitButton) {
                primaryStage.close();
            }

        });
    }



}
