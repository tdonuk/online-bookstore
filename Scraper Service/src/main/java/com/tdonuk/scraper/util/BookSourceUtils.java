package com.tdonuk.scraper.util;

import com.tdonuk.scraper.model.BookSource;

public class BookSourceUtils {
    private BookSourceUtils() {}

    public static BookSource getFromText(String text) {
        for(BookSource source : BookSource.values()) {
            if(source.text().equals(text)) return source;
        }
        return null;
    }

}
