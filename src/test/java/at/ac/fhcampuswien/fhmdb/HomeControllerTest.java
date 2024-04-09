package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.models.SortedState;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.*;

class HomeControllerTest {
    private static HomeController homeController;
    @BeforeAll
    static void init() {
        homeController = new HomeController();
    }

    @Test
    void at_initialization_allMovies_and_observableMovies_should_be_filled_and_equal() {
        homeController.initializeState();
        assertEquals(homeController.allMovies, homeController.observableMovies);
    }

    @Test
    void if_not_yet_sorted_sort_is_applied_in_ascending_order() {
        // given
        homeController.initializeState();
        homeController.sortedState = SortedState.NONE;

        // when
        homeController.sortMovies();

        // then
        assertFalse(homeController.observableMovies.isEmpty());
        int n = homeController.observableMovies.size();
        for (int i = 0; i < n - 1; i++) {
            String title1 = homeController.observableMovies.get(i).getTitle();
            String title2 = homeController.observableMovies.get(i + 1).getTitle();
            assertTrue(title1.compareTo(title2) <= 0);
        }
    }

    @Test
    void if_sorted_ascending_then_sort_is_toggled_to_descending_order() {
    // given
    homeController.initializeState();
    homeController.sortedState = SortedState.ASCENDING;  // Set initial state to ASCENDING

    // when
    homeController.sortMovies();  // This should toggle sorting to descending

    // then
    assertFalse(homeController.observableMovies.isEmpty());
    int n = homeController.observableMovies.size();
    for (int i = 0; i < n - 1; i++) {
        String title1 = homeController.observableMovies.get(i).getTitle();
        String title2 = homeController.observableMovies.get(i + 1).getTitle();
        assertTrue(title1.compareTo(title2) >= 0);  // Check for descending order
    }
}

    @Test
void if_last_sort_descending_next_sort_should_be_ascending() {
    // given
    homeController.initializeState();
    homeController.sortedState = SortedState.DESCENDING;  // Set initial state to DESCENDING

    // when
    homeController.sortMovies();  // This should toggle sorting to ascending

    // then
    assertFalse(homeController.observableMovies.isEmpty());
    int n = homeController.observableMovies.size();
    for (int i = 0; i < n - 1; i++) {
        String title1 = homeController.observableMovies.get(i).getTitle();
        String title2 = homeController.observableMovies.get(i + 1).getTitle();
        assertTrue(title1.compareTo(title2) <= 0);  // Check for ascending order
    }
}

    @Test
    void query_filter_matches_with_lower_and_uppercase_letters() {
        // given
        HomeController homeController = new HomeController();
        homeController.initializeState();  // Setup initial state and data

        // Assuming the initializeState method populates `allMovies` with test data
        homeController.allMovies = List.of(
                new Movie(
                        "Life Is Beautiful",
                        "When an open-minded Jewish librarian and his son become victims of the Holocaust, he uses a perfect mixture of will, humor, and imagination to protect his son from the dangers around their camp." ,
                        Arrays.asList(Genre.DRAMA, Genre.ROMANCE),
                        Arrays.asList("Roberto Benigni", "Nicoletta Braschi", "Giorgio Cantarini"), // Beispiel Hauptdarsteller
                        "Roberto Benigni", // Beispiel Regisseur
                        1997, // Veröffentlichungsjahr
                        8.6
                )
        );

        String query = "IfE";  // The query to test case insensitivity

        // when
        homeController.applyAllFilters(query, null);  // Apply filter with the query and no genre

        // then
        assertEquals(1, homeController.observableMovies.size());
        assertTrue(homeController.observableMovies.stream()
            .anyMatch(movie -> movie.getTitle().equalsIgnoreCase("Life is Beautiful")));
    }


    @Test
    void query_filter_with_null_movie_list_throws_exception(){
        // given
        homeController.initializeState();
        String query = "IfE";

        // when and then
        assertThrows(IllegalArgumentException.class, () -> homeController.filterByQuery(null, query));
    }

    @Test
    void query_filter_with_null_value_returns_unfiltered_list() {
        // given
        homeController.initializeState();
        String query = null;

        // when
        List<Movie> actual = homeController.filterByQuery(homeController.observableMovies, query);

        // then
        assertEquals(homeController.observableMovies, actual);
    }

    @Test
    void genre_filter_with_null_value_returns_unfiltered_list() {
        // given
        homeController.initializeState();
        Genre genre = null;

        // when
        List<Movie> actual = homeController.filterByGenre(homeController.observableMovies, genre);

        // then
        assertEquals(homeController.observableMovies, actual);
    }

    @Test
    void genre_filter_returns_all_movies_containing_given_genre() {
        // given
        List<Movie> allMovies = Arrays.asList(
                new Movie(
                        "Avatar",
                        "A paraplegic Marine dispatched to the moon Pandora on a unique mission becomes torn between following his orders and protecting the world he feels is his home.",
                        Arrays.asList(Genre.ACTION, Genre.ADVENTURE, Genre.FANTASY, Genre.SCIENCE_FICTION),
                        Arrays.asList("Sam Worthington", "Zoe Saldana", "Sigourney Weaver"), // Beispiel Hauptdarsteller
                        "James Cameron", // Beispiel Regisseur
                        2009, // Veröffentlichungsjahr
                        7.6
                ),
                new Movie(
                        "The Wolf of Wall Street",
                        "Based on the true story of Jordan Belfort, from his rise to a wealthy stock-broker living the high life to his fall involving crime, corruption and the federal government.",
                        Arrays.asList(Genre.DRAMA, Genre.ROMANCE, Genre.BIOGRAPHY),
                        Arrays.asList("Leonardo Dicaprio", "Jonah Hill", "Margot Robbie"), // Beispiel Hauptdarsteller
                        "Martin Scorsese", // Beispiel Regisseur
                        2013, // Veröffentlichungsjahr
                        8.2
                ),
                new Movie(
                        "Life Is Beautiful",
                        "When an open-minded Jewish librarian and his son become victims of the Holocaust, he uses a perfect mixture of will, humor, and imagination to protect his son from the dangers around their camp." ,
                        Arrays.asList(Genre.DRAMA, Genre.ROMANCE),
                        Arrays.asList("Roberto Benigni", "Nicoletta Braschi", "Giorgio Cantarini"), // Beispiel Hauptdarsteller
                        "Roberto Benigni", // Beispiel Regisseur
                        1997, // Veröffentlichungsjahr
                        8.6
                ),
                new Movie(
                        "Puss in Boots",
                        "An outlaw cat, his childhood egg-friend, and a seductive thief kitty set out in search for the eggs of the fabled Golden Goose to clear his name, restore his lost honor, and regain the trust of his mother and town.",
                        Arrays.asList(Genre.COMEDY, Genre.FAMILY, Genre.ANIMATION),
                        Arrays.asList("Antonio Banderas", "Salma Hayek", "Zach Galifianakis"), // Beispiel Hauptdarsteller
                        "Chris Miller", // Beispiel Regisseur
                        2011, // Veröffentlichungsjahr
                        6.6
                        ),
                new Movie(
                        "The Usual Suspects",
                        "A sole survivor tells of the twisty events leading up to a horrific gun battle on a boat, which begin when five criminals meet at a seemingly random police lineup.",
                        Arrays.asList(Genre.CRIME, Genre.DRAMA, Genre.MYSTERY),
                        Arrays.asList("Kevin Spacey", "Gabriel Byrne", "Chazz Palminteri"), // Beispiel Hauptdarsteller
                        "Bryan Singer", // Beispiel Regisseur
                        1995, // Veröffentlichungsjahr
                        8.6
                        )
);
        Genre filterGenre = Genre.DRAMA;

        // when
        List<Movie> filteredMovies = allMovies.stream()
                .filter(movie -> movie.getGenres().contains(filterGenre))
                .collect(Collectors.toList());

        // then
        assertEquals(3, filteredMovies.size(), "Should return exactly two movies with the given genre.");
        assertTrue(filteredMovies.stream().allMatch(movie -> movie.getGenres().contains(Genre.DRAMA)),
                "All returned movies should contain the genre DRAMA.");
    }


    @Test
    void no_filtering_ui_if_empty_query_or_no_genre_is_set() {
        // given
        homeController.initializeState();

        // when
        homeController.applyAllFilters("", null);

        // then
        assertEquals(homeController.allMovies, homeController.observableMovies);
    }

    @Test
    void is_the_most_popular_actor_correctly_identified() {
        // Given
        HomeController homeController = new HomeController();
        homeController.allMovies = Arrays.asList(
                new Movie(
                        "Avatar",
                        "A paraplegic Marine dispatched to the moon Pandora on a unique mission becomes torn between following his orders and protecting the world he feels is his home.",
                        Arrays.asList(Genre.ACTION, Genre.ADVENTURE, Genre.FANTASY, Genre.SCIENCE_FICTION),
                        Arrays.asList("Sam Worthington", "Zoe Saldana", "Sigourney Weaver"), // Beispiel Hauptdarsteller
                        "James Cameron", // Beispiel Regisseur
                        2009, // Veröffentlichungsjahr
                        7.6
                ),
                new Movie(
                        "The Wolf of Wall Street",
                        "Based on the true story of Jordan Belfort, from his rise to a wealthy stock-broker living the high life to his fall involving crime, corruption and the federal government.",
                        Arrays.asList(Genre.DRAMA, Genre.ROMANCE, Genre.BIOGRAPHY),
                        Arrays.asList("Leonardo Dicaprio", "Jonah Hill", "Margot Robbie"), // Beispiel Hauptdarsteller
                        "Martin Scorsese", // Beispiel Regisseur
                        2013, // Veröffentlichungsjahr
                        8.2
                ),
                new Movie(
                        "Inception",
                        "A thief, who steals corporate secrets through use of dream-sharing technology, is given the inverse task of planting an idea into the mind of a CEO.",
                        Arrays.asList(Genre.ACTION, Genre.ADVENTURE, Genre.THRILLER, Genre.SCIENCE_FICTION),
                        Arrays.asList("Leonardo Dicaprio", "Joseph Gordon-Levitt", "Elliot Page"), // Beispiel Hauptdarsteller
                        "Christopher Nolan", // Beispiel Regisseur
                        2010, // Veröffentlichungsjahr
                        8.8
                )
        );

        // When
        String mostPopularActor = homeController.getMostPopularActor();

        // Then
        assertEquals("Leonardo Dicaprio", mostPopularActor, "Leonardo Dicaprio should be identified as the most popular actor.");
    }
    @Test
    void longest_movie_title_returns_correct_length() {
        // Given
        HomeController homeController = new HomeController();
        homeController.allMovies = Arrays.asList(
                new Movie(
                        "Avatar",
                        "A paraplegic Marine dispatched to the moon Pandora on a unique mission becomes torn between following his orders and protecting the world he feels is his home.",
                        Arrays.asList(Genre.ACTION, Genre.ADVENTURE, Genre.FANTASY, Genre.SCIENCE_FICTION),
                        Arrays.asList("Sam Worthington", "Zoe Saldana", "Sigourney Weaver"), // Beispiel Hauptdarsteller
                        "James Cameron", // Beispiel Regisseur
                        2009, // Veröffentlichungsjahr
                        7.6
                ),
                new Movie(
                        "The Wolf of Wall Street",
                        "Based on the true story of Jordan Belfort, from his rise to a wealthy stock-broker living the high life to his fall involving crime, corruption and the federal government.",
                        Arrays.asList(Genre.DRAMA, Genre.ROMANCE, Genre.BIOGRAPHY),
                        Arrays.asList("Leonardo Dicaprio", "Jonah Hill", "Margot Robbie"), // Beispiel Hauptdarsteller
                        "Martin Scorsese", // Beispiel Regisseur
                        2013, // Veröffentlichungsjahr
                        8.2
                ),
                new Movie(
                        "Inception",
                        "A thief, who steals corporate secrets through use of dream-sharing technology, is given the inverse task of planting an idea into the mind of a CEO.",
                        Arrays.asList(Genre.ACTION, Genre.ADVENTURE, Genre.THRILLER, Genre.SCIENCE_FICTION),
                        Arrays.asList("Leonardo Dicaprio", "Joseph Gordon-Levitt", "Elliot Page"), // Beispiel Hauptdarsteller
                        "Christopher Nolan", // Beispiel Regisseur
                        2010, // Veröffentlichungsjahr
                        8.8
                )
        );

        // When
        int longestTitleLength = homeController.getLongestMovieTitle();

        // Then
        assertEquals(23, longestTitleLength, "The length of the longest movie title should be 27.");
    }
    @Test
    void counting_movies_returns_correct_count() {
        // Given
        HomeController homeController = new HomeController();
        homeController.allMovies = Arrays.asList(
                new Movie(
                        "Avatar",
                        "A paraplegic Marine dispatched to the moon Pandora on a unique mission becomes torn between following his orders and protecting the world he feels is his home.",
                        Arrays.asList(Genre.ACTION, Genre.ADVENTURE, Genre.FANTASY, Genre.SCIENCE_FICTION),
                        Arrays.asList("Sam Worthington", "Zoe Saldana", "Sigourney Weaver"), // Beispiel Hauptdarsteller
                        "James Cameron", // Beispiel Regisseur
                        2009, // Veröffentlichungsjahr
                        7.6
                ),
                new Movie(
                        "The Wolf of Wall Street",
                        "Based on the true story of Jordan Belfort, from his rise to a wealthy stock-broker living the high life to his fall involving crime, corruption and the federal government.",
                        Arrays.asList(Genre.DRAMA, Genre.ROMANCE, Genre.BIOGRAPHY),
                        Arrays.asList("Leonardo Dicaprio", "Jonah Hill", "Margot Robbie"), // Beispiel Hauptdarsteller
                        "Martin Scorsese", // Beispiel Regisseur
                        2013, // Veröffentlichungsjahr
                        8.2
                ),
                new Movie(
                        "Inception",
                        "A thief, who steals corporate secrets through use of dream-sharing technology, is given the inverse task of planting an idea into the mind of a CEO.",
                        Arrays.asList(Genre.ACTION, Genre.ADVENTURE, Genre.THRILLER, Genre.SCIENCE_FICTION),
                        Arrays.asList("Leonardo Dicaprio", "Joseph Gordon-Levitt", "Elliot Page"), // Beispiel Hauptdarsteller
                        "Christopher Nolan", // Beispiel Regisseur
                        2010, // Veröffentlichungsjahr
                        8.8
                )
        );

        // When
        long count = homeController.countMoviesFrom("Christopher Nolan");

        // Then
        assertEquals(1, count, "There should be 1 movie directed Christopher Nolan.");
    }
    @Test
    void movies_between_years_returns_correct_movies() {
        // Given
        HomeController homeController = new HomeController();
        homeController.allMovies = Arrays.asList(
                new Movie(
                        "Avatar",
                        "A paraplegic Marine dispatched to the moon Pandora on a unique mission becomes torn between following his orders and protecting the world he feels is his home.",
                        Arrays.asList(Genre.ACTION, Genre.ADVENTURE, Genre.FANTASY, Genre.SCIENCE_FICTION),
                        Arrays.asList("Sam Worthington", "Zoe Saldana", "Sigourney Weaver"), // Beispiel Hauptdarsteller
                        "James Cameron", // Beispiel Regisseur
                        2009, // Veröffentlichungsjahr
                        7.6
                ),
                new Movie(
                        "The Wolf of Wall Street",
                        "Based on the true story of Jordan Belfort, from his rise to a wealthy stock-broker living the high life to his fall involving crime, corruption and the federal government.",
                        Arrays.asList(Genre.DRAMA, Genre.ROMANCE, Genre.BIOGRAPHY),
                        Arrays.asList("Leonardo Dicaprio", "Jonah Hill", "Margot Robbie"), // Beispiel Hauptdarsteller
                        "Martin Scorsese", // Beispiel Regisseur
                        2013, // Veröffentlichungsjahr
                        8.2
                ),
                new Movie(
                        "Inception",
                        "A thief, who steals corporate secrets through use of dream-sharing technology, is given the inverse task of planting an idea into the mind of a CEO.",
                        Arrays.asList(Genre.ACTION, Genre.ADVENTURE, Genre.THRILLER, Genre.SCIENCE_FICTION),
                        Arrays.asList("Leonardo Dicaprio", "Joseph Gordon-Levitt", "Elliot Page"), // Beispiel Hauptdarsteller
                        "Christopher Nolan", // Beispiel Regisseur
                        2010, // Veröffentlichungsjahr
                        8.8
                )
        );

        // When
        List<Movie> filteredMovies = homeController.getMoviesBetweenYears(2010, 2020);

        // Then
        assertEquals(2, filteredMovies.size(), "There should be 2 movies released between 2010 and 2020.");
        assertTrue(filteredMovies.stream().anyMatch(movie -> movie.getTitle().equals("The Wolf of Wall Street")), "The Wolf of Wall Street should be included.");
        assertTrue(filteredMovies.stream().anyMatch(movie -> movie.getTitle().equals("Inception")), "Inception should be included.");
    }
}

