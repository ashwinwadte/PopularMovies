package io.github.ashwinwadte.popularmovies.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Ashwin on 06-May-16.
 */
public class Utils {
    public static String formatDate(String date) {
        final String DATE_FORMAT = "yyyy-MM-dd";
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT, Locale.US);

        try {
            Date parsedDate = format.parse(date);
            return DateFormat.getDateInstance().format(parsedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }
}
