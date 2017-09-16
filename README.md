# daoutils
daoutils is a lightweight ORM util for Android to deal with SQLite.
## Usage
1. Using AbstractSQLiteManger to create a database.
2. Creating an entity bean corresponding to each table. You can use annotations(PrimaryKey, ColumnAlias, Ignore) to help you make it.
3. Creating a dao extends AbstractDao<T> for each entity. In its constructor, you have to set database and table name. You also can do anything you want with database got by getDatabase().
4. Then, you can use all the methods included in IBaseDao.
## Samples
1. Create a database.

```
public class SQLiteHelper extends AbstractSQLiteManger {
    /**
     * 构造函数
     *
     * @param databaseName    保存的数据库文件名，如test.db
     * @param packageName     工程包名，如cn.flyzy2005.fzthelper，在工程的build.gradle文件可以看到
     * @param databaseVersion 当前的数据库版本
     * @param databaseRawId   需要写入的database文件所对应的R.raw的id，如R.id.test（把test.db文件拷贝到res下的raw下面即可）
     * @param context         ApplicationContext
     */
    public SQLiteHelper(String databaseName, String packageName, int databaseVersion, int databaseRawId, Context context) {
        super(databaseName, packageName, databaseVersion, databaseRawId, context);
    }

    @Override
    protected void updateDatabase(int oldVersion, int newVersion) {
        if(oldVersion >= newVersion)
            return;

        //根据数据库版本依次更新
        for(int i = oldVersion; i < newVersion; ++i){
            switch (i){
                case 0:
                    //更新操作包括4个步骤
                    //1.将所有表重命名成temp表 String TEMP_TABLE = "ALTER TABLE \"routeline\" RENAME TO \"_temp_routeline\"";
                    //2.建立一个新表
                    //String NEW_TABLE = "CREATE TABLE \"routeline\" (\n" +
                    //"\"ID\"  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                    //        "\"userName\"  TEXT,\n" +
                    //        "\"RouteLine\"  BLOB NOT NULL,\n" +
                    //        "\"beginDate\"  TEXT NOT NULL,\n" +
                    //        "\"stopDate\"  TEXT NOT NULL,\n" +
                    //        "\"upload\"  INTEGER NOT NULL DEFAULT 0\n" +
                    //        ")";
                    //3.转移数据 String INSERT_DATA = "INSERT INTO \"routeline\" (\"ID\", \"userName\", \"RouteLine\", \"beginDate\", \"stopDate\") SELECT \"ID\", \"userName\", \"RouteLine\", \"beginDate\", \"stopDate\" FROM \"_temp_routeline\"";
                    //4.删除temp表 String DROP_TEMP = "drop table _temp_routeline";
                    //依次调用getDatabase().execSQL(sql)即可
                    break;
                case 1:

                    break;
                default:
                    break;
            }
        }
    }
}
```

2. Creating an entity bean.

```
public class BaseBook {
    @PrimaryKey
    private int id;
    @Ignore
    private String test;
    @Ignore
    private String test1;
    @Ignore
    private String test2;
    //... get set
}

public class Book extends BaseBook{
    @ColumnAlias(columnName = "name")
    private String name1;
    private String author;
    private String publisher;
    //... get set
}
```

3. Creating a dao extends AbstractDao<T> for each entity.

```
public class BookDao extends AbstractDao<Book> {
    public BookDao() {
        setDatabase(BaseApplication.getInstance().getDatabase());
        setTableName("book");
    }

    public void myOpera(){
        SQLiteDatabase myDatabase =  getDatabase();
        //...do anything you want with SQLiteDatabase
    }
}
```

4. Then, you can use all the methods included in IBaseDao.

```
interface IBaseDao<T> {
    /**
     * 根据primaryKey进行查询
     * 需要设置{@link cn.flyzy2005.daoutils.anno.PrimaryKey}
     *
     * @param primaryKey 主键的值
     * @return entity实例，没有则为null
     */
    T getByPrimaryKey(Object primaryKey);

    /**
     * 根据条件条件进行查询
     *
     * @param condition 查询条件{"name":"fly"}(封装在JSONObject中)
     * @return 满足条件的第一个实例，没有则为null
     */
    T getByParams(JSONObject condition);

    /**
     * 根据条件条件进行查询
     *
     * @param condition 查询条件{"name":"fly"}(封装在JSONObject中)
     * @return 所有满足条件的entity实例，没有则为空ArrayList
     */
    List<T> listByParams(JSONObject condition);

    /**
     * 查询表中所有数据
     *
     * @return 所有entity实例，没有则为空ArrayList
     */
    List<T> listAll();

    /**
     * 根据sql语句进行查询
     *
     * @param sql sql语句
     * @return 满足条件的第一个实例，没有则为null
     */
    T getBySql(String sql);

    /**
     * 根据sql语句进行查询
     *
     * @param sql sql语句
     * @return 满足查询条件的entity实例，没有则为空ArrayList
     */
    List<T> listBySql(String sql);

    /**
     * 添加一条记录
     *
     * @param model  entity实例
     * @param withId 是否需要插入PrimaryKey，不插入的话就采用SQLite自带的自增策略（不插入需要设置{@link cn.flyzy2005.daoutils.anno.PrimaryKey}）
     * @return 是否插入成功
     */
    boolean insert(T model, boolean withId);

    /**
     * 根据primaryKey删除一条记录
     *
     * @param model entity实例
     * @return 是否删除成功
     */
    boolean delete(T model);

    /**
     * 根据Id删除一条记录
     *
     * @param primaryKey primaryKey
     * @return 是否删除成功
     */
    boolean deleteByPrimaryKey(Object primaryKey);

    /**
     * 根据条件删除一条（多条）记录
     *
     * @param condition 删除条件
     * @return 是否删除成功
     */
    boolean deleteByParams(JSONObject condition);

    /**
     * 删除表中所有数据
     *
     * @return 是否删除成功
     */
    boolean deleteAll();

    /**
     * 修改一条记录，会根据传入的entity的primaryKey进行匹配修改，并且会用除primaryKey之外的所有其他属性的新值更新数据库
     *
     * @param model 修改的entity实例
     * @return 是否修改成功
     */
    boolean update(T model);

    /**
     * 根据条件修改一条记录，并且会用除primaryKey的所有其他属性的新值更新数据库
     *
     * @param model     修改的entity实例
     * @param condition 条件
     * @return 是否修改成功
     */
    boolean updateByParams(T model, JSONObject condition);

    /**
     * 所有行数
     *
     * @return int
     */
    int count();

    /**
     * 满足条件的所有行数
     *
     * @param condition 条件
     * @return int
     */
    int countByParams(JSONObject condition);
}
```
## Dependency
```
compile 'cn.flyzy2005:daoutils:1.0.1'
```