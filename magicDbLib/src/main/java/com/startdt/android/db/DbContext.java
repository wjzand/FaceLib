package com.startdt.android.db;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import java.io.File;

/**
 * Created by wjz on 2019/2/22
 * 设置数据库自定义路径
 */
public class DbContext extends ContextWrapper {
    private File dbFile;

    public DbContext(Context base) {
        super(base);
        dbFile = null;
    }

    public DbContext(Context base,File dbFile) {
        super(base);
        this.dbFile = dbFile;
    }

    @Override
    public File getDatabasePath(String name) {
        if(dbFile != null){
            if(!dbFile.exists()){
                dbFile.mkdir();
            }
            StringBuffer buffer = new StringBuffer();
            buffer.append(dbFile.getPath());
            buffer.append(File.separator);
            buffer.append(name);
            return new File(buffer.toString());
        }
        return super.getDatabasePath(name);
    }

    /**
     *  < 4.0
     * @param name
     * @param mode
     * @param factory
     * @return
     */
    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory) {
        if(dbFile != null){
            return SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name),factory);
        }
        return super.openOrCreateDatabase(name, mode, factory);
    }

    /**
     * > 4.0
     * @param name
     * @param mode
     * @param factory
     * @param errorHandler
     * @return
     */
    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory, DatabaseErrorHandler errorHandler) {
        if(dbFile != null){
            return SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name),factory);
        }
        return super.openOrCreateDatabase(name, mode, factory, errorHandler);
    }
}
