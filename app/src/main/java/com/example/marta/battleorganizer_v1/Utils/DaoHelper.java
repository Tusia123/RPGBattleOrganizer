package com.example.marta.battleorganizer_v1.Utils;

import android.app.Application;

import org.greenrobot.greendao.database.Database;


public class DaoHelper extends Application {

    private String TAG = Utils.getTag(this.getClass().getSimpleName());
    private DaoSession mDaoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "notes-db");
        Database db = helper.getWritableDb();
        mDaoSession = new DaoMaster(db).newSession();

//            DaoMaster.dropAllTables(db, true);
//            DaoMaster.createAllTables(db, true);
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }
}