package com.example.myvocabulary;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
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
import android.widget.FrameLayout;
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
import java.util.Random;

public class BookActivity extends AppCompatActivity {

    public String TAG = "Log";
    private ImageButton back, add, home, game_exit, touch, incorrect, correct;
    private ImageView touch_blank;
    private TextView state_text, game_word, game_mean, gm_count;
    private FrameLayout game_layout, touch_layout;
    SQLiteDatabase bookDB = null;
    private final String dbname = "MyVocabulary";
    private final String tablename = "book";
    private String add_name;
    private String add_subname;
    private String modify_name;
    private String modify_subname;
    private String select_name;
    private String select_subname;
    private String[] wd_arr;
    private String[] mn_arr;
    private int[] rd_arr;
    private ListView listView;
    private Booklist bl;
    public static String bk_name;
    private SQLiteDatabase ReadDB;
    private int count = 0;
    private int c_cnt = 0;
    //private boolean finish = false;

    private int state = MainActivity.state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booklist);

        bookDB = this.openOrCreateDatabase(dbname, MODE_PRIVATE, null);
        bookDB.execSQL("CREATE TABLE IF NOT EXISTS "+tablename
                +" (name VARCHAR(10) PRIMARY KEY, subname VARCHAR(20));");

        ReadDB = this.openOrCreateDatabase(dbname, MODE_PRIVATE, null);

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
                Intent intent = new Intent(BookActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //액티비티 스택제거
                startActivity(intent);
            }
        });

        game_exit = findViewById(R.id.game_exit);
        Glide.with(this).load(R.drawable.exit).into(game_exit);
        game_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                game_layout.setVisibility(View.INVISIBLE);
                disableEnableControls(true, (ViewGroup)findViewById(R.id.book_layout));
                count = 0;
            }
        });

        touch = findViewById(R.id.touch);
        Glide.with(this).load(R.drawable.touch).into(touch);
        touch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,"TOUCH");
                // 터치 버튼 비활성화 고려 ( 필요없는 것 같기도 함 )
                touch_layout.setVisibility(View.VISIBLE);
            }
        });

        touch_blank = findViewById(R.id.touch_blank);
        Glide.with(this).load(R.drawable.touch_blank).into(touch_blank);

        incorrect = findViewById(R.id.incorrect);
        Glide.with(this).load(R.drawable.incorrect).into(incorrect);
        incorrect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 틀렸어요, 다음 단어, 우선순위 증가
                if (count!=c_cnt){
                    nextWord();
                } else{
                    ending();
                }
                Log.d(TAG,count+" (count)");
            }
        });

        correct = findViewById(R.id.correct);
        Glide.with(this).load(R.drawable.correct).into(correct);
        correct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 맞췄어요, 다음 단어
                if (count!=c_cnt){
                    nextWord();
                } else{
                    ending();
                }
                Log.d(TAG,count+" (count)");
            }
        });

        game_layout = findViewById(R.id.game_layout);
        touch_layout = findViewById(R.id.touch_layout);
        game_layout.setVisibility(View.INVISIBLE);

        game_word = findViewById(R.id.game_word);
        game_mean = findViewById(R.id.game_mean);
        gm_count = findViewById(R.id.gm_count);

        listView = findViewById(R.id.book_list);
        BookAdapter adapter = new BookAdapter();
        registerForContextMenu(listView); // 리스트뷰 Context menu 등록

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                bl = (Booklist) listView.getAdapter().getItem(i);
                bk_name = bl.getName();

                if (state == 0){
                    // 단어추가
                    Intent intent = new Intent(BookActivity.this, WordActivity.class);
                    startActivity(intent);
                } else {
                    // 게임시작
                    Cursor cursor = ReadDB.rawQuery("SELECT * FROM "+bk_name, null);
                    c_cnt = cursor.getCount();

                    if (c_cnt != 0){
                        game_layout.setVisibility(View.VISIBLE);
                        disableEnableControls(false, (ViewGroup)findViewById(R.id.book_layout));

                        // 단어 select
                        Log.d(TAG,c_cnt+" (c_cnt)");
                        wd_arr = new String[c_cnt];
                        mn_arr = new String[c_cnt];

                        int j = 0;
                        if(cursor!=null){
                            if(cursor.moveToFirst()){
                                do{
                                    wd_arr[j] = cursor.getString(cursor.getColumnIndex("word"));
                                    mn_arr[j] = cursor.getString(cursor.getColumnIndex("mean"));
                                    j++;
                                } while (cursor.moveToNext());
                            }
                        }
                        randomNumber();
                        nextWord();

                        Toast.makeText(getApplicationContext(),bk_name, Toast.LENGTH_SHORT).show();
                    } else{
                        Toast.makeText(getApplicationContext(),"최소 1개 이상의 단어를 등록하세요.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        state_text = findViewById(R.id.state_text);

        if (state == 0){
            state_text.setText("단어추가");
            state_text.setTextColor(Color.parseColor("#8EBB65"));

            add=findViewById(R.id.add_book);
            Glide.with(this).load(R.drawable.add_book).into(add);
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    LinearLayout et = (LinearLayout) vi.inflate(R.layout.edit_box, null);

                    final EditText et_name = (EditText)et.findViewById(R.id.edit_name);
                    final EditText et_subname = (EditText)et.findViewById(R.id.edit_subname);

                    AlertDialog.Builder ad = new AlertDialog.Builder(BookActivity.this);

                    ad.setTitle("단어장 추가");       // 제목 설정
                    ad.setMessage("단어장 이름");   // 내용 설정
                    ad.setIcon(R.drawable.book);

                    ad.setView(et);

                    // 확인 버튼 설정
                    ad.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            // Text 값 받아서 로그 남기기
                            add_name = et_name.getText().toString();
                            add_subname = et_subname.getText().toString();
                            if (add_name.length() != 0 && add_subname.length() != 0){
                                try{
                                    bookDB.execSQL("CREATE TABLE "+add_name
                                            +" (word VARCHAR(10) PRIMARY KEY, mean VARCHAR(20));");
                                    bookDB.execSQL("INSERT INTO "+tablename+" VALUES('"+add_name+"','"+add_subname+"');");
                                    updateListView();
                                    dialog.dismiss();     //닫기
                                } catch (Exception e){
                                    Toast.makeText(getApplicationContext(),"에러:동일한 단어장이 존재하거나 특수문자가 포함되어있습니다.",Toast.LENGTH_LONG).show();
                                }
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"빈칸을 채우세요.",Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                    // 취소 버튼 설정
                    ad.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
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
        else{
            state_text.setText("게임시작");
            state_text.setTextColor(Color.parseColor("#489DD8"));
        }

        Cursor c = ReadDB.rawQuery("SELECT * FROM "+tablename, null);
        if(c!=null){
            if(c.moveToFirst()){
                do{
                    String Name = c.getString(c.getColumnIndex("name"));
                    String Subname = c.getString(c.getColumnIndex("subname"));
                    adapter.addItem(new Booklist(Name, Subname, R.drawable.book));
                    listView.setAdapter(adapter);
                } while (c.moveToNext());
            }
        }
    }

    private void randomNumber(){
        Random random = new Random();
        rd_arr = new int[c_cnt];

        for (int a=0; a<c_cnt; a++){
            rd_arr[a] = random.nextInt(c_cnt);
            for (int b=0; b<a; b++){
                if (rd_arr[a] == rd_arr[b]){
                    a--;
                }
            }
        }
    }

    private void ending(){
        AlertDialog.Builder ending = new AlertDialog.Builder(BookActivity.this);

        ending.setTitle("게임종료");       // 제목 설정
        ending.setMessage("모든 단어를 테스트하였습니다." +
                "\n한번 더 하시겠습니까?");   // 내용 설정
        ending.setIcon(R.drawable.baby);

        // 확인 버튼 설정
        ending.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();     //닫기
                count = 0;
                randomNumber();
                nextWord();
            }
        });

        // 취소 버튼 설정
        ending.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();     //닫기
                game_layout.setVisibility(View.INVISIBLE);
                disableEnableControls(true, (ViewGroup)findViewById(R.id.book_layout));
                count = 0;
            }
        });

        ending.show();
    }

    private void nextWord(){
        touch_layout.setVisibility(View.INVISIBLE);
        game_word.setText(wd_arr[rd_arr[count]]);
        game_mean.setText(mn_arr[rd_arr[count]]);
        count++;
        gm_count.setText(count+"/"+c_cnt);
    }

    private void disableEnableControls(boolean enable, ViewGroup vg){
        for (int i = 0; i < vg.getChildCount(); i++){
            View child = vg.getChildAt(i);
            child.setEnabled(enable);
            if (child instanceof ViewGroup){
                disableEnableControls(enable, (ViewGroup)child);
            }
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        bl = (Booklist) listView.getAdapter().getItem(info.position);

        menu.setHeaderTitle(bl.getName());
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        select_name = bl.getName();
        select_subname = bl.getSubname();

        switch(item.getItemId())
        {
            case R.id.modify:

                LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                LinearLayout et = (LinearLayout) vi.inflate(R.layout.edit_box, null);

                final EditText et_name = (EditText)et.findViewById(R.id.edit_name);
                final EditText et_subname = (EditText)et.findViewById(R.id.edit_subname);

                AlertDialog.Builder md = new AlertDialog.Builder(BookActivity.this);

                md.setTitle("단어장 수정");       // 제목 설정
                md.setMessage("수정 할 단어장 이름을 입력하세요.");   // 내용 설정
                md.setIcon(R.drawable.book);

                // EditText 삽입하기

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
                            bookDB.execSQL("ALTER TABLE "+select_name+" RENAME TO "+modify_name+";");
                            bookDB.execSQL("UPDATE "+tablename+" SET"+" name='"+modify_name+"', subname='"+modify_subname+"' WHERE name='"+select_name+"';");
                            updateListView();
                            dialog.dismiss();     //닫기
                            Toast.makeText(getApplicationContext(), "수정되었습니다.", Toast.LENGTH_LONG).show();
                        } catch(Exception e){
                            Toast.makeText(getApplicationContext(),"에러:동일한 단어장이 존재하거나 특수문자가 포함되어있습니다.",Toast.LENGTH_LONG).show();
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
                return true;

            case R.id.delete:
                AlertDialog.Builder dl = new AlertDialog.Builder(BookActivity.this);

                dl.setTitle("단어장 삭제");       // 제목 설정
                dl.setMessage("정말 삭제하시겠습니까?");   // 내용 설정
                dl.setIcon(R.drawable.book);

                // 확인 버튼 설정
                dl.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try{
                            bookDB.execSQL("DELETE FROM "+tablename+" WHERE name='"+select_name+"';"); // book Table에 해당 Data 삭제
                            bookDB.execSQL("DROP TABLE "+select_name+";"); // 해당 Table 삭제
                            updateListView();
                            dialog.dismiss();     //닫기
                            Toast.makeText(getApplicationContext(), "삭제되었습니다.", Toast.LENGTH_LONG).show();
                        } catch (Exception e){
                            Toast.makeText(getApplicationContext(), "문제가 발생하였습니다. 개발자에게 문의하세요.",Toast.LENGTH_LONG).show();
                        }

                    }
                });

                // 취소 버튼 설정
                dl.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();     //닫기
                        // Event
                    }
                });

                dl.show();
                return true;
        }

        return super.onContextItemSelected(item);
    }

    public void updateListView(){
        ListView listView = findViewById(R.id.book_list);
        BookAdapter adapter = new BookAdapter();

        SQLiteDatabase ReadDB = this.openOrCreateDatabase(dbname, MODE_PRIVATE, null);
        Cursor c = ReadDB.rawQuery("SELECT * FROM "+tablename, null);

        if(c!=null && c.getCount() != 0){
            if(c.moveToFirst()){
                do{
                    String Name = c.getString(c.getColumnIndex("name"));
                    String Subname = c.getString(c.getColumnIndex("subname"));
                    adapter.addItem(new Booklist(Name, Subname, R.drawable.book));
                    listView.setAdapter(adapter);
                } while (c.moveToNext());
            }
        }
        else{
            listView.setAdapter(null); // 저장된 데이터가 없을 때, 공백으로 listview 업데이트
        }
    }

    class BookAdapter extends BaseAdapter {

        ArrayList<Booklist> items = new ArrayList<Booklist>();

        @Override
        public int getCount() {
            return items.size();
        }

        public void addItem(Booklist item) {
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
            BooklistView booklistView = null;
            if(view == null){
                booklistView = new BooklistView(getApplicationContext());
            }
            else{
                booklistView = (BooklistView)view;
            }
            Booklist item = items.get(i);
            booklistView.setName(item.getName());
            booklistView.setSubname(item.getSubname());
            booklistView.setBook(item.getResId());
            return booklistView;
        }


    }
}
