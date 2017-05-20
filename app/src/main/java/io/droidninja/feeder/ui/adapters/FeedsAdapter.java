package io.droidninja.feeder.ui.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.droidninja.feeder.R;
import io.droidninja.feeder.contentProvider.FeederDbHelper;
import io.droidninja.feeder.ui.fragments.FeedListener;
import io.droidninja.feeder.ui.fragments.FeedsFragment;

/**
 * Created by Zeeshan on 2/15/17.
 */

public class FeedsAdapter extends RecyclerView.Adapter<FeedsAdapter.ViewHolder> {
    Cursor mCursor;
    @Inject
    Picasso picasso;

    FeedListener feedListener;

    public FeedsAdapter(FeedListener mfeedListener) {
        this.feedListener = mfeedListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        mCursor.moveToPosition(position);
        String title = null;
        String description = null;
        String urlToImage = null;
        String url = null;
        Typeface typeface = Typeface.createFromAsset(holder.tvTittle.getContext().getResources().getAssets(), "fonts/Miller-Display.otf");
        Typeface typeface2 = Typeface.createFromAsset(holder.tvTittle.getContext().getResources().getAssets(), "fonts/american_fonts.ttf");
        holder.tvTittle.setTypeface(typeface);
        try {
            title = mCursor.getString(FeedsFragment.INDEX_ARITICLE_TITLE);
            description = mCursor.getString(FeedsFragment.INDEX_ARTICLE_DESCRIPTION);
            urlToImage = mCursor.getString(FeedsFragment.INDEX_ARTICLE_URL_TO_IMAGE);
            url = mCursor.getString(FeedsFragment.INDEX_ARTICLE_URL);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (title != null)
            holder.tvTittle.setText(title);
        if (urlToImage != null) {
            final String finalUrlToImage1 = urlToImage;
            picasso.with(holder.ivCover.getContext())
                    .load(urlToImage)
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .into(holder.ivCover, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            picasso.with(holder.ivCover.getContext())
                            .load(finalUrlToImage1)
                            .into(holder.ivCover);
                        }
                    });
        }
        holder.tvSource.setTypeface(typeface);
        final String source = mCursor.getString(FeedsFragment.INDEX_ARTICLE_SOURCE).replace("-", " ").toUpperCase();

        holder.tvSource.setText(source);
        final String finalTitle = title;
        final String finalDescription = description;
        final String finalUrlToImage = urlToImage;
        final String finalUrl = url;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.ivCover.setTransitionName("trans_image" + position);
            holder.tvTittle.setTransitionName("trans_title" + position);
            holder.tvSource.setTransitionName("trans_source" + position);
        }
        holder.ivCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feedListener.openFeed(finalTitle, source, finalDescription, finalUrlToImage, finalUrl, holder.ivCover, "trans_image" + position, holder.tvTittle, "trans_title" + position, holder.tvSource, "trans_source" + position);
            }
        });


    }

    @Override
    public int getItemCount() {
        if (mCursor == null) return 0;
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_title)
        TextView tvTittle;
        @BindView(R.id.iv_cover)
        ImageView ivCover;
        @BindView(R.id.tv_source)
        TextView tvSource;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
