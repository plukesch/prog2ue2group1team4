package at.ac.fhcampuswien.fhmdb.models;

import com.google.gson.Gson;
import okhttp3.OkHttpClient;

public abstract class BaseAPI {
    protected OkHttpClient client = new OkHttpClient();
    protected Gson gson = new Gson();
    protected final String BASE_URL = "http://prog2.fh-campuswien.ac.at";
}
