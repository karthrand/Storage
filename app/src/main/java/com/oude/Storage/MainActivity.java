package com.oude.Storage;

import android.os.*;
import android.app.Activity;
import java.util.List;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;
import java.util.ArrayList;
public class MainActivity extends Activity 
{
    private List<MyList> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initMylist();
        RecyclerView recycleView = (RecyclerView) findViewById(R.id.mainRecyclerView1);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recycleView.setLayoutManager(layoutManager);
        MyListAdapter adapter =new MyListAdapter(list);
        recycleView.setAdapter(adapter);
    }


    private void initMylist()
    {
            MyList  list1 = new MyList("文件存储",R.drawable.ic_launcher);
            list.add(list1);
            MyList  list2 = new MyList("List2",R.drawable.ic_launcher);
            list.add(list2);
            MyList  list3 = new MyList("List3",R.drawable.ic_launcher);
            list.add(list3);
            MyList  list4 = new MyList("List4",R.drawable.ic_launcher);            list.add(list4);
   
        
    }

}
