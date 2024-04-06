package at.ac.fhcampuswien.fhmdb.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Movie {
    @SerializedName("title") // This annotation is optional if the names match.
    private final String title;

    @SerializedName("description")
    private final String description;

    @SerializedName("genres")
    private final List<String> genres;

    // Additional fields that might be included in the API response
    @SerializedName("id")
    private final String id;

    @SerializedName("releaseYear")
    private final int releaseDate;

    @SerializedName("rating")
    private final double rating;

    public Movie(String id, String title, String description, List<String> genres, int releaseDate, double rating) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.genres = genres;
        this.releaseDate = releaseDate;
        this.rating = rating;
    }

    // Getters and possibly setters if fields should be mutable
    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getGenres() {
        return genres;
    }

    public int getReleaseDate() {
        return releaseDate;
    }

    public double getRating() {
        return rating;
    }

    // Override equals, hashCode, and toString methods as needed
}

