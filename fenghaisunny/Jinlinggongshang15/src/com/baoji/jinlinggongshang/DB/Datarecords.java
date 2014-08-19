package com.baoji.jinlinggongshang.DB;

import android.net.Uri;
import android.provider.BaseColumns;

public final class Datarecords {
	
	public static final String AUTHORITY = "com.baoji.provider.Datarecords";
	
	public static final class Datarecord implements BaseColumns {

        private Datarecord() {}
        
        /*public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.shf.Numberrecords";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.shf.Numberrecords";*/
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/Datarecord");
        public static final String TITLE = "newstitle";	
        public static final String CONTENT = "newscontent";
        public static final String IMAGE_URL = "imageurl";
        
				
    }

}
