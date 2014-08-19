package com.baoji.jinlinggongshang.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.baoji.jinlinggongshang.DB.Datarecords.Datarecord;





public class DBHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "news.db";

    private static final int DATABASE_VERSION = 1;
    public static final String NEWS_TABLE_NAME = "news";


	public DBHelper(Context context) {

		super(context, DATABASE_NAME,null, DATABASE_VERSION);
	}


	public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + NEWS_TABLE_NAME + " ("
        		+ Datarecord._ID + " INTEGER PRIMARY KEY,"
                + Datarecord.TITLE + " TEXT,"+ Datarecord.CONTENT + " TEXT, "
                + Datarecord.IMAGE_URL + " TEXT "
                + ");");
	}


	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS employee");
        onCreate(db);
	}

}
