package at.ac.fhcampuswien.fhmdb.models;

import com.google.gson.reflect.TypeToken;
import okhttp3.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class MovieAPI extends BaseAPI{

    private Type movieListType = new TypeToken<List<Movie>>(){}.getType();

    private final String PATH = "movies";


    public List<Movie> index(String query, String genre, String releaseYear, String rating){
        HttpUrl url = this.buildUrlWithParams(query, genre, releaseYear, rating);
        Request request = new Request.Builder()
                .url(url.toString())
                .addHeader("User-Agent", "http.agent")
                .build();

        try (Response response = client.newCall(request).execute()){
            return this.parseMovies(response.body().string());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Movie> parseMovies(String json){
        ArrayList<Movie> parsedMovies = gson.fromJson(json, movieListType);
        return parsedMovies;
    }

    /*private List<Movie> parseMovies(String json) {
        // Diese Implementierung hängt von der Struktur des JSON und der Bibliothek ab, die du für das Parsing verwendest (z.B. Gson).
        // Angenommen, du verwendest Gson für das Parsing:
        Type listOfMoviesType = new TypeToken<List<Movie>>(){}.getType();
        List<Movie> movies = gson.fromJson(json, listOfMoviesType);

        // Hypothetische Annahme, dass jede Movie-Information in einem separaten Objekt innerhalb des JSON ist,
        // und dass du für jedes dieser Objekte ein neues Movie-Objekt erstellst.
        return movies.stream().map(movieJson -> {
            String title = movieJson.getTitle();
            String description = movieJson.getDescription();
            List<Genre> genres = movieJson.getGenres(); // Angenommen, die Genres sind direkt verfügbar und passen zum Enum.
            List<String> mainCast = movieJson.getMainCast(); // Diese Zeile setzt voraus, dass eine getMainCast-Methode existiert.
            String director = movieJson.getDirector(); // Setzt voraus, dass eine getDirector-Methode existiert.
            int releaseYear = movieJson.getReleaseYear(); // Setzt voraus, dass eine getReleaseYear-Methode existiert.

            // Erstellen eines neuen Movie-Objekts mit den extrahierten Daten
            return new Movie(title, description, genres, mainCast, director, releaseYear);
        }).collect(Collectors.toList());
    }*/

    private HttpUrl buildUrl() {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(this.BASE_URL).newBuilder();

        urlBuilder.addQueryParameter("genre", "ACTION");

        HttpUrl url = urlBuilder.addPathSegment(this.PATH).build();

        System.out.println(urlBuilder);

        return url;
    }

    private HttpUrl buildUrlWithParams(String query, String genre, String releaseYear, String rating) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL).newBuilder();
        urlBuilder.addPathSegment(PATH);

        if (query != null && !query.isEmpty()) {
            urlBuilder.addQueryParameter("query", query);
        }
        if (genre != null && !genre.isEmpty() && !genre.equals("No filter")) {
            urlBuilder.addQueryParameter("genre", genre);
        }
        if (releaseYear != null && !releaseYear.isEmpty() && !releaseYear.equals("No filter")) {
            urlBuilder.addQueryParameter("releaseYear", releaseYear);
        }
        if (rating != null && !rating.isEmpty() && !rating.equals("No filter")) {
            urlBuilder.addQueryParameter("rating", rating);
        }

        System.out.println(urlBuilder);

        return urlBuilder.build();
    }
}
