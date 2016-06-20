/**
 * Created by Ashwin on 19-Jun-16
 */
package io.github.ashwinwadte.popularmovies.models;

public class Video {
    String name;
    String key;

    public Video() {
    }

    public Video(String name, String key) {
        this.name = name;
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }
}
