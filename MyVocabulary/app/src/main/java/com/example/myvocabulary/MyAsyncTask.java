package com.example.myvocabulary;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyAsyncTask extends AppCompatActivity{
    private static final String TAG = "Log";
    private Drawable drawable = null;
    private LinearLayout background = null;
    private String url;

    public void executeTask(LinearLayout ll){
        background = ll;
        url = "https://i.imgur.com/Ry9UfrG.png";
        new myTask().execute();
    }

    // NetworkOnMainThreadException 오류 해결 AsyncTask
    public class myTask extends AsyncTask<Integer, Void, Integer> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //show a progress bar
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            try {
                drawable  =  drawableFromUrl(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return 0;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            if (background != null){
                background.setBackground(drawable);
            }
        }
    }

    // url -> drawable (background 설정을 위함)
    public Drawable drawableFromUrl(String url) throws IOException {
        Bitmap x;

        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.connect();
        InputStream input = connection.getInputStream();

        x = BitmapFactory.decodeStream(input);
        return new BitmapDrawable(Resources.getSystem(), x);
    }
}
