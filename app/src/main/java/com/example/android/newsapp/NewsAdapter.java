package com.example.android.newsapp;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Viper on 07-May-18.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    //List of news
    private List<New> mNews;

    private Context mContext;

    /**
     * @param news pass the list of news to the constructor
     */
    public NewsAdapter(List<New> news) {
        mNews = news;
    }

    @NonNull
    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context mContext = parent.getContext();
        LayoutInflater mNewsInflater = LayoutInflater.from(mContext);

        //Inflate the custom layout
        View newsView = mNewsInflater.inflate(R.layout.new_item, parent, false);

        //Return a new holder instance
        ViewHolder mNewsHolder = new ViewHolder(newsView);
        return mNewsHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.ViewHolder holder, int position) {
        //Create an {@link New} object that holds the current new
        New currentNew = mNews.get(position);

        //Sets the title of the current new
        TextView currentTitle = holder.titleTextView;
        currentTitle.setText(currentNew.getTitle());

        //Sets the section of the current new
        TextView currentSection = holder.sectionTextView;
        currentSection.setText(currentNew.getSection());

        //Sets the author of the current new
        TextView currentAuthor = holder.authorTextView;

        mContext = holder.authorTextView.getContext();
        String authorString = "";

        if(currentNew.hasAuthor()){
            authorString = mContext.getString(R.string.by_author);
            currentAuthor.setText( authorString + " " + currentNew.getAuthor());
        }else{
            currentAuthor.setVisibility(View.GONE);
        }

        //Holds the date and time from JSON response
        String currentDate = currentNew.getDate();

        //The format of the date provided by JSON response
        SimpleDateFormat inputDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date dateObject;
        String outputDate;
        String outputTime;

        try {
            //Parse the String date to Date object
            dateObject = inputDate.parse(currentDate);
            //Format the date (i.e. May 09, 2018)
            outputDate = formatDate(dateObject);
            //Sets the date of the current new
            TextView publishDate = holder.dateTextView;
            publishDate.setText(outputDate);

            //Format the time (i.e. 3:00 PM)
            outputTime = formatTime(dateObject);
            //Sets the time of the current new
            TextView publishTime = holder.timeTextView;
            publishTime.setText(outputTime);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    // Returns the total count of items in the list;
    @Override
    public int getItemCount() {
        return mNews.size();
    }

    /**
     * @param date pass the date that should be formatted
     * @return formatted date as a String
     */
    private String formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(date);
    }

    /**
     * @param date pass the date that should be formatted
     * @return formatted time as a String
     */
    private String formatTime(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm a");
        return dateFormat.format(date);
    }

    public void swapItems(List<New> news) {

        if (mNews != null) {
            mNews.clear();
        }

        // compute diffs
        final NewsDiffCallback diffCallback = new NewsDiffCallback(mNews, news);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        // clear news and add
        if (mNews != null) {
            mNews.clear();
        }
        mNews = news;

        diffResult.dispatchUpdatesTo(this); // calls adapter's notify methods after diff is computed
        notifyDataSetChanged();
    }

    public void clearAdapter(List<New> news) {
        if (mNews != null) {
            mNews.clear();
        }
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        //Create an instance of every variable in {@link New} class
        public TextView titleTextView;
        public TextView sectionTextView;
        public TextView dateTextView;
        public TextView timeTextView;
        public TextView authorTextView;

        /**
         * Constructor
         *
         * @param itemView accepts the entire item row
         *                 and does the view lookups to find each subview
         */
        public ViewHolder(View itemView) {
            super(itemView);

            //Finds TextView with ID title in new_item.xml
            titleTextView = (TextView) itemView.findViewById(R.id.title);
            //Finds TextView with ID section in new_item.xml
            sectionTextView = (TextView) itemView.findViewById(R.id.section);
            //Finds TextView with ID date in new_item.xml
            dateTextView = (TextView) itemView.findViewById(R.id.date);
            //Finds TextView with ID time in new_item.xml
            timeTextView = (TextView) itemView.findViewById(R.id.time);
            //Finds TextView with ID author in new_item.xml
            authorTextView = (TextView) itemView.findViewById(R.id.author);
        }
    }
}
