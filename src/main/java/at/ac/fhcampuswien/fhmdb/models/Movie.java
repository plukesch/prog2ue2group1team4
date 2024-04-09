package at.ac.fhcampuswien.fhmdb.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Movie {
    public final String title;
    public final String description;
    public final List<Genre> genres;
    public final List<String> mainCast; // Hinzugefügt für die Schauspieler
    public final String director; // Hinzugefügt für den Regisseur
    public final int releaseYear; // Hinzugefügt für das Veröffentlichungsjahr
    public final double rating;

    // Aktualisierter Konstruktor
    public Movie(String title, String description, List<Genre> genres, List<String> mainCast, String director, int releaseYear, double rating) {
        this.title = title;
        this.description = description;
        this.genres = genres;
        this.mainCast = mainCast;
        this.director = director;
        this.releaseYear = releaseYear;
        this.rating = rating;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) {
            return false;
        }
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof Movie other)) {
            return false;
        }
        return this.title.equals(other.title) && this.description.equals(other.description) && this.genres.equals(other.genres);
    }

    // Getter
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public List<String> getMainCast() {
        return mainCast;
    }

    public String getDirector() {
        return director;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public double getRating(){
        return rating;
    }
}
