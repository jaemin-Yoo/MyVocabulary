package com.example.myvocabulary;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class WordActivity extends AppCompatActivity {

    public String TAG = "MyVocabulary";
    private ImageButton back, home, register;
    private ImageView word, mean;
    private TextView title;
    private String bk_name = BookActivity.bk_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wordlist);

        title = findViewById(R.id.title);
        title.setText(bk_name); // 단어장 이름

        back=findViewById(R.id.btn_back);
        Glide.with(this).load(R.drawable.back).into(back); // 이미지 로드
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        home = findViewById(R.id.btn_home);
        Glide.with(this).load(R.drawable.home).into(home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 홈화면으로 돌아가기
            }
        });

        word = findViewById(R.id.word_text);
        Glide.with(this).load(R.drawable.word_text).into(word);

        mean = findViewById(R.id.mean_text);
        Glide.with(this).load(R.drawable.mean_text).into(mean);

        register = findViewById(R.id.btn_register);
        Glide.with(this).load(R.drawable.register).into(register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 단어 등록
                // SQLite 접근
            }
        });

    }
}
