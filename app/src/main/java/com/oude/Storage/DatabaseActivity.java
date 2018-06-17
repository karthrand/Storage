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

public class DatabaseActivity extends Activity 
{
    private MyDatabaseHelper itemdb;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.database);
        //创建数据库，并指定数据库文件名称和版本
        //数据库文件会自动在/data/data/<package name>/databases/目录下创建
        //如果item.db数据库已创建，不会重复调用MyDatabaseHelper的onCreate()方法创建
        itemdb = new MyDatabaseHelper(this,"item.db",null,1);
        //以读写操作方式打开数据库
        itemdb.getWritableDatabase();
        

    }
    
    
    
    
    //RecycleView的Adapter
    public class DBListAdapter extends RecyclerView.Adapter<DBListAdapter.ViewHolder>
    {
        private List<ItemsList> mItemsList;

        class ViewHolder extends RecyclerView.ViewHolder {
            View ItemsView;
            TextView itemsName;

            public ViewHolder(View view) {
                super(view);
                ItemsView = view;
                itemsName = view.findViewById(R.id.items_list_name);
            }
        }

        public DBListAdapter(List<ItemsList> itemsList) {
            mItemsList = itemsList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemslist, parent, false);
            final ViewHolder holder = new ViewHolder(view);
            holder.ItemsView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = holder.getAdapterPosition();
                        ItemsList itmsList = mItemsList.get(position);
                        switch(position){
                                default:
                                break;
                        }
                    }
                });
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            ItemsList itemsList = mItemsList.get(position);
            holder.itemsName.setText(itemsList.getName());
        }

        @Override
        public int getItemCount() {
            return mItemsList.size();
        }
    }
    

}
