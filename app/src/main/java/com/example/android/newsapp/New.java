package com.example.android.newsapp;

import android.content.Context;

/**
 * Created by Viper on 07-May-18.
 */

public class New {

    //gives unique ID to every article
    private static int mId = 0;
    //New's title
    private String mTitle;
    //New's section
    private String mSection;
    //New's publish date and time
    private String mDate;
    //New's URL
    private String mWebSite;

    //New's author
    private String mAuthor;

    /**
     * Construct a new {@link New} object
     *
     * @param title   is the title of the article
     * @param section is the category that the article falls into
     * @param date    is the publish date and time
     * @param webSite is the URL that links o the article
     * @param author is the author of the article
     */
    public New(String title, String section, String date, String webSite, String author) {
        mTitle = title;
        mSection = section;
        mDate = date;
        mWebSite = webSite;
        mAuthor = author;
        mId++;
    }

    /**
     * @return the title of the article
     */
    public String getTitle() {
        return mTitle;
    }

    /**
     * @return the section of the article
     */
    public String getSection() {
        return mSection;
    }

    /**
     * @return date and time of publish of the article
     */
    public String getDate() {
        return mDate;
    }

    /**
     * @return the URL of the article
     */
    public String getWebSite() {
        return mWebSite;
    }

    /**
     * @return the URL of the article
     */
    public String getAuthor() {
        return mAuthor;
    }

    /**
     * @return the true if the article has author
     */
    public boolean hasAuthor() {
        return mAuthor != "";
    }

    /**
     * @return the ID of the article
     */
    public int getId() {
        return mId;
    }
}
