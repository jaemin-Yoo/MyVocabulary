package com.example.myvocabulary;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.ArrayList;

public class WordActivity extends AppCompatActivity {

    public String TAG = "Log";
    private ImageButton back, home, register, delete, many_word, priority, sort;
    private ImageView word, mean;
    private TextView title, count;
    private String bk_name = BookActivity.bk_name;
    private ListView listView;
    private String add_word, add_mean;
    private LinearLayout background;
    private Wordlist wl;
    private String select_name, select_subname, modify_name, modify_subname;
    private int sort_state = 0;

    private MyAsyncTask myAsyncTask = new MyAsyncTask();
    SQLiteDatabase bookDB = null;
    private final String dbname = "MyVocabulary";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wordlist);

        background = findViewById(R.id.background);
        //myAsyncTask.executeTask(background); // 백그라운드 url 설정
        Glide.with(WordActivity.this).asBitmap().load("https://i.imgur.com/Ry9UfrG.png").into(new CustomTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                background.setBackground(new BitmapDrawable(getResources(), resource));
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {

            }
        });

        title = findViewById(R.id.title);
        title.setText(bk_name); // 단어장 이름

        bookDB = this.openOrCreateDatabase(dbname, MODE_PRIVATE, null);

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
                Intent intent = new Intent(WordActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //액티비티 스택제거
                startActivity(intent);
            }
        });

        listView = findViewById(R.id.word_list);
        listView.setFocusable(false); // 리스트뷰와 리스트뷰 내 아이템 둘 다 클릭 가능하도록 설정
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                wl = (Wordlist) listView.getAdapter().getItem(i);
                select_name = wl.getName();
                select_subname = wl.getSubname();
                modifyFunction();
            }
        });

        word = findViewById(R.id.word_text);
        Glide.with(this).load("https://i.imgur.com/9fWB5g6.png").into(word);

        mean = findViewById(R.id.mean_text);
        Glide.with(this).load("https://i.imgur.com/8panHgJ.png").into(mean);

        register = findViewById(R.id.btn_register);
        Glide.with(this).load("https://i.imgur.com/3ah17I3.png").into(register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 단어 등록
                // SQLite 접근

                final EditText et_word = (EditText) findViewById(R.id.edit_word);
                final EditText et_mean = (EditText) findViewById(R.id.edit_mean);

                add_word = et_word.getText().toString();
                add_mean = et_mean.getText().toString();

                if(add_word.length() != 0 && add_mean.length() != 0){
                    try{
                        bookDB.execSQL("INSERT INTO "+bk_name+" VALUES('"+add_word+"','"+add_mean+"',0);"); // SQLite 단어 추가
                        updateListView(sort_state);
                        Log.d(TAG, "추가한 단어:"+add_word);
                    } catch (Exception e){
                        Toast.makeText(getApplicationContext(),"중복된 단어가 존재합니다.",Toast.LENGTH_LONG).show();

                        Cursor cursor = bookDB.rawQuery("SELECT * FROM "+bk_name+" WHERE word='"+add_word+"'",null);

                        if(cursor!=null){
                            if(cursor.moveToFirst()){
                                do{
                                    select_name = cursor.getString(cursor.getColumnIndex("word"));
                                    select_subname = cursor.getString(cursor.getColumnIndex("mean"));
                                } while (cursor.moveToNext());
                            }
                        }
                        modifyFunction();
                    }
                    et_word.setText(""); // 추가 후 EditText 공백처리
                    et_mean.setText("");
                    et_word.requestFocus();
                    et_word.setCursorVisible(true);
                }
                else{
                    Toast.makeText(getApplicationContext(), "빈칸을 채우세요.",Toast.LENGTH_SHORT).show();
                }
            }
        });

        // a : "https://i.imgur.com/OzrIkQq.png"
        // z : https://i.imgur.com/LGwcFGr.png
        // sort : https://i.imgur.com/xVbYlTz.png

        sort = findViewById(R.id.btn_sort);
        Glide.with(this).load("https://i.imgur.com/xVbYlTz.png").into(sort);
        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(sort_state){
                    case 0:
                        Glide.with(getApplicationContext()).load("https://i.imgur.com/OzrIkQq.png").into(sort); // a 정렬
                        sort_state = 1;
                        break;
                    case 1:
                        Glide.with(getApplicationContext()).load("https://i.imgur.com/LGwcFGr.png").into(sort); // z 정렬
                        sort_state = 2;

                        break;
                    case 2:
                        Glide.with(getApplicationContext()).load("https://i.imgur.com/xVbYlTz.png").into(sort); // 기본 정렬
                        sort_state = 0;
                        break;
                }
                updateListView(sort_state);
            }
        });

        updateListView(sort_state);

        many_word = findViewById(R.id.add_many);
        Glide.with(this).load("https://i.imgur.com/oBWlKev.png").into(many_word);
        many_word.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 한꺼번에 단어추가
                LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE); // 커스텀
                LinearLayout et_many = (LinearLayout) vi.inflate(R.layout.edit_many_box, null);

                ImageView dialog_icon = et_many.findViewById(R.id.edit_many_icon);

                Glide.with(getApplicationContext()).load("https://i.imgur.com/Kxz19ZG.png").into(dialog_icon);

                final EditText et_word = et_many.findViewById(R.id.edit_many_word);

                final AlertDialog.Builder ad = new AlertDialog.Builder(WordActivity.this);

                ad.setView(et_many);
                Log.d(TAG,"OK");

                // 확인 버튼 설정
                ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String total = et_word.getText().toString();
                        Log.d(TAG,total);

                        try{
                            String[] line_break = total.split("\n");
                            int word_cnt = line_break.length;
                            int state = 0;
                            for(int i=0; i<word_cnt; i++){
                                String[] slush = line_break[i].split("/");
                                Log.d(TAG,line_break[i]);
                                add_word = slush[0];
                                add_mean = slush[1];

                                if(add_word.length() != 0 && add_mean.length() != 0){
                                    try{
                                        bookDB.execSQL("INSERT INTO "+bk_name+" VALUES('"+add_word+"','"+add_mean+"',0);"); // SQLite 단어 추가
                                        state++;
                                    } catch (Exception e){
                                        Toast.makeText(getApplicationContext(),"중복된 단어가 존재합니다.",Toast.LENGTH_LONG).show();
                                        break;
                                    }
                                }
                                else{
                                    Toast.makeText(getApplicationContext(), "양식이 잘못되었습니다.",Toast.LENGTH_SHORT).show();
                                    break;
                                }
                            }

                            if(state > 0){
                                Toast.makeText(getApplicationContext(),state+"개의 단어가 추가되었습니다.",Toast.LENGTH_SHORT).show();
                            }
                            updateListView(sort_state);
                            dialog.dismiss();     //닫기
                        } catch (Exception e){
                            Toast.makeText(getApplicationContext(), "양식이 잘못되었습니다.",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                // 취소 버튼 설정
                ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();     //닫기
                        // Event
                    }
                });

                ad.show();
            }
        });
    }

    public void modifyFunction(){

        Toast.makeText(getApplicationContext(),select_name, Toast.LENGTH_SHORT).show();
        Log.d(TAG,select_name);

        LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout et = (LinearLayout) vi.inflate(R.layout.edit_box, null);

        ImageView alertIcon = et.findViewById(R.id.edit_icon);
        TextView alertTitle = et.findViewById(R.id.edit_title);
        TextView alertSubTitle = et.findViewById(R.id.edit_subtitle);

        final EditText et_name = (EditText)et.findViewById(R.id.edit_name);
        final EditText et_subname = (EditText)et.findViewById(R.id.edit_subname);

        Glide.with(getApplicationContext()).load("https://i.imgur.com/D25gEp5.png").into(alertIcon);
        alertTitle.setText("단어장 수정");
        alertSubTitle.setText("수정 할 단어장 이름을\n입력하세요.");

        AlertDialog.Builder md = new AlertDialog.Builder(WordActivity.this);

        md.setView(et);
        et_name.setText(select_name);
        et_subname.setText(select_subname);


        // 확인 버튼 설정
        md.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Text 값 받아서 로그 남기기
                modify_name = et_name.getText().toString();
                modify_subname = et_subname.getText().toString();

                try{
                    bookDB.execSQL("UPDATE "+bk_name+" SET"+" word='"+modify_name+"', mean='"+modify_subname+"' WHERE word='"+select_name+"';");
                    updateListView(sort_state);
                    dialog.dismiss();     //닫기
                    Toast.makeText(getApplicationContext(), "수정되었습니다.", Toast.LENGTH_LONG).show();
                } catch(Exception e){
                    Toast.makeText(getApplicationContext(),"에러:동일한 단어가 존재합니다.",Toast.LENGTH_LONG).show();
                }

            }
        });

        // 취소 버튼 설정
        md.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();     //닫기
                // Event
            }
        });

        md.show();
    }

    public void updateListView(int sort_state){
        WordAdapter adapter = new WordAdapter();

        String str="";

        try{
            switch(sort_state){
                case 0:
                    str = "";
                    break;
                case 1:
                    str = " ORDER BY word ASC";
                    break;
                case 2:
                    str = " ORDER BY word DESC";
                    break;
            }

            Cursor c = bookDB.rawQuery("SELECT * FROM "+bk_name+str, null);

            count = findViewById(R.id.wl_count);
            count.setText(c.getCount()+"개");

            if(c!=null && c.getCount() != 0){
                if(c.moveToFirst()){
                    do{
                        String Name = c.getString(c.getColumnIndex("word"));
                        String Subname = c.getString(c.getColumnIndex("mean"));
                        int Priority = c.getInt(c.getColumnIndex("pri"));
                        String pri_url = new String();
                        switch (Priority){
                            case 1:
                                pri_url = "https://i.imgur.com/NubkZ4l.png";
                                break;
                            case 2:
                                pri_url = "https://i.imgur.com/RjwsANg.png";
                                break;
                            case 3:
                                pri_url = "https://i.imgur.com/KzZMZ7n.png";
                                break;
                            default: // 우선순위 0 또는 음수
                                pri_url = "https://i.imgur.com/5sZaSCh.png";
                                break;
                        }
                        adapter.addItem(new Wordlist(Name, Subname, "https://i.imgur.com/R4p7yBK.png",pri_url));
                        listView.setAdapter(adapter);
                    } while (c.moveToNext());
                }
            }
            else{
                listView.setAdapter(null); // 저장된 데이터가 없을 때, 공백으로 listview 업데이트
            }
        } catch (Exception e){
            Toast.makeText(getApplicationContext(), "SELECT ERROR", Toast.LENGTH_LONG).show();
        }
    }

    class WordAdapter extends BaseAdapter {

        ArrayList<Wordlist> items = new ArrayList<Wordlist>();

        @Override
        public int getCount() {
            return items.size();
        }

        public void addItem(Wordlist item) {
            items.add(item);
        }

        @Override
        public Object getItem(int i) {
            return items.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            WordlistView wordlistView = null;
            if(view == null){
                wordlistView = new WordlistView(getApplicationContext());
            }
            else{
                wordlistView = (WordlistView) view;
            }

            Wordlist item = items.get(i);
            wordlistView.setName(item.getName());
            wordlistView.setSubname(item.getSubname());
            wordlistView.setImageView(item.getUrl());
            wordlistView.setImageView2(item.getUrl2());

            final String select_word = item.getName();

            delete = wordlistView.findViewById(R.id.btn_delete);
            delete.setFocusable(false); // 리스트뷰와 리스트뷰 내 아이템 둘 다 클릭 가능하도록 설정
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG,"삭제한 단어:"+select_word);
                    try{
                        bookDB.execSQL("DELETE FROM "+bk_name+" WHERE word='"+select_word+"';"); // 단어 삭제
                        updateListView(sort_state);
                        Toast.makeText(getApplicationContext(),"삭제되었습니다.",Toast.LENGTH_SHORT).show();
                    } catch (Exception e){
                        Toast.makeText(getApplicationContext(),"문제가 발생하였습니다. 개발자에게 문의하세요.",Toast.LENGTH_LONG).show();
                    }
                }
            });

            priority = wordlistView.findViewById(R.id.btn_priority);
            priority.setFocusable(false);
            priority.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 우선순위 클릭 시
                    Cursor c = bookDB.rawQuery("SELECT pri FROM "+bk_name+" WHERE word='"+select_word+"';", null);
                    int pri_cnt = 0;
                    if(c.moveToFirst()){
                        do{
                            pri_cnt = c.getInt(c.getColumnIndex("pri"));
                        } while (c.moveToNext());
                    }

                    switch (pri_cnt){
                        case 1:
                            Glide.with(getApplicationContext()).load("https://i.imgur.com/RjwsANg.png").into(priority);
                            bookDB.execSQL("UPDATE "+bk_name+" SET"+" pri=2 WHERE word='"+select_word+"';");
                            break;
                        case 2:
                            Glide.with(getApplicationContext()).load("https://i.imgur.com/KzZMZ7n.png").into(priority);
                            bookDB.execSQL("UPDATE "+bk_name+" SET"+" pri=3 WHERE word='"+select_word+"';");
                            break;
                        case 3:
                            Glide.with(getApplicationContext()).load("https://i.imgur.com/5sZaSCh.png").into(priority);
                            bookDB.execSQL("UPDATE "+bk_name+" SET"+" pri=0 WHERE word='"+select_word+"';");
                            break;
                        default: // 우선순위 0 또는 음수
                            Glide.with(getApplicationContext()).load("https://i.imgur.com/NubkZ4l.png").into(priority);
                            bookDB.execSQL("UPDATE "+bk_name+" SET"+" pri=1 WHERE word='"+select_word+"';");
                            break;
                    }
                    updateListView(sort_state);
                }
            });



            return wordlistView;
        }
    }
}
