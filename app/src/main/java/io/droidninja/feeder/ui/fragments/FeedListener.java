package io.droidninja.feeder.ui.fragments;

import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Zeeshan on 2/21/17.
 */

public interface FeedListener {
    void openFeed(String title, String source, String description, String urlToImage, String url, ImageView cover, String transitionName, TextView titletv, String transitionText,TextView sourcetv, String sourceText);
}
