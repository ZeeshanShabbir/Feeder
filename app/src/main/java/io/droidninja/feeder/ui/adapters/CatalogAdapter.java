package io.droidninja.feeder.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.droidninja.feeder.R;
import io.droidninja.feeder.api.model.CatalogDTO;
import io.droidninja.feeder.api.model.SourceDTO;

/**
 * Created by Zeeshan on 2/7/17.
 */

public class CatalogAdapter extends RecyclerView.Adapter<CatalogAdapter.ViewHolder> {
    private List<SourceDTO> items = new ArrayList<SourceDTO>();
    private static final String TAG = "CatalogAdapter";
    private SelectedInterfaceListener mSelectedInterface;
    private List<SourceDTO> sourceDTOs = Collections.EMPTY_LIST;
    private Picasso picasso;

    public CatalogAdapter(Picasso picasso, SelectedInterfaceListener mSelectedInterface) {
        this.picasso = picasso;
        this.mSelectedInterface = mSelectedInterface;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.catelog_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.title.setText(sourceDTOs.get(position).getName());
        picasso.load(sourceDTOs.get(position).getUrlsToLogos().getMedium()).into(holder.logo);
        if (sourceDTOs.get(position).getSelected()) {
            holder.iv_selected.setVisibility(View.VISIBLE);
        } else {
            holder.iv_selected.setVisibility(View.GONE);
        }
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (items.contains(sourceDTOs.get(position))) {
                    items.remove(sourceDTOs.get(position));
                    holder.iv_selected.setVisibility(View.GONE);
                    sourceDTOs.get(position).setSelected(false);
                } else {
                    items.add(sourceDTOs.get(position));
                    holder.iv_selected.setVisibility(View.VISIBLE);
                    sourceDTOs.get(position).setSelected(true);
                }
                mSelectedInterface.onSelectedItems(items);
            }
        });
    }

    @Override
    public int getItemCount() {
        return sourceDTOs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.rl_catalog)
        RelativeLayout relativeLayout;
        @BindView(R.id.tv_title)
        TextView title;
        @BindView(R.id.iv_selected)
        ImageView iv_selected;
        @BindView(R.id.iv_logo)
        ImageView logo;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    public void swap(List<SourceDTO> data) {
        sourceDTOs = data;
        notifyDataSetChanged();
    }
}
