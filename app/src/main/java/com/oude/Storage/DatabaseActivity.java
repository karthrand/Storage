package com.oude.Storage;

import android.content.SharedPreferences;
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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.content.DialogInterface;
import android.util.TypedValue;
import android.graphics.Typeface;
import java.util.LinkedList;
import java.math.BigDecimal;
import android.support.v4.widget.SwipeRefreshLayout;

public class DatabaseActivity extends AppCompatActivity
{
    private DBListAdapter adapter;
    
    private MyDatabaseHelper itemdb;
    //用item表的name作为筛选条件，因此需要保证item的name的唯一性
    private String item_name,item_type,item_explain;
    private Float item_price,item_weight;
    //存储取出来的item名字
    private List<ItemsList> list = new ArrayList<>();
    //下拉刷新
    private SwipeRefreshLayout swipeRefresh;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.database);
        //下拉刷新绑定控件
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
                @Override
                public void onRefresh()
                {
                    refreshItems();
                }
            });
       
        //创建数据库，并指定数据库文件名称和版本，完成初始化
        //数据库文件会自动在/data/data/<package name>/databases/目录下创建
        //如果item.db数据库已创建，不会重复调用MyDatabaseHelper的onCreate()方法创建
        itemdb = new MyDatabaseHelper(this, "item.db", null, 1);
        //以读写操作方式打开数据库
        itemdb.getWritableDatabase();
        initItems();

        //Recycle实现
        RecyclerView recycleView = (RecyclerView) findViewById(R.id.databaseRecyclerView1);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recycleView.setLayoutManager(layoutManager);
        adapter =new DBListAdapter(list);
        recycleView.setAdapter(adapter);


    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        //关闭数据库，一个Activity中可以只使用一个数据库实例，节省性能
        itemdb.close();

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
                        ItemsList itmsList = mItemsList.get(position);
                        //在初始化详情之前获取当前列的name
                        item_name = itmsList.getName();
                        switch (position)
                        {
                            default: Detail();
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
        list.clear();
        //以读写操作方式打开数据库
        SQLiteDatabase  sqlLite = itemdb.getReadableDatabase();
        //取出数据
        Cursor cursor = sqlLite.query("item", null, null, null, null, null, null);
        cursor.moveToFirst();
        //取出所有的item项目时，如果数据库为空，则报错Indpex 0 requested, with a size of 0
        if (cursor.moveToPosition(0) != true)
        {  
            //新增一条数据，以免报报空指针错误
            ContentValues values = new ContentValues();
            values.put("name", "背包");
            values.put("type", "冒险物品");
            values.put("price", 2.1);
            values.put("weight", 2);
            values.put("explain", "普通的背包");
            sqlLite.insert("item", null, values);
            values.clear();
            Toast.makeText(this, "无Item，已自动添加初始，请下拉刷新!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            do{
                String name=cursor.getString(cursor.getColumnIndex("name"));
                ItemsList  item =new ItemsList(name);
                list.add(item);
            }while (cursor.moveToNext());
            cursor.close();
        }

    }


    //单击后查看详情
    public void Detail()
    {

        AlertDialog.Builder builder= new AlertDialog.Builder(DatabaseActivity.this);
        View detailView = LayoutInflater.from(DatabaseActivity.this).inflate(R.layout.itemdetails, null);
        builder.setTitle(this.getResources().getText(R.string.detail));
        //使用自定义xml
        builder.setView(detailView);
        //详情页面view加载和控件绑定
        final EditText name = detailView.findViewById(R.id.item_name);
        final EditText type = detailView.findViewById(R.id.item_type);
        final EditText price = detailView.findViewById(R.id.item_price);
        final EditText weight = detailView.findViewById(R.id.item_weight);
        final EditText explain = detailView.findViewById(R.id.item_explain);

        //修改数据库中取出数据后的字体大小和风格，尽量与旁边的TextView显示风格对齐
        List<EditText> ets = new LinkedList<EditText>();
        ets.add(name);
        ets.add(type);
        ets.add(price);
        ets.add(weight);
        ets.add(explain);
        for (int i=0;i < ets.size();i++)
        {
            ets.get(i).setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            ets.get(i).setTypeface(Typeface.DEFAULT_BOLD);
        }

        //从数据库获取值在详情中显示
        final SQLiteDatabase  db = itemdb.getReadableDatabase();
        Cursor cursor = db.query("item", null, "name=?", new String[]{item_name}, null, null, null);
        if (cursor.moveToFirst())
        {
            do{
                //遍历获取数据库中的值并给EditText赋值
                item_type = cursor.getString(cursor.getColumnIndex("type"));
                item_price = cursor.getFloat(cursor.getColumnIndex("price"));
                item_weight = cursor.getFloat(cursor.getColumnIndex("weight"));
                item_explain = cursor.getString(cursor.getColumnIndex("explain"));

                name.setText(item_name);
                type.setText(item_type);               
                price.setText(String.valueOf(item_price));
                weight.setText(String.valueOf(item_weight));
                explain.setText(item_explain);
                //存储在SharedPreferences中，用于在后面进行校验
                SharedPreferences.Editor editor =  getSharedPreferences("items", MODE_PRIVATE).edit();               
                editor.putString("item_name", item_name);
                editor.putString("item_type", item_type);
                editor.putFloat("item_price", item_price);
                editor.putFloat("item_weight", item_weight);
                editor.putString("item_explain", item_explain);
                editor.apply();  

            }while(cursor.moveToNext());
        }
        cursor.close();

        //取消按钮和修改按钮，按钮的值都写在string文件中，此处使用java方式获取
        builder.setPositiveButton(this.getResources().getText(R.string.cancel), new DialogInterface.OnClickListener(){

                @Override
                public void onClick(DialogInterface p1, int p2)
                {
                    //取消目前不做啥，以后可以优化
                }
            });

        builder.setNegativeButton(this.getResources().getText(R.string.modify), new DialogInterface.OnClickListener(){

                @Override
                public void onClick(DialogInterface p1, int p2)
                {
                    //修改检查，检查是否有更新
                    //重新获取EditText的值
                    item_name = name.getText().toString();
                    item_type = type.getText().toString();
                    item_price = Float.parseFloat(price.getText().toString());
                    item_weight = Float.parseFloat(weight.getText().toString());
                    item_explain = explain.getText().toString();
                    //将新的值和之前保存的比较，无改变则不更新数据库
                    SharedPreferences sp = getSharedPreferences("items", MODE_PRIVATE);
                    if (item_name.equals(sp.getString("item_name", "")) && 
                        item_type.equals(sp.getString("item_type", ""))  &&
                        Math.abs(item_price - sp.getFloat("item_price", 0)) < 0.00001  &&
                        Math.abs(item_weight - sp.getFloat("item_weight", 0)) < 0.00001 &&
                        item_explain.equals(sp.getString("item_explain", "")))
                    {
                        Toast.makeText(DatabaseActivity.this, "修改操作需要修改任意值！", Toast.LENGTH_SHORT).show();                     
                    }
                    else
                    {
                        //更新数据库
                        ContentValues updateValue = new ContentValues();
                        updateValue.put("name", item_name);
                        updateValue.put("type", item_type);
                        updateValue.put("price", item_price);
                        updateValue.put("weight", item_weight);
                        updateValue.put("explain", item_explain);
                        db.update("item", updateValue, "name=?", new String[]{sp.getString("item_name", "")});
                    }

                }
            });
        builder.show();
    }

    
    private void refreshItems(){
        new Thread(new Runnable(){
                @Override
                public void run()
                {
                    try {
                        //因为本地刷新速度太快看不到效果，延迟下
                        Thread.sleep(1000);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable(){

                            @Override
                            public void run()
                            {
                                initItems();
                                adapter.notifyDataSetChanged();
                                swipeRefresh.setRefreshing(false);
                            }
                    });
                }
            
        }).start();
    }



}
