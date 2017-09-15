package cn.flyzy2005.samples.base;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import java.io.IOException;

import cn.flyzy2005.samples.R;
import cn.flyzy2005.samples.SQLiteHelper;


/**
 * Created by Fly on 2017/5/3.
 */

public class BaseApplication extends Application {
    private static BaseApplication instance;
    private SQLiteDatabase database;
    private SQLiteHelper sqLiteHelper;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initDatabase();
    }

    public static BaseApplication getInstance(){
        return instance;
    }

    public SQLiteDatabase getDatabase(){
        return database;
    }

    public void closeDatabase(){
        sqLiteHelper.closeDatabase();
    }


    private void initDatabase(){
        sqLiteHelper = new SQLiteHelper("test.db", "cn.flyzy2005.samples", 1, R.raw.test, this);
        try {
            sqLiteHelper.openDatabase();
        } catch (IOException e) {
            //打开文件失败，文件不存在
            e.printStackTrace();
        }
        database = sqLiteHelper.getDatabase();
    }
}
