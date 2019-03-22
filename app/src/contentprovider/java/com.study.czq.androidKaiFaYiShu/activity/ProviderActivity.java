package com.study.czq.androidKaiFaYiShu.activity;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;


import com.study.czq.androidKaiFaYiShu.R;
import com.study.czq.androidKaiFaYiShu.base.BaseActivity;
import com.study.czq.androidKaiFaYiShu.entity.Book;
import com.study.czq.androidKaiFaYiShu.entity.User;
import com.study.czq.androidKaiFaYiShu.utils.Trace;


public class ProviderActivity extends BaseActivity {
    private static final String TAG = "ProviderActivity";
    private ContentResolver mCr;
    private static final String URI_STR = "content://BookProvider/user";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider);

        mCr = getContentResolver();
        //往contentprovider插入数据
        initDb();
        //往contentprovider读取数据
        requestDb();


    }

    public void initDb(){
        Uri bookUri = Uri.parse(URI_STR);
        ContentValues values = new ContentValues();
        values.put("_id",6);
        values.put("name","程序设计的艺术");
        mCr.insert(bookUri,values);
        Cursor bookCursor = mCr.query(bookUri,new String[]{"_id","name"},
                null,null,null);
        while (bookCursor.moveToNext()) {
            Book book = new Book();
            book.bookId = bookCursor.getInt(0);
            book.bookName = bookCursor.getString(1);
            Trace.d(TAG,"query book:" + book.toString());
        }
        bookCursor.close();
    }

    public void requestDb(){
        Uri userUri = Uri.parse(URI_STR);
        Cursor userCursor = mCr.query(userUri,new String[]{"_id","name","sex"},
                null,null,null);
        while (userCursor.moveToNext()) {
            User user = new User();
            user.userId = userCursor.getInt(0);
            user.userName = userCursor.getString(1);
            user.isMale = userCursor.getInt(2) == 1;
            Trace.d(TAG,"query user:" + user.toString());
        }
        userCursor.close();
    }
    @Override
    protected int getRLayoutId() {
        return R.layout.activity_provider;
    }
}
