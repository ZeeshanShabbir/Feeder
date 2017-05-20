package io.droidninja.feeder.ui.activities;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import io.droidninja.feeder.R;

public class ArticleActivity extends AppCompatActivity {
    @BindView(R.id.wv_article)
    WebView webView;
    String url = null;
    @BindView(R.id.pb)
    SmoothProgressBar smoothProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            url = bundle.getString("url");
        }

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
            }

            @Override
            public void onPageStarted(WebView view, String url1, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                //if (url1.contains(url)) {
                    smoothProgressBar.setVisibility(View.VISIBLE);
                    // TODO: 2/25/17 show progress bar
                //} else {
                    //Toast.makeText(ArticleActivity.this, "Sorry, you can't browser here", Toast.LENGTH_SHORT).show();
                    //onBackPressed();
                //}
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                smoothProgressBar.setVisibility(View.INVISIBLE);
            }
        });

        if (url != null)
            webView.loadUrl(url);

    }
}
