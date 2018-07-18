package sy.com.initproject.root.ui.web;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.pince.frame.BaseActivity;

import sy.com.initproject.R;
import sy.com.initproject.databinding.ActivityWebBinding;

/**
 * @date：2018/7/18
 * @author: SyShare
 */
public class WebActivity extends BaseActivity<ActivityWebBinding> {

    private static final String EXTRA_URL = "extra_url";
    private static final String EXTRA_TITLE = "extra_title";
    private String mUrl, mTitle;

    /**
     * Using newIntent trick, return WebActivity Intent, to avoid `public static`
     * constant
     * variable everywhere
     *
     * @return Intent to start WebActivity
     */
    public static Intent newIntent(Context context, String extraURL, String extraTitle) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra(EXTRA_URL, extraURL);
        intent.putExtra(EXTRA_TITLE, extraTitle);
        return intent;
    }

    public static void open(Context context, String extraURL, String extraTitle) {
        context.startActivity(newIntent(context, extraURL, extraTitle));
    }

    @Override
    protected boolean checkData(Bundle bundle) {
        return true;
    }

    @Override
    protected int requestLayoutId() {
        return R.layout.activity_web;
    }

    @Override
    protected void initView(View contentView) {
        super.initView(contentView);
        mUrl = getIntent().getStringExtra(EXTRA_URL);
        mTitle = getIntent().getStringExtra(EXTRA_TITLE);

        WebSettings settings = mBinding.webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setAppCacheEnabled(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setSupportZoom(true);
        mBinding.webView.setWebChromeClient(new ChromeClient());
        mBinding.webView.setWebViewClient(new LoveClient());

        mBinding.webView.loadUrl(mUrl);
    }

    @Override
    protected void setViewData(Bundle savedInstanceState) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (mBinding.webView.canGoBack()) {
                        mBinding.webView.goBack();
                    } else {
                        finish();
                    }
                    return true;
                default:
                    return super.onKeyDown(keyCode, event);
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    private class ChromeClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            mBinding.progressbar.setProgress(newProgress);
            if (newProgress == 100) {
                mBinding.progressbar.setVisibility(View.GONE);
            } else {
                mBinding.progressbar.setVisibility(View.VISIBLE);
            }
        }


        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            setTitle(title);
        }
    }

    private class LoveClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url != null) view.loadUrl(url);
            return true;
        }
    }
}
