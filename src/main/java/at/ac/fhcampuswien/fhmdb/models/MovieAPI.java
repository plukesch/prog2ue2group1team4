package at.ac.fhcampuswien.fhmdb.models;

import com.google.gson.reflect.TypeToken;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class MovieAPI extends BaseAPI{

    private Type movieListType = new TypeToken<List<Movie>>(){}.getType();

    private final String PATH = "movies";

    public List<Movie> index(){
        HttpUrl url = this.buildUrl();
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

    private HttpUrl buildUrl(){
        HttpUrl.Builder urlBuilder = HttpUrl.parse(this.BASE_URL).newBuilder();
        HttpUrl url = urlBuilder.addPathSegment(this.PATH).build();


        return url;
    }
}
