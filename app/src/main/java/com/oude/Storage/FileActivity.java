package com.oude.Storage;

import android.app.Activity;
import android.os.Bundle;
import java.io.FileOutputStream;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.OutputStreamWriter;
import java.io.IOException;
import android.widget.Button;
import android.widget.EditText;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.widget.TextView;
import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import android.text.TextUtils;

public class FileActivity extends Activity 
{
    private Button save,load;
    private EditText et;
    private TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file);
        save = findViewById(R.id.fileButton1);
        et = findViewById(R.id.fileEditText1);
        tv = findViewById(R.id.fileTextView2);
        load = findViewById(R.id.fileButton2);
        tv.setText("读取记录:\n");
        //保存按钮实现，调用save方法
        save.setOnClickListener(new OnClickListener(){

                @Override
                public void onClick(View p1)
                {
                    String inputText = et.getText().toString();
                    save(inputText);
                    Toast.makeText(FileActivity.this, "输入已保存", Toast.LENGTH_SHORT).show();
                }
            });
        //读取按钮实现，调用load方法
        load.setOnClickListener(new OnClickListener(){

                @Override
                public void onClick(View p1)
                {
                    String data = load();
                    if (!TextUtils.isEmpty(data))
                    {
                        //以时间+当前data.txt中的内容+回车方式输出到文本中
                        tv.append(getTime() + data + "\n");
                    }
                }
            });


    }

    //文件存储第一步：实现存储方法
    public void save(String input)
    {
        FileOutputStream out = null;
        BufferedWriter write = null;
        try
        {
            //设置输出到/dta/data/<package name>/files/data.txt下，写入方式为覆盖方式
            out = openFileOutput("data.txt", MODE_PRIVATE);
            write = new BufferedWriter(new OutputStreamWriter(out));
            write.write(input);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (write != null)
                {
                    write.close();
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

        }

    }


    //文件读取第一步：实现读取方法
    public String load()
    {
        FileInputStream in = null;
        BufferedReader reader =null;
        StringBuilder content = new StringBuilder();
        try
        {
            in = openFileInput("data.txt");
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null)
            {
                content.append(line);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (reader != null)
            {
                try
                {
                    reader.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return content.toString();
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
