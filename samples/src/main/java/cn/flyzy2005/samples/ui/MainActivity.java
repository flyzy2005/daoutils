package cn.flyzy2005.samples.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

import cn.flyzy2005.daoutils.DataBaseConfig;
import cn.flyzy2005.samples.R;
import cn.flyzy2005.samples.dao.BookDao;
import cn.flyzy2005.samples.entity.Book;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void query(View view) {
        BookDao bookDao = new BookDao();
        Book bookInsert = new Book();
        bookInsert.setId(1);//并不会用到
        bookInsert.setPublisher("whu2");
        bookInsert.setName1("心灵鸡汤1");
        bookInsert.setAuthor("fly1");
        if (bookDao.insert(bookInsert, false)) {
            Log.i(TAG, "insert: " + "插入成功，id采用自增模式");
        }
        bookInsert.setId(15);//会用这个作为id插入到表中
        bookInsert.setPublisher("whu2");
        bookInsert.setAuthor("fly2");
        bookInsert.setName1("心灵鸡汤2");
        if (bookDao.insert(bookInsert, true)) {
            Log.i(TAG, "insert: " + "插入成功， id为设置的id");
        }

        Book bookFind = bookDao.getByPrimaryKey(1);
        Log.i(TAG, "find: " + "根据id找到book：" + JSON.toJSONString(bookFind));

        List<Book> bookList1 = bookDao.listAll();
        Log.i(TAG, "find: " + "查询出所有book：" + JSON.toJSONString(bookList1));

        bookDao.deleteAll();

        JSONObject condition = new JSONObject();
        condition.put("author", "fly1");
        condition.put("publisher" + DataBaseConfig.LIKE_END_STRING, "whu%");
        List<Book> bookList2 = bookDao.listByParams(condition);
        Log.i(TAG, "find: " + "根据条件查询出所有book：" + JSON.toJSONString(bookList2));

        String sql = "select * from book where author = 'fly1'";
        List<Book> bookList3 = bookDao.listBySql(sql);
        Log.i(TAG, "find: " + "根据sql语句查询出所有book：" + JSON.toJSONString(bookList3));

        if (bookDao.deleteByPrimaryKey(7)) {
            Log.i(TAG, "delete: " + "根据id删除成功，成功删除id为7的book");
        }


        Book bookDelete = new Book();
        bookDelete.setId(1);
        if (bookDao.delete(bookDelete)) {
            Log.i(TAG, "delete: " + "根据model删除成功，成功删除实体bookDelete，实质是删除id为14的book");
        }

        Book bookModify = new Book();
        bookModify.setId(2);
        bookModify.setAuthor("flyModify");
        bookModify.setName1("心灵鸡汤Modify");
        bookModify.setPublisher("whuModify");
        if (bookDao.update(bookModify)) {
            Log.i(TAG, "update: " + "成功修改id为" + bookModify.getId() + "的书籍，书籍信息修改为：" + JSON.toJSONString(bookModify));
        }

        condition = new JSONObject();
        condition.put("author", "fly1");
        condition.put("publisher", "whu1");
        if (bookDao.updateByParams(bookModify, condition)) {
            Log.i(TAG, "update: " + "成功修改满足条件" + condition + "的书籍，书籍信息修改为：" + JSON.toJSONString(bookModify));
        }

        condition = new JSONObject();
        condition.put("author", "flyModify");
        condition.put("publisher", "whuModify");
        if (bookDao.deleteByParams(condition)) {
            Log.i(TAG, "delete: " + "成功删除满足条件" + condition + "的书籍");
        }


    }
}
