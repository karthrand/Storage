package com.oude.Storage;

import android.content.SharedPreferences;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SharedPreferences extends Activity implements OnClickListener
{
    private Button stringSave,intSave,stringLoad,intLoad;
    private EditText string_et,int_et;
    String stringData,getString;
    Integer intData,getInt;
    private TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sharedpreferences);

        //绑定按钮
        stringSave = findViewById(R.id.spButton1);
        intSave = findViewById(R.id.spButton2);
        stringLoad = findViewById(R.id.spButton3);
        intLoad = findViewById(R.id.spButton4);
        string_et = findViewById(R.id.spEditText1);
        int_et = findViewById(R.id.spEditText2);
        tv = findViewById(R.id.spTextView3);

        //绑定监听器
        stringSave.setOnClickListener(this);
        intSave.setOnClickListener(this);
        stringLoad.setOnClickListener(this);
        intLoad.setOnClickListener(this);
        
        tv.setText("读取记录:\n");


    }

    //按钮监听器
    @Override
    public void onClick(View p1)
    {
        //editor负责存储，sp负责服读取
        //editor后记得apply提交
        SharedPreferences.Editor editor =  getSharedPreferences("data",MODE_PRIVATE).edit();
        SharedPreferences sp = getSharedPreferences("data",MODE_PRIVATE);
        switch(p1.getId()){                                             
            case R.id.spButton1:
                stringData = string_et.getText().toString();
                editor.putString("str",stringData);
                //存储提交，否则不生效
                editor.apply();         
                Toast.makeText(this,"字符保存成功!",Toast.LENGTH_SHORT).show();
                break;
            case R.id.spButton2:
                intData = Integer.parseInt(int_et.getText().toString());
                editor.putInt("int",intData);
                editor.apply();         
                Toast.makeText(this,"整型保存成功!",Toast.LENGTH_SHORT).show();
                break;
            case R.id.spButton3:
                getString = sp.getString("str","获取失败！");
                tv.append(getTime() + " 获取String: " + getString + "\n");
                break;
            case R.id.spButton4:
                getInt = sp.getInt("int",-1);
                tv.append(getTime() + " 获取Intger: " + getInt + "\n");
                break;
                default:
                    Toast.makeText(this,"未知错误!",Toast.LENGTH_SHORT).show();
                break;
                }

        }
        
    //实现获取当前时间
    public String getTime()
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss ");
        Date curDate = new Date(System.currentTimeMillis());
        String str = formatter.format(curDate);
        return str;
    }


    }
