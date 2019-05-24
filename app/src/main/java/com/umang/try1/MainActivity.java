package com.umang.try1;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.cunoraz.gifview.library.GifView;

public class MainActivity extends AppCompatActivity {

    WebView mywebview;
    Configuration config;
    View imgloading;
    View tv;
    View tvload;
    private GifView gifView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gifView = (GifView) findViewById(R.id.pb);
        gifView.setGifResource(R.mipmap.redcube); //1 4 6

        mywebview = (WebView) findViewById(R.id.webview);
        imgloading = findViewById(R.id.img);
        tv = findViewById(R.id.tv);
        tvload = findViewById(R.id.tvload);
        config = getResources().getConfiguration();
        WebSettings webSettings = mywebview.getSettings();
        webSettings.setJavaScriptEnabled(true);

        mywebview.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

                gifView.setVisibility(View.VISIBLE);
                showgif();

            }


            @Override
            public void onPageFinished(WebView view, String url) {


            }
        });
        mywebview.loadUrl("http://www.intern.ecelldtu.in/");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                w.setStatusBarColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimaryDark));
            }
        }

    }

    public AlertDialog.Builder buildDialog(Context c) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("No Internet Connection");
        builder.setMessage("You need to have Mobile Data or wifi to access this. Press ok to Exit");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                finish();
                moveTaskToBack(true);
            }
        });

        return builder;
    }

    void showgif() {
        CountDownTimer timer = new CountDownTimer(14000, 14000) {
            @Override
            public void onTick(long l) {
            }

            @Override
            public void onFinish() {

                imgloading.setVisibility(View.GONE);
                tv.setVisibility(View.GONE);
                tvload.setVisibility(View.GONE);


                if (gifView.isPlaying())
                    gifView.pause();

                gifView.setVisibility(View.GONE);
                if (!isConnected(MainActivity.this)) {
                    buildDialog(MainActivity.this).show();
                    mywebview.setVisibility(View.GONE);
                } else
                    mywebview.setVisibility(View.VISIBLE);

            }
        };
        timer.start();
    }

    public boolean isConnected(Context context) {

        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if ((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting()))
                return true;
            else return false;
        } else
            return false;
    }

    private static long back_pressed;

    @Override
    public void onBackPressed() {


        if (back_pressed + 4000 > System.currentTimeMillis()) {

            super.onBackPressed();

        } else {
            if (mywebview.canGoBack())
                mywebview.goBack();

            Toast.makeText(getBaseContext(), "Press once again to exit! ", Toast.LENGTH_SHORT).show();
            back_pressed = System.currentTimeMillis();

        }

    }

}
