package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.models.MovieAPI;
import at.ac.fhcampuswien.fhmdb.models.SortedState;
import at.ac.fhcampuswien.fhmdb.ui.MovieCell;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import java.net.URL;
import java.util.*;

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
        ratingComboBox.getItems().addAll(getRating());
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

    /*public void searchBtnClicked(ActionEvent actionEvent) {
        String searchQuery = searchField.getText().trim();
        String genre = "";
        if (genreComboBox.getValue() != null){
            genre = genreComboBox.getValue().toString();
            if (genre.equals("No filter")){
                genre = "";
            }
        }

        List<Movie> filteredMovies = new MovieAPI().index(searchQuery, genre);
        observableMovies.setAll(filteredMovies);
        sortMovies(sortedState);
    }*/

    public void searchBtnClicked(ActionEvent actionEvent) {
        String searchQuery = searchField.getText().trim();
        String genre = genreComboBox.getValue() != null ? genreComboBox.getValue().toString() : "No filter";
        String releaseYear = yearComboBox.getValue() != null ? yearComboBox.getValue().toString() : "No filter";
        String rating = ratingComboBox.getValue() != null ? ratingComboBox.getValue().toString() : "No filter";

        genre = genre.equals("No filter") ? "" : genre;
        releaseYear = releaseYear.equals("No filter") ? "" : releaseYear;
        rating = rating.equals("No filter") ? "" : rating;

        List<Movie> filteredMovies = new MovieAPI().index(searchQuery, genre, releaseYear, rating);
        observableMovies.setAll(filteredMovies);
        sortMovies(sortedState);
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

    private ArrayList<Double> getRating(){
        double start = 0.0;
        double end = 10.1;
        ArrayList<Double> ratings = new ArrayList<>();

        for (double i = start; i <= end; i += 0.1) {
            ratings.add(new BigDecimal(i).setScale(1, RoundingMode.DOWN).doubleValue());
        }
        return ratings;
    }
}