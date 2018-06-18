package com.oude.Storage;

import android.app.Activity;
import android.os.Bundle;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.TextView.SavedState;
import android.widget.Button;
import android.widget.EditText;
import android.support.v7.widget.RecyclerView;
import java.util.List;
import android.widget.TextView;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import android.support.v7.widget.LinearLayoutManager;
import android.content.ContentValues;
import android.widget.Toast;

public class DatabaseActivity extends Activity 
{
    private MyDatabaseHelper itemdb;
    //存储取出来的item名字
    private List<ItemsList> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.database);
        //创建数据库，并指定数据库文件名称和版本，完成初始化
        //数据库文件会自动在/data/data/<package name>/databases/目录下创建
        //如果item.db数据库已创建，不会重复调用MyDatabaseHelper的onCreate()方法创建
        itemdb = new MyDatabaseHelper(this, "item.db", null, 1);
        //以读写操作方式打开数据库
        itemdb.getWritableDatabase();
        itemdb.close();
        initItems();
        RecyclerView recycleView = findViewById(R.id.databaseRecyclerView1);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recycleView.setLayoutManager(layoutManager);
        DBListAdapter adapter =new DBListAdapter(list);
        recycleView.setAdapter(adapter);




    }




    //RecycleView的Adapter
    public class DBListAdapter extends RecyclerView.Adapter<DBListAdapter.ViewHolder>
    {
        private List<ItemsList> mItemsList;

        class ViewHolder extends RecyclerView.ViewHolder
        {
            View ItemsView;
            TextView itemsName;

            public ViewHolder(View view)
            {
                super(view);
                ItemsView = view;
                itemsName = view.findViewById(R.id.items_list_name);
            }
        }

        public DBListAdapter(List<ItemsList> itemsList)
        {
            mItemsList = itemsList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemslist, parent, false);
            final ViewHolder holder = new ViewHolder(view);
            holder.ItemsView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        int position = holder.getAdapterPosition();
                        // ItemsList itmsList = mItemsList.get(position);
                        switch (position)
                        {
                            default:
                                break;
                        }
                    }
                });
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position)
        {
            ItemsList itemsList = mItemsList.get(position);
            holder.itemsName.setText(itemsList.getName());
        }

        @Override
        public int getItemCount()
        {
            return mItemsList.size();
        }
    }



    //获取所有items资源并显示
    private void initItems()
    {
        MyDatabaseHelper dbhelper = new MyDatabaseHelper(this, "item.db", null, 1);
        //以读写操作方式打开数据库
        SQLiteDatabase  sqlLite = dbhelper.getReadableDatabase();
        //新增一条数据，以免报报空指针错误
        
        ContentValues values = new ContentValues();
        values.put("name", "背包");
        values.put("type", "冒险物品");
        values.put("price", 2);
        values.put("weight", 2);
        values.put("explain", "普通的背包");
        sqlLite.insert("item",null,values);
        values.clear();
        
        //取出数据
        Cursor cursor = sqlLite.query("item", null, null, null, null, null, null);
        cursor.moveToFirst();
        //取出所有的item项目时，如果数据库为空，则报错Indpex 0 requested, with a size of 0
        if (cursor.moveToPosition(0) != true) {  
            Toast.makeText(this,"无Item，请添加!",Toast.LENGTH_SHORT).show();
        }else{
        do{
            String name=cursor.getString(cursor.getColumnIndex("name"));
            ItemsList  item =new ItemsList(name);
            list.add(item);
        }while (cursor.moveToNext());
        cursor.close();
        }
        
        }


    

}
