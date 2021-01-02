package com.example.myvocabulary;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class BookActivity extends AppCompatActivity {

    public String TAG = "MyVocabulary";
    private ImageButton back, add, home;
    private ImageView img_book;
    SQLiteDatabase bookDB = null;
    private final String dbname = "MyVocabulary";
    private final String tablename = "book";
    private String add_name;
    private String add_subname;
    private String modify_name;
    private String modify_subname;
    private String select_name;
    private String select_subname;
    private ListView listView;
    private Booklist bl;
    public static String bk_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booklist);

        bookDB = this.openOrCreateDatabase(dbname, MODE_PRIVATE, null);
        bookDB.execSQL("CREATE TABLE IF NOT EXISTS "+tablename
                +" (name VARCHAR(10) PRIMARY KEY, subname VARCHAR(20));");

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

        listView = findViewById(R.id.listview);
        BookAdapter adapter = new BookAdapter();
        registerForContextMenu(listView); // 리스트뷰 Context menu 등록

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(BookActivity.this, WordActivity.class);
                startActivity(intent);
                bl = (Booklist) listView.getAdapter().getItem(i);
                bk_name = (String) bl.getName();
                Toast.makeText(getApplicationContext(),bk_name, Toast.LENGTH_SHORT).show();
            }
        });

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
                        bookDB.execSQL("INSERT INTO "+tablename+" VALUES('"+add_name+"','"+add_subname+"');");
                        updateListView();
                        dialog.dismiss();     //닫기
                        // Event
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

        SQLiteDatabase ReadDB = this.openOrCreateDatabase(dbname, MODE_PRIVATE, null);
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
                        bookDB.execSQL("UPDATE "+tablename+" SET"+" name='"+modify_name+"', subname='"+modify_subname+"' WHERE name='"+select_name+"';");
                        updateListView();
                        dialog.dismiss();     //닫기
                        Toast.makeText(getApplicationContext(), "수정되었습니다.", Toast.LENGTH_LONG).show();
                        // Event
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
                        bookDB.execSQL("DELETE FROM "+tablename+" WHERE name='"+select_name+"';");
                        updateListView();
                        dialog.dismiss();     //닫기
                        Toast.makeText(getApplicationContext(), "삭제되었습니다.", Toast.LENGTH_LONG).show();
                        // Event
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
        ListView listView = findViewById(R.id.listview);
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
