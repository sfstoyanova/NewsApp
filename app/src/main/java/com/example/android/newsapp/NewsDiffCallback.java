package com.example.android.newsapp;

import android.support.v7.util.DiffUtil.Callback;

import java.util.List;

/**
 * Created by Viper on 14-May-18.
 */

public class NewsDiffCallback extends Callback {

    //old list of news
    private List<New> mOldList;

    //new list of news
    private List<New> mNewList;

    public NewsDiffCallback(List<New> oldList, List<New> newList) {
        mOldList = oldList;
        mNewList = newList;
    }

    @Override
    public int getOldListSize() {
        return mOldList.size();
    }

    @Override
    public int getNewListSize() {
        return mNewList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldList.get(oldItemPosition).getId() == mNewList.get(newItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        New oldNew = mOldList.get(oldItemPosition);
        New latestNew = mOldList.get(newItemPosition);

        if (oldNew.getTitle() == latestNew.getTitle() && oldNew.getSection() == latestNew.getSection() &&
                oldNew.getDate() == latestNew.getDate() && oldNew.getWebSite() == latestNew.getWebSite() &&
                oldNew.getAuthor() == latestNew.getAuthor()) {
            return true;
        }
        return false;
    }
}
