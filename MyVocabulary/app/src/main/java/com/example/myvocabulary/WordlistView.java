package com.example.myvocabulary;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class WordlistView extends LinearLayout {

    TextView textView, textView2;
    ImageView imageView;

    private ImageButton delete;

    public WordlistView(Context context) {
        super(context);
        init(context);
    }

    public WordlistView(Context context, @Nullable AttributeSet attrs){
        super(context, attrs);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.list_word, this, true);

        textView = findViewById(R.id.word);
        textView2 = findViewById(R.id.mean);
        imageView = findViewById(R.id.btn_delete);


    }

    public void setName(String word){
        textView.setText(word);
    }

    public void setSubname(String mean){
        textView2.setText(mean);
    }

    public void setWord(int resId){
        imageView.setImageResource(resId);
    }
}
