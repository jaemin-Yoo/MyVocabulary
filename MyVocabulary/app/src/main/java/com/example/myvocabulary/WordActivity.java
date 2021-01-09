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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class WordActivity extends AppCompatActivity {

    public String TAG = "Log";
    private ImageButton back, home, register, delete, many_word;
    private ImageView word, mean;
    private TextView title, count;
    private String bk_name = BookActivity.bk_name;
    private ListView listView;
    private String add_word, add_mean;

    SQLiteDatabase bookDB = null;
    private final String dbname = "MyVocabulary";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wordlist);

        title = findViewById(R.id.title);
        title.setText(bk_name); // 단어장 이름

        bookDB = this.openOrCreateDatabase(dbname, MODE_PRIVATE, null);

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
                Intent intent = new Intent(WordActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //액티비티 스택제거
                startActivity(intent);
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

                final EditText et_word = (EditText) findViewById(R.id.edit_word);
                final EditText et_mean = (EditText) findViewById(R.id.edit_mean);

                add_word = et_word.getText().toString();
                add_mean = et_mean.getText().toString();

                if(add_word.length() != 0 && add_mean.length() != 0){
                    try{
                        bookDB.execSQL("INSERT INTO "+bk_name+" VALUES('"+add_word+"','"+add_mean+"');"); // SQLite 단어 추가
                        updateListView();
                        et_word.setText(""); // 추가 후 EditText 공백처리
                        et_mean.setText("");
                        et_word.requestFocus();
                        et_word.setCursorVisible(true);
                        Log.d(TAG, "추가한 단어:"+add_word);
                    } catch (Exception e){
                        Toast.makeText(getApplicationContext(),"중복된 단어가 존재합니다.",Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "빈칸을 채우세요.",Toast.LENGTH_SHORT).show();
                }
            }
        });

        updateListView();

        many_word = findViewById(R.id.add_many);
        Glide.with(this).load(R.drawable.add_many).into(many_word);
        many_word.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 한꺼번에 단어추가
            }
        });
    }


    public void updateListView(){
        listView = findViewById(R.id.word_list);
        WordAdapter adapter = new WordAdapter();

        SQLiteDatabase ReadDB = this.openOrCreateDatabase(dbname, MODE_PRIVATE, null);
        Cursor c = ReadDB.rawQuery("SELECT * FROM "+bk_name, null);

        count = findViewById(R.id.wl_count);
        count.setText(c.getCount()+"개");

        if(c!=null && c.getCount() != 0){
            if(c.moveToFirst()){
                do{
                    String Name = c.getString(c.getColumnIndex("word"));
                    String Subname = c.getString(c.getColumnIndex("mean"));
                    adapter.addItem(new Wordlist(Name, Subname, R.drawable.delete));
                    listView.setAdapter(adapter);
                } while (c.moveToNext());
            }
        }
        else{
            listView.setAdapter(null); // 저장된 데이터가 없을 때, 공백으로 listview 업데이트
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
            wordlistView.setWord(item.getResId());

            final String delete_word = item.getName();

            delete = wordlistView.findViewById(R.id.btn_delete);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG,"삭제한 단어:"+delete_word);
                    try{
                        bookDB.execSQL("DELETE FROM "+bk_name+" WHERE word='"+delete_word+"';"); // 단어 삭제
                        updateListView();
                        Toast.makeText(getApplicationContext(),"삭제되었습니다.",Toast.LENGTH_SHORT).show();
                    } catch (Exception e){
                        Toast.makeText(getApplicationContext(),"문제가 발생하였습니다. 개발자에게 문의하세요.",Toast.LENGTH_LONG).show();
                    }
                }
            });

            return wordlistView;
        }
    }
}
