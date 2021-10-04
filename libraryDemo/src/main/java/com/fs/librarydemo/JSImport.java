package com.fs.librarydemo;

import android.util.Base64;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.InputStream;

public class JSImport {
    String JSfile;
    WebView webView;

    public JSImport(WebView webView) {
        this.webView = webView;
    }

    public WebView createwebview(InputStream file) {

        webView.getSettings().setJavaScriptEnabled(true);


        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                injectJS(file);
                super.onPageFinished(view, url);
            }
        });
        webView.loadUrl("https://www.google.com/");

        return webView;
    }

    private void injectJS(InputStream file) {
        try {
            InputStream inputStream = file;
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();
            String encoded = Base64.encodeToString(buffer, Base64.NO_WRAP);
            webView.loadUrl("javascript:(function() {" +
                    "var parent = document.getElementsByTagName('head').item(0);" +
                    "var script = document.createElement('script');" +
                    "script.type = 'text/javascript';" +
                    "script.innerHTML = window.atob('" + encoded + "');" +
                    "parent.appendChild(script)" +
                    "})()");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

