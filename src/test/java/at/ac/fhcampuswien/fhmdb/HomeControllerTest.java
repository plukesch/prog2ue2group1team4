package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.models.SortedState;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

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

    /*@Test
    void if_not_yet_sorted_sort_is_applied_in_ascending_order() {
        // given
        homeController.initializeState();
        homeController.sortedState = SortedState.NONE;

        // when
        homeController.sortMovies();

        // then
        List<Movie> expectedMovies = Arrays.asList(
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
                )
                /*new Movie(
                        "Life Is Beautiful",
                        "When an open-minded Jewish librarian and his son become victims of the Holocaust, he uses a perfect mixture of will, humor, and imagination to protect his son from the dangers around their camp." ,
                        Arrays.asList(Genre.DRAMA, Genre.ROMANCE)),
                new Movie(
                        "Puss in Boots",
                        "An outlaw cat, his childhood egg-friend, and a seductive thief kitty set out in search for the eggs of the fabled Golden Goose to clear his name, restore his lost honor, and regain the trust of his mother and town.",
                        Arrays.asList(Genre.COMEDY, Genre.FAMILY, Genre.ANIMATION)),
                new Movie(
                        "The Usual Suspects",
                        "A sole survivor tells of the twisty events leading up to a horrific gun battle on a boat, which begin when five criminals meet at a seemingly random police lineup.",
                        Arrays.asList(Genre.CRIME, Genre.DRAMA, Genre.MYSTERY)),
                new Movie(
                        "The Wolf of Wall Street",
                        "Based on the true story of Jordan Belfort, from his rise to a wealthy stock-broker living the high life to his fall involving crime, corruption and the federal government.",
                        Arrays.asList(Genre.DRAMA, Genre.ROMANCE, Genre.BIOGRAPHY))

        );

        //assertEquals(expected, homeController.observableMovies);

        assertEquals(expectedMovies.size(), homeController.observableMovies.size());

        for (int i = 0; i < expectedMovies.size(); i++) {
            Movie expected = expectedMovies.get(i);
            Movie actual = homeController.observableMovies.get(i);

            assertEquals(expected.getTitle(), actual.getTitle());
            assertEquals(expected.getDescription(), actual.getDescription());
            assertTrue(expected.getGenres().containsAll(actual.getGenres()) && actual.getGenres().containsAll(expected.getGenres()));
            assertTrue(expected.getMainCast().containsAll(actual.getMainCast()) && actual.getMainCast().containsAll(expected.getMainCast()));
            assertEquals(expected.getDirector(), actual.getDirector());
            assertEquals(expected.getReleaseYear(), actual.getReleaseYear());
            assertEquals(expected.getRating(), actual.getRating(), 0.01);
        }

    }*/

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
    void if_last_sort_ascending_next_sort_should_be_descending() {
        // given
        homeController.initializeState();
        homeController.sortedState = SortedState.ASCENDING;

        // when
        homeController.sortMovies();

        // then
        List<Movie> expected = Arrays.asList(
                new Movie(
                        "Avatar",
                        "A paraplegic Marine dispatched to the moon Pandora on a unique mission becomes torn between following his orders and protecting the world he feels is his home.",
                        Arrays.asList(Genre.ANIMATION, Genre.DRAMA, Genre.ACTION),
                        Arrays.asList("Actor1", "Actor2"), // Beispiel Hauptdarsteller
                        "Director Name", // Beispiel Regisseur
                        2009, // Veröffentlichungsjahr
                        7.6
                )
                /*new Movie(
                        "The Usual Suspects",
                        "A sole survivor tells of the twisty events leading up to a horrific gun battle on a boat, which begin when five criminals meet at a seemingly random police lineup.",
                        Arrays.asList(Genre.CRIME, Genre.DRAMA, Genre.MYSTERY)),
                new Movie(
                        "Puss in Boots",
                        "An outlaw cat, his childhood egg-friend, and a seductive thief kitty set out in search for the eggs of the fabled Golden Goose to clear his name, restore his lost honor, and regain the trust of his mother and town.",
                        Arrays.asList(Genre.COMEDY, Genre.FAMILY, Genre.ANIMATION)),
                new Movie(
                        "Life Is Beautiful",
                        "When an open-minded Jewish librarian and his son become victims of the Holocaust, he uses a perfect mixture of will, humor, and imagination to protect his son from the dangers around their camp." ,
                        Arrays.asList(Genre.DRAMA, Genre.ROMANCE)),
                new Movie(
                        "Avatar",
                        "A paraplegic Marine dispatched to the moon Pandora on a unique mission becomes torn between following his orders and protecting the world he feels is his home.",
                        Arrays.asList(Genre.ANIMATION, Genre.DRAMA, Genre.ACTION))*/
        );

        assertEquals(expected, homeController.observableMovies);
    }

    @Test
    void if_last_sort_descending_next_sort_should_be_ascending() {
        // given
        homeController.initializeState();
        homeController.sortedState = SortedState.DESCENDING;

        // when
        homeController.sortMovies();

        // then
        List<Movie> expected = Arrays.asList(
                new Movie(
                        "Avatar",
                        "A paraplegic Marine dispatched to the moon Pandora on a unique mission becomes torn between following his orders and protecting the world he feels is his home.",
                        Arrays.asList(Genre.ANIMATION, Genre.DRAMA, Genre.ACTION),
                        Arrays.asList("Actor1", "Actor2"), // Beispiel Hauptdarsteller
                        "Director Name", // Beispiel Regisseur
                        2009, // Veröffentlichungsjahr
                        7.6
                )
                /*new Movie(
                        "Life Is Beautiful",
                        "When an open-minded Jewish librarian and his son become victims of the Holocaust, he uses a perfect mixture of will, humor, and imagination to protect his son from the dangers around their camp." ,
                        Arrays.asList(Genre.DRAMA, Genre.ROMANCE)),
                new Movie(
                        "Puss in Boots",
                        "An outlaw cat, his childhood egg-friend, and a seductive thief kitty set out in search for the eggs of the fabled Golden Goose to clear his name, restore his lost honor, and regain the trust of his mother and town.",
                        Arrays.asList(Genre.COMEDY, Genre.FAMILY, Genre.ANIMATION)),
                new Movie(
                        "The Usual Suspects",
                        "A sole survivor tells of the twisty events leading up to a horrific gun battle on a boat, which begin when five criminals meet at a seemingly random police lineup.",
                        Arrays.asList(Genre.CRIME, Genre.DRAMA, Genre.MYSTERY)),
                new Movie(
                        "The Wolf of Wall Street",
                        "Based on the true story of Jordan Belfort, from his rise to a wealthy stock-broker living the high life to his fall involving crime, corruption and the federal government.",
                        Arrays.asList(Genre.DRAMA, Genre.ROMANCE, Genre.BIOGRAPHY))*/

        );

        assertEquals(expected, homeController.observableMovies);

    }

    @Test
    void query_filter_matches_with_lower_and_uppercase_letters(){
        // given
        homeController.initializeState();
        String query = "IfE";

        // when
        List<Movie> actual = homeController.filterByQuery(homeController.observableMovies, query);

        // then
        List<Movie> expected = Arrays.asList(
                new Movie(
                        "The Wolf of Wall Street",
                        "Based on the true story of Jordan Belfort, from his rise to a wealthy stock-broker living the high life to his fall involving crime, corruption and the federal government.",
                        Arrays.asList(Genre.DRAMA, Genre.ROMANCE, Genre.BIOGRAPHY),
                        Arrays.asList("Actor1", "Actor2"), // Beispiel Hauptdarsteller
                        "Director Name", // Beispiel Regisseur
                        2009, // Veröffentlichungsjahr
                        7.6
                )
                /*new Movie(
                        "The Wolf of Wall Street",
                        "Based on the true story of Jordan Belfort, from his rise to a wealthy stock-broker living the high life to his fall involving crime, corruption and the federal government.",
                        Arrays.asList(Genre.DRAMA, Genre.ROMANCE, Genre.BIOGRAPHY))*/
        );

        assertEquals(expected, actual);
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
        homeController.initializeState();
        Genre genre = Genre.DRAMA;

        // when
        List<Movie> actual = homeController.filterByGenre(homeController.observableMovies, genre);

        // then
        assertEquals(4, actual.size());
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
    void getMostPopularActor_returns_correct_actor() {
        // given
        List<Movie> movies = Arrays.asList(
                new Movie(
                        "Avatar",
                        "A paraplegic Marine dispatched to the moon Pandora on a unique mission becomes torn between following his orders and protecting the world he feels is his home.",
                        Arrays.asList(Genre.ACTION, Genre.ADVENTURE, Genre.FANTASY, Genre.SCIENCE_FICTION),
                        Arrays.asList("Sam Worthington", "Zoe Saldana", "Sigourney Weaver", "Jonah Hill"), // Beispiel Hauptdarsteller
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
                ));

        // when
        String mostPopularActor = homeController.getMostPopularActor(movies);

        // then
        assertEquals("Jonah Hill", mostPopularActor);
    }

    /*@Test
    void getLongestMovieTitle_returns_correct_length() {
        // given
        List<Movie> movies = Arrays.asList(
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
                ));

        // when
        int maxLength = homeController.getLongestMovieTitle(movies);

        // then
        assertEquals(expectedLength, maxLength);
    }

    @Test
    void countMoviesFrom_returns_correct_count() {
        // given
        List<Movie> movies = // Initialisiere deine Liste von Movies hier

        // when
        long count = homeController.countMoviesFrom(movies, "Director Name");

        // then
        assertEquals(expectedCount, count);
    }

    @Test
    void getMoviesBetweenYears_returns_correct_movies() {
        // given
        List<Movie> movies = // Initialisiere deine Liste von Movies hier

                // when
                List<Movie> filteredMovies = homeController.getMoviesBetweenYears(movies, 2000, 2010);

        // then
        assertEquals(expectedNumberOfMovies, filteredMovies.size());
        // Zusätzliche Assertions können hinzugefügt werden, um zu prüfen, ob die zurückgegebenen Filme die erwarteten sind
    }*/
}