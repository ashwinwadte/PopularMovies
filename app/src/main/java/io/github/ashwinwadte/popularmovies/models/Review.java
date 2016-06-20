/**
 * Created by Ashwin on 19-Jun-16
 */

package io.github.ashwinwadte.popularmovies.models;

public class Review {
    String author;
    String content;

    public Review() {
    }

    public Review(String author, String content) {
        this.author = author;
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }
}
