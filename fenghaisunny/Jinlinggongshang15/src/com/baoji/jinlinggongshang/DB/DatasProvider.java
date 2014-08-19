package com.baoji.jinlinggongshang.DB;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.baoji.jinlinggongshang.DB.Datarecords.Datarecord;



public class DatasProvider extends ContentProvider{

	private DBHelper dbHelper;
	private static final UriMatcher sUriMatcher;

	private static final int DATARECORD = 1;
	private static final int DATARECORD_ID = 2;

	    private static HashMap<String, String> empProjectionMap;
	    static {

	        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	        sUriMatcher.addURI(Datarecords.AUTHORITY, "datarecord", DATARECORD);
	        sUriMatcher.addURI(Datarecords.AUTHORITY, "datarecord/#", DATARECORD_ID);
	        empProjectionMap = new HashMap<String, String>();
	        empProjectionMap.put(Datarecord.TITLE, Datarecord.TITLE);
	    }

 
	public boolean onCreate() {
		dbHelper = new DBHelper(getContext());
		return true;
	}

	public Uri insert(Uri uri, ContentValues values) {

		SQLiteDatabase db = dbHelper.getWritableDatabase();
		long rowId = db.insert(DBHelper.NEWS_TABLE_NAME, Datarecord.TITLE, values);
        if (rowId > 0) {
            Uri empUri = ContentUris.withAppendedId(Datarecord.CONTENT_URI, rowId);
            getContext().getContentResolver().notifyChange(empUri, null);
            return empUri;
        }
		return null;
	}
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		return 0;
	}

	
	// 获得类型
	public String getType(Uri uri) {
		return null;
	}

	// 查询方法
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
	        SQLiteDatabase db = dbHelper.getReadableDatabase();
	        Cursor c =db.query(true, DBHelper.NEWS_TABLE_NAME, projection, 
						selection, selectionArgs, null, null, sortOrder, null);
	        return c;
	}
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		return 0;
	}
}
