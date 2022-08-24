package systems.kiznaiver.webappp;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;


import systems.kiznaiver.webappp.R;

public class MainActivity extends Activity {

    public static final String EXTRA_URL = "<Change me>";
    WebView myWebview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myWebview = (WebView) this.findViewById(R.id.webz);
        myWebview.getSettings().setJavaScriptEnabled(true);
        myWebview.setWebViewClient(new WebViewClient());
        myWebview.loadUrl(EXTRA_URL);
    }
}
