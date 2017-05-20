package io.droidninja.feeder.ui.fragments;

import android.content.Intent;
import android.graphics.Typeface;
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
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.droidninja.feeder.R;
import io.droidninja.feeder.ui.activities.ArticleActivity;
import io.droidninja.feeder.util.Helper;

/**
 * Created by Zeeshan on 2/12/17.
 */

public class FeedFragment extends Fragment {
    private static final String ARG_PARAM1 = "title";
    private static final String ARG_PARAM2 = "description";
    private static final String ARG_PARAM3 = "url";
    private static final String ARG_PARAM4 = "urlToImage";
    private static final String ARG_PARAM5 = "transitionName";
    private static final String ARG_PARAM6 = "transitionText";
    private static final String ARG_PARAM7 = "sourceText";
    private static final String ARG_PARAM8 = "source";

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_description)
    TextView tvDescription;
    @BindView(R.id.iv_cover)
    ImageView ivCover;
    @BindView(R.id.tv_app_name)
    TextView appName;
    @BindView(R.id.adView)
    AdView mAdView;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    String url = null;


    public static FeedFragment newInstance(String title, String description, String url, String urlToImage, String transitionName, String transitionText, String sourceText, String source) {
        FeedFragment fragment = new FeedFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, title);
        args.putString(ARG_PARAM2, description);
        args.putString(ARG_PARAM3, url);
        args.putString(ARG_PARAM4, urlToImage);
        args.putString(ARG_PARAM5, transitionName);
        args.putString(ARG_PARAM6, transitionText);
        args.putString(ARG_PARAM7, sourceText);
        args.putString(ARG_PARAM8, source);
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
        loadad();
        Typeface typeface = Typeface.createFromAsset(getActivity().getResources().getAssets(), "fonts/Miller-Display.otf");
        Typeface typeface1 = Typeface.createFromAsset(getActivity().getResources().getAssets(), "fonts/Miller-Text.otf");
        appName.setTypeface(typeface);
        tvTitle.setTypeface(typeface);
        tvDescription.setTypeface(typeface1);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ivCover.setTransitionName(getArguments().getString(ARG_PARAM5));
            tvTitle.setTransitionName(getArguments().getString(ARG_PARAM6));
            appName.setTransitionName(getArguments().getString(ARG_PARAM7));
        }
        loadData();
        return view;
    }

    private void loadad() {
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private void loadData() {
        String title = null;
        String description = null;
        String urlToImage = null;
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
        appName.setText(getArguments().getString(ARG_PARAM8));
    }


    @OnClick(R.id.btn_story)
    public void openLink() {
        if (url != null) {
            if(Helper.isConnected(getActivity())) {
                startActivity(new Intent(getActivity(), ArticleActivity.class).putExtra("url", url));
            }else {
                Toast.makeText(getActivity(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), "Sorry, no link available", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.iv_back)
    public void onBack() {
        getActivity().onBackPressed();
    }
}
