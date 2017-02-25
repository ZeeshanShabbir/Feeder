package io.droidninja.feeder.ui.fragments;

import android.widget.ImageView;

/**
 * Created by Zeeshan on 2/21/17.
 */

public interface FeedListener {
    void openFeed(String title, String description, String urlToImage, String url, ImageView cover, String transitionName);
}
