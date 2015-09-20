package org.houxg.pixiurss.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import org.houxg.pixiurss.R;
import org.houxg.pixiurss.ui.base.BaseActivity;

import butterknife.Bind;

/**
 * desc:
 * <br>
 * author: houxg
 * <br>
 * create on 2015/9/5
 */
public class ArticleWebActivity extends BaseActivity {

    @Bind(R.id.web)
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String url = getIntent().getStringExtra("url");
        if (TextUtils.isEmpty(url)) {
            finish();
            return;
        }
        initWebView();
        webView.loadUrl(url);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_article_web;
    }

    public static Bundle getOpenBundle(String url) {
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        return bundle;
    }

    private void initWebView() {
        WebSettings settings = webView.getSettings();

        settings.setJavaScriptEnabled(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        settings.setSupportZoom(true);
        settings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        webView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
                        webView.goBack();
                        return true;
                    }
                }
                return false;
            }
        });
    }
}
