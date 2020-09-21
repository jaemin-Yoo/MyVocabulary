package com.example.myvocabulary;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class BookActivity extends AppCompatActivity {

    private ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        back=findViewById(R.id.btn_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Log.d("test", "test1");
        ListView listView = findViewById(R.id.listview);
        BookAdapter adapter = new BookAdapter();
        Log.d("test", "test2");
        adapter.addItem(new Booklist("Test1", "test1", R.drawable.book));
        adapter.addItem(new Booklist("Test2", "test2", R.drawable.book));
        adapter.addItem(new Booklist("Test3", "test3", R.drawable.book));
        adapter.addItem(new Booklist("Test4", "test4", R.drawable.book));
        adapter.addItem(new Booklist("Test5", "test5", R.drawable.book));
        adapter.addItem(new Booklist("Test6", "test6", R.drawable.book));
        adapter.addItem(new Booklist("Test7", "test7", R.drawable.book));
        adapter.addItem(new Booklist("Test8", "test8", R.drawable.book));
        Log.d("test", "test3");
        listView.setAdapter(adapter);
        Log.d("test", "test4");
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
