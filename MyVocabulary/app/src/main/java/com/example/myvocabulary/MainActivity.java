package com.example.myvocabulary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.bumptech.glide.Glide;

public class MainActivity extends AppCompatActivity {

    private ImageButton add, test, exit;
    public static int state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        add=findViewById(R.id.btn_add);
        Glide.with(this).load("https://i.imgur.com/aZFNhx0.png").into(add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                state = 0;
                Log.d("click", "Add");
                Intent intent = new Intent(MainActivity.this, BookActivity.class);
                startActivity(intent);
            }
        });

        test=findViewById(R.id.btn_test);
        Glide.with(this).load("https://i.imgur.com/Bz1lFfd.png").into(test);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                state = 1;
                Log.d("click", "Test");
                Intent intent = new Intent(MainActivity.this, BookActivity.class);
                startActivity(intent);
            }
        });

        exit=findViewById(R.id.btn_exit);
        Glide.with(this).load("https://i.imgur.com/vHjbVf6.png").into(exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("click", "Exit");
                finish();
            }
        });

    }
}