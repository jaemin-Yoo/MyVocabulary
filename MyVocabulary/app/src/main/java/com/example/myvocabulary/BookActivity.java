package com.example.myvocabulary;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class BookActivity extends AppCompatActivity {

    private ImageButton back, add;
    SQLiteDatabase bookDB = null;
    private final String dbname = "MyVocabulary";
    private final String tablename = "book";
    private String add_text;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        bookDB = this.openOrCreateDatabase(dbname, MODE_PRIVATE, null);
        bookDB.execSQL("CREATE TABLE IF NOT EXISTS "+tablename
                +" (name VARCHAR(10) PRIMARY KEY, subname VARCHAR(20));");

        back=findViewById(R.id.btn_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ListView listView = findViewById(R.id.listview);
        BookAdapter adapter = new BookAdapter();

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                show();
                return false;
            }
        });

        add=findViewById(R.id.add_book);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder ad = new AlertDialog.Builder(BookActivity.this);

                ad.setTitle("단어장 추가");       // 제목 설정
                ad.setMessage("단어장 이름");   // 내용 설정
                ad.setIcon(R.drawable.book);

                // EditText 삽입하기
                final EditText et_name = new EditText(BookActivity.this);
                ad.setView(et_name);


                // 확인 버튼 설정
                ad.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Text 값 받아서 로그 남기기
                        add_text = et_name.getText().toString();
                        bookDB.execSQL("INSERT INTO "+tablename+" VALUES('"+add_text+"','test');");
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

    /*
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuItem Edit = menu.add(Menu.NONE, 1001, 1, "수정");
        MenuItem Delete = menu.add(Menu.NONE, 1002, 2, "삭제");
        Edit.setOnMenuItemClickListener(onEditMenu);
        Edit.setOnMenuItemClickListener(onDeleteMenu);
    }

    private final MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()){
                case 1001:

                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    View view = LayoutInflater.from(mContext)
                            .inflate(R.layout.activity_booklist, null, false);
                    builder.setView(view);
            }
        }
    }

     */

    void show(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Title");
        builder.setMessage("어떤 작업을 수행하시겠습니까?");
        builder.setPositiveButton("삭제",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        Toast.makeText(getApplicationContext(), "삭제되었습니다.", Toast.LENGTH_LONG).show();
                    }
                });
        builder.setNegativeButton("수정",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        Toast.makeText(getApplicationContext(), "수정되었습니다.", Toast.LENGTH_LONG).show();
                    }
                });
        builder.setNeutralButton("취소",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        builder.show();
    }

    public void updateListView(){
        ListView listView = findViewById(R.id.listview);
        BookAdapter adapter = new BookAdapter();

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
