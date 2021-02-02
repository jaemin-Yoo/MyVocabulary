package com.example.myvocabulary;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

public class TestActivity extends AppCompatActivity {

    private ImageButton back, home, all, pri, del;
    private LinearLayout background;
    public static int test_state;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testlist);

        background = findViewById(R.id.background);
        Glide.with(TestActivity.this).asBitmap().load("https://i.imgur.com/Ry9UfrG.png").into(new CustomTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                background.setBackground(new BitmapDrawable(getResources(), resource));
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {

            }
        });

        back=findViewById(R.id.btn_back);
        Glide.with(this).load("https://i.imgur.com/iYM8Gc1.png").into(back); // 이미지 로드
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        home = findViewById(R.id.btn_home);
        Glide.with(this).load("https://i.imgur.com/BmGIFS3.png").into(home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 홈화면으로 돌아가기
                Intent intent = new Intent(TestActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //액티비티 스택제거
                startActivity(intent);
            }
        });

        all=findViewById(R.id.all_test);
        Glide.with(this).load("https://i.imgur.com/gp4T8CE.png").into(all);
        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 전체 단어 테스트
                test_state = 0;
                Intent intent = new Intent(TestActivity.this, BookActivity.class);
                startActivity(intent);
            }
        });

        pri=findViewById(R.id.priority_test);
        Glide.with(this).load("https://i.imgur.com/176BLtv.png").into(pri);
        pri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 우선순위 50개 테스트
                test_state = 1;
                Intent intent = new Intent(TestActivity.this, BookActivity.class);
                startActivity(intent);
            }
        });

        del=findViewById(R.id.delete_test);
        Glide.with(this).load("https://i.imgur.com/SMW5QMy.png").into(del);
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 삭제 테스트
                test_state = 2;
                Intent intent = new Intent(TestActivity.this, BookActivity.class);
                startActivity(intent);
            }
        });
    }
}
