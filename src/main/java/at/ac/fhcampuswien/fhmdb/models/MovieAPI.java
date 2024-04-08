package at.ac.fhcampuswien.fhmdb.models;

import com.google.gson.reflect.TypeToken;
import okhttp3.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class MovieAPI extends BaseAPI{

    private Type movieListType = new TypeToken<List<Movie>>(){}.getType();

    private final String PATH = "movies";


    public List<Movie> index(String query, String genre){
        HttpUrl url = this.buildUrlWithParams(query, genre);
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

    private HttpUrl buildUrl() {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(this.BASE_URL).newBuilder();

        urlBuilder.addQueryParameter("genre", "ACTION");

        HttpUrl url = urlBuilder.addPathSegment(this.PATH).build();

        System.out.println(urlBuilder);

        return url;
    }

    private HttpUrl buildUrlWithParams(String query, String genre) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(this.BASE_URL).newBuilder();
        urlBuilder.addPathSegment(this.PATH);

        if (query != null && !query.isEmpty()) {
            urlBuilder.addQueryParameter("query", query);
        }
        if (genre != null && !genre.isEmpty()) {
            urlBuilder.addQueryParameter("genre", genre);
        }

        System.out.println(urlBuilder);

        return urlBuilder.build();
    }
}
