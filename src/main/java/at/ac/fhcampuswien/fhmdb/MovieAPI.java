package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class MovieAPI {
    private static final String BASE_URL = "https://prog2.fh-campuswien.ac.at/movies";
    private final OkHttpClient client = new OkHttpClient();

    // Methode zum Abrufen aller Filme
    public String getAllMovies() throws Exception {
        Request request = new Request.Builder()
                .url(BASE_URL)
                .addHeader("User-Agent", "http.agent") // Vermeidung von 403 Forbidden Fehler
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            // Gibt den JSON-String zurück
            assert response.body() != null;
            return response.body().string();
        }
    }

    // Methode zum Abrufen von Filmen basierend auf Suche und Genre
    public String getMovies(String query, String genre) throws Exception {
        String url = BASE_URL;

        // Überprüfung, ob Parameter hinzugefügt werden sollen
        if (query != null && !query.isEmpty() || genre != null && !genre.isEmpty()) {
            url += "?";
            if (query != null && !query.isEmpty()) {
                url += "query=" + query;
                if (genre != null && !genre.isEmpty()) {
                    url += "&";
                }
            }
            if (genre != null && !genre.isEmpty()) {
                url += "genre=" + genre.toUpperCase(); // API erwartet Großbuchstaben für Genre
            }
        }

        Request request = new Request.Builder()
                .url(url)
                .addHeader("User-Agent", "http.agent")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            assert response.body() != null;
            return response.body().string();
        }
    }

    public List<Movie> parseMoviesJson(String json) {
        Gson gson = new Gson();
        Type movieListType = new TypeToken<List<Movie>>(){}.getType();
        return gson.fromJson(json, movieListType);
    }
}