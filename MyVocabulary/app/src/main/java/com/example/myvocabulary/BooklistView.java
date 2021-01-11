package com.example.myvocabulary;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

public class BooklistView extends LinearLayout {

    TextView textView, textView2;
    ImageView imageView;

    public BooklistView(Context context) {
        super(context);
        init(context);
    }

    public BooklistView(Context context, @Nullable AttributeSet attrs){
        super(context, attrs);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.list_book, this, true);

        textView = findViewById(R.id.name);
        textView2 = findViewById(R.id.subname);
        imageView = findViewById(R.id.book);
    }

    public void setName(String name){
        textView.setText(name);
    }

    public void setSubname(String subname){
        textView2.setText(subname);
    }

    public void setBook(String url){
        Glide.with(this).load(url).into(imageView);
    }
}
