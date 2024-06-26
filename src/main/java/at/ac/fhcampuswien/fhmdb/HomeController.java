package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.models.MovieAPI;
import at.ac.fhcampuswien.fhmdb.models.SortedState;
import at.ac.fhcampuswien.fhmdb.ui.MovieCell;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.math.BigDecimal;
import java.math.RoundingMode;

import java.net.URL;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class HomeController implements Initializable {
    @FXML
    public JFXButton searchBtn;

    @FXML
    public TextField searchField;

    @FXML
    public JFXListView movieListView;

    @FXML
    public JFXComboBox genreComboBox;

    @FXML
    public JFXButton sortBtn;

    public List<Movie> allMovies;
    @FXML
    public JFXComboBox yearComboBox;
    @FXML
    public JFXComboBox ratingComboBox;

    protected ObservableList<Movie> observableMovies = FXCollections.observableArrayList();

    protected SortedState sortedState;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeState();
        initializeLayout();
    }

    public void initializeState() {
        MovieAPI api = new MovieAPI();
        allMovies = api.index("", "", "", ""); // Leere Strings für initiales Laden ohne Filter
        observableMovies.clear();
        observableMovies.addAll(allMovies); // Fügt alle Filme zur observable list hinzu
        sortedState = SortedState.NONE;
    }

    public void initializeLayout() {
        movieListView.setItems(observableMovies);   // set the items of the listview to the observable list
        movieListView.setCellFactory(movieListView -> new MovieCell()); // apply custom cells to the listview

        Object[] genres = Genre.values();   // get all genres
        yearComboBox.getItems().add("No filter");
        yearComboBox.getItems().addAll(getReleasedYears());
        ratingComboBox.getItems().add("No filter");
        ratingComboBox.getItems().addAll(getRatings());
        genreComboBox.getItems().add("No filter");  // add "no filter" to the combobox
        genreComboBox.getItems().addAll(genres);    // add all genres to the combobox
        genreComboBox.setPromptText("Filter by Genre");
        yearComboBox.setPromptText("Filter by Release Year");
        ratingComboBox.setPromptText("Filter by Rating");
    }

    public void sortMovies(){
        if (sortedState == SortedState.NONE || sortedState == SortedState.DESCENDING) {
            sortMovies(SortedState.ASCENDING);
        } else if (sortedState == SortedState.ASCENDING) {
            sortMovies(SortedState.DESCENDING);
        }
    }
    // sort movies based on sortedState
    // by default sorted state is NONE
    // afterwards it switches between ascending and descending
    public void sortMovies(SortedState sortDirection) {
        if (sortDirection == SortedState.ASCENDING) {
            observableMovies.sort(Comparator.comparing(Movie::getTitle));
            sortedState = SortedState.ASCENDING;
        } else {
            observableMovies.sort(Comparator.comparing(Movie::getTitle).reversed());
            sortedState = SortedState.DESCENDING;
        }
    }

    public List<Movie> filterByQuery(List<Movie> movies, String query){
        if(query == null || query.isEmpty()) return movies;

        if(movies == null) {
            throw new IllegalArgumentException("movies must not be null");
        }

        return movies.stream()
                .filter(Objects::nonNull)
                .filter(movie ->
                    movie.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                    movie.getDescription().toLowerCase().contains(query.toLowerCase())
                )
                .toList();
    }

    public List<Movie> filterByGenre(List<Movie> movies, Genre genre){
        if(genre == null) return movies;

        if(movies == null) {
            throw new IllegalArgumentException("movies must not be null");
        }

        return movies.stream()
                .filter(Objects::nonNull)
                .filter(movie -> movie.getGenres().contains(genre))
                .toList();
    }

    public void applyAllFilters(String searchQuery, Object genre) {
        List<Movie> filteredMovies = allMovies;

        if (!searchQuery.isEmpty()) {
            filteredMovies = filterByQuery(filteredMovies, searchQuery);
        }

        if (genre != null && !genre.toString().equals("No filter")) {
            filteredMovies = filterByGenre(filteredMovies, Genre.valueOf(genre.toString()));
        }

        observableMovies.clear();
        observableMovies.addAll(filteredMovies);
    }

    public void searchBtnClicked(ActionEvent actionEvent) {
        // Extrahiert die Eingaben aus den UI-Komponenten und prüft, ob "No filter" ausgewählt wurde.
        String searchQuery = searchField.getText().trim();
        String genre = genreComboBox.getValue() != null && !genreComboBox.getValue().equals("No filter")
                ? genreComboBox.getValue().toString() : "";
        String releaseYear = yearComboBox.getValue() != null && !yearComboBox.getValue().equals("No filter")
                ? yearComboBox.getValue().toString() : "";
        String rating = ratingComboBox.getValue() != null && !ratingComboBox.getValue().equals("No filter")
                ? ratingComboBox.getValue().toString() : "";

        // Ruft Filme von der API ab, ohne das Rating als Filter zu verwenden.
        List<Movie> filteredMovies = new MovieAPI().index(searchQuery, genre, releaseYear, rating);

        // Filtert nach dem Rating, falls ein spezifischer Wert ausgewählt wurde.
        if (!rating.isEmpty()) {
            double ratingValue = Double.parseDouble(rating); // Konvertiert den Rating-String in einen Double-Wert.
            filteredMovies = filteredMovies.stream()
                    .filter(movie -> movie.getRating() == ratingValue)
                    .collect(Collectors.toList());
        }

        // Aktualisiert die ObservableList und die ListView.
        observableMovies.setAll(filteredMovies);
    }

    public void sortBtnClicked(ActionEvent actionEvent) {
        sortMovies();
    }

    private ArrayList<Integer> getReleasedYears(){
        int start = 1950;
        int end = 2024;
        ArrayList<Integer> years = new ArrayList<>();

        for (int i = start; i <= end; i++) {
            years.add(i);
        }
        return years;
    }

    private ArrayList<Double> getRatings(){
        double start = 0.0;
        double end = 10.1;
        ArrayList<Double> ratings = new ArrayList<>();

        for (double i = start; i <= end; i += 0.1) {
            ratings.add(new BigDecimal(i).setScale(1, RoundingMode.DOWN).doubleValue());
        }
        return ratings;
    }

    public String getMostPopularActor() {
        return allMovies.stream()
                .flatMap(movie -> movie.getMainCast().stream())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    public int getLongestMovieTitle() {
        return allMovies.stream()
                .map(Movie::getTitle)
                .mapToInt(String::length)
                .max()
                .orElse(0);
    }

    public long countMoviesFrom(String director) {
        return allMovies.stream()
                .filter(movie -> movie.getDirector().equals(director))
                .count();
    }

    public List<Movie> getMoviesBetweenYears(int startYear, int endYear) {
        return allMovies.stream()
                .filter(movie -> movie.getReleaseYear() >= startYear && movie.getReleaseYear() <= endYear)
                .collect(Collectors.toList());
    }
}