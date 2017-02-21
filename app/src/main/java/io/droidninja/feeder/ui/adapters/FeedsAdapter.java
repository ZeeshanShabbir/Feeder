package io.droidninja.feeder.ui.adapters;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.droidninja.feeder.R;
import io.droidninja.feeder.ui.fragments.FeedsFragment;

/**
 * Created by Zeeshan on 2/15/17.
 */

public class FeedsAdapter extends RecyclerView.Adapter<FeedsAdapter.ViewHolder> {
    Cursor mCursor;

    public FeedsAdapter() {
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        String title = null;
        String description = null;
        String urlToImage = null;
        try {
            title = mCursor.getString(FeedsFragment.INDEX_ARITICLE_TITLE);
            description = mCursor.getString(FeedsFragment.INDEX_ARTICLE_DESCRIPTION);
            urlToImage = mCursor.getString(FeedsFragment.INDEX_ARTICLE_URL_TO_IMAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (title != null)
            holder.tvTittle.setText(title);
        if (urlToImage != null)
            Picasso.with(holder.ivCover.getContext()).load(urlToImage).into(holder.ivCover);
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

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
