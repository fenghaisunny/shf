package com.baoji.jiuguijiu.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.baoji.jiuguijiu.DB.Datarecords.Datarecord;





public class DBHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "Love.db";

    private static final int DATABASE_VERSION = 1;
    public static final String LOVE_TABLE_NAME = "love";


	public DBHelper(Context context) {

		super(context, DATABASE_NAME,null, DATABASE_VERSION);
	}


	public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + LOVE_TABLE_NAME + " ("
        		+ Datarecord._ID + " INTEGER PRIMARY KEY,"
                + Datarecord.NAME + " TEXT"
                + ");");
	}


	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS employee");
        onCreate(db);
	}

}
