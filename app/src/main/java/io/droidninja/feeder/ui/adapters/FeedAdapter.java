package io.droidninja.feeder.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.droidninja.feeder.FeederApplication;
import io.droidninja.feeder.R;
import io.droidninja.feeder.api.model.FeedsDTO;

/**
 * Created by Zeeshan on 2/15/17.
 */

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder> {
    FeedsDTO feedsDTO;

    public FeedAdapter(FeedsDTO feedsDTO) {
        this.feedsDTO = feedsDTO;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvTittle.setText(feedsDTO.getArticle().get(position).getTitle());
        Picasso.with(holder.ivCover.getContext()).load(feedsDTO.getArticle().get(position).getUrlToImage()).into(holder.ivCover);
    }

    @Override
    public int getItemCount() {
        return feedsDTO.getArticle().size();
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
