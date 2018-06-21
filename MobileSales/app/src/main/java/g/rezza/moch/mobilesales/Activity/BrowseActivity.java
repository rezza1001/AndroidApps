package g.rezza.moch.mobilesales.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import g.rezza.moch.mobilesales.R;

public class BrowseActivity extends AppCompatActivity {

    private String      url = "https://dev.faspay.co.id/pws/100003/0830000010100000/e91ab30f24e8f035c10b8d000e69608e28a49245?trx_id=3209240600000083&merchant_id=32092&bill_no=120320180110057";
    private WebView     webView;
    private ProgressBar progressBar;
    private TextView    txvw_hdr_00;
    private float       m_downX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);

        url = getIntent().getStringExtra("URL");
        if (TextUtils.isEmpty(url)) {
            finish();
        }

        webView             = (WebView)     findViewById(R.id.webView);
        progressBar         = (ProgressBar) findViewById(R.id.progressBar);
        txvw_hdr_00         = (TextView)    findViewById(R.id.txvw_hdr_00);

        txvw_hdr_00.setText(getResources().getString(R.string.payment));
        initWebView();
        webView.loadUrl(url);
    }

    private void initWebView() {
        webView.setWebChromeClient(new MyWebChromeClient(this));
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
                invalidateOptionsMenu();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webView.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
                invalidateOptionsMenu();
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                progressBar.setVisibility(View.GONE);
                invalidateOptionsMenu();
            }
        });
        webView.clearCache(true);
        webView.clearHistory();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setHorizontalScrollBarEnabled(false);
        webView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getPointerCount() > 1) {
                    //Multi touch detected
                    return true;
                }

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        // save the x
                        m_downX = event.getX();
                    }
                    break;
                    case MotionEvent.ACTION_MOVE:
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP: {
                        // set x so that it doesn't move
                        event.setLocation(m_downX, event.getY());
                    }
                    break;
                }

                return false;
            }
        });

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.d("WEB_VIEW", url);
            }

        });


    }

    // backward the browser navigation
    private void back() {
        if (webView.canGoBack()) {
            webView.goBack();
        }
    }

    // forward the browser navigation
    private void forward() {
        if (webView.canGoForward()) {
            webView.goForward();
        }
    }

    private class MyWebChromeClient extends WebChromeClient {
        Context context;

        public MyWebChromeClient(Context context) {
            super();
            this.context = context;
        }
    }

    @Override
    public void onBackPressed() {
//        Intent intent = new Intent(this, HomeActivity.class);
//        startActivity(intent);
        this.finish();
    }
}
