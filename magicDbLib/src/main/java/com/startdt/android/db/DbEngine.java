package com.startdt.android.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.startdt.android.db.dao.DaoMaster;
import com.startdt.android.db.dao.DaoSession;

import org.greenrobot.greendao.query.QueryBuilder;

import java.io.File;

/**
 * Created by wjz on 2019/2/22
 * db 引擎
 */
public class DbEngine {
    private static DaoSession daoSession;
    private static final String DBNAME = "magic.db";

    /**
     * 默认是系统db路径
     * @param context
     */
    public static void init(Context context){
        init(context,null,DBNAME);
    }

    public static void init(Context context, File dbOutFile){
       init(context,dbOutFile,DBNAME);
    }

    /**
     * @param context
     * @param dbOutFile  是否自定义输出db文件路径
     * @param dbName db name
     */
    public static void init(Context context,File dbOutFile,String dbName){
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(new DbContext(context,dbOutFile),dbName);
        SQLiteDatabase sqLiteDatabase = devOpenHelper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(sqLiteDatabase);
        daoSession = daoMaster.newSession();
    }

    /**
     * 日志输出
     * @param isDebug
     */
    public static void isDebug(boolean isDebug){
        QueryBuilder.LOG_SQL = isDebug;
        QueryBuilder.LOG_VALUES = isDebug;
    }

    public static DaoSession getDaoSession(){
        if(daoSession == null) throw new RuntimeException("please init dbEngine first");
        return daoSession;
    }
}
