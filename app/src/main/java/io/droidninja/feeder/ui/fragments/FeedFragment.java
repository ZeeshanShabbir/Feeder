package io.droidninja.feeder.ui.fragments;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.droidninja.feeder.R;

/**
 * Created by Zeeshan on 2/12/17.
 */

public class FeedFragment extends Fragment {
    private static final String ARG_PARAM1 = "title";
    private static final String ARG_PARAM2 = "description";
    private static final String ARG_PARAM3 = "url";
    private static final String ARG_PARAM4 = "urlToImage";
    private static final String ARG_PARAM5 = "transitionName";

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_description)
    TextView tvDescription;
    @BindView(R.id.iv_cover)
    ImageView ivCover;

    public static FeedFragment newInstance(String title, String description, String url, String urlToImage, String transitionName) {
        FeedFragment fragment = new FeedFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, title);
        args.putString(ARG_PARAM2, description);
        args.putString(ARG_PARAM3, url);
        args.putString(ARG_PARAM4, urlToImage);
        args.putString(ARG_PARAM5, transitionName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postponeEnterTransition();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.feed_fragment, container, false);
        ButterKnife.bind(this, view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ivCover.setTransitionName(getArguments().getString(ARG_PARAM5));
        }
        loadData();
        return view;
    }

    private void loadData() {
        String title = null;
        String description = null;
        String urlToImage = null;
        String url = null;
        try {
            title = getArguments().getString(ARG_PARAM1);
            description = getArguments().getString(ARG_PARAM2);
            url = getArguments().getString(ARG_PARAM3);
            urlToImage = getArguments().getString(ARG_PARAM4);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (title != null)
            tvTitle.setText(title);
        if (tvDescription != null)
            tvDescription.setText(description);
        if (urlToImage != null)
            Picasso.with(getActivity()).load(urlToImage).into(ivCover, new Callback() {
                @Override
                public void onSuccess() {
                    startPostponedEnterTransition();
                }

                @Override
                public void onError() {
                    startPostponedEnterTransition();
                }
            });
    }
}
