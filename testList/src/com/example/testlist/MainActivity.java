package com.example.testlist;

import java.util.ArrayList;
import java.util.List;




import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private ListView lv; 
	private PopupWindow pw;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		init();
	}
	
	private void init(){
		lv=(ListView)findViewById(R.id.m_list);
		 initList();
	}
	
	private void initList(){
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_expandable_list_item_1,
                getData());
		
        lv.setAdapter(adapter);
        lv.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				Toast.makeText(MainActivity.this, "simsunny", 1000).show();  
				initPop(arg1,arg2);
			
				return false;
			}
		});
		
	}
	
	private ArrayList<String> getData(){
		
		List<String> ls = new ArrayList<String>();
		
		ls.add("海贼王");
		ls.add("火影忍者");
		ls.add("死神");
		
        return (ArrayList<String>) ls;
	}
	
	
	private void initPop(View v,int position){
		
//		View pv= (View)MainActivity.this.getLayoutInflater().inflate(R.layout.pv, null);
		RelativeLayout pv= (RelativeLayout) LayoutInflater.from(MainActivity.this).inflate(
				R.layout.pv, null);
		
		pw= new PopupWindow(MainActivity.this);
		pw.setBackgroundDrawable(new BitmapDrawable());
		pw.setContentView(pv);
		
		TextView tvDel= (TextView)pv.findViewById(R.id.pv_tv_del);
		TextView tvMod= (TextView)pv.findViewById(R.id.pv_tv_mod);
		
		pw.setWidth(getWindowManager().getDefaultDisplay().getWidth() / 3);
		pw.setHeight(60);
		
		pw.setOutsideTouchable(true);
		pw.setFocusable(true);
		
		pw.showAtLocation(v, 
				Gravity.LEFT| Gravity.TOP, 
				getWindowManager().getDefaultDisplay().getWidth() / 4, 
				getStateBar()+v.getHeight()*(position+1));
		
		tvDel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pw.dismiss();
				Intent i = new Intent();
				i.setClass(MainActivity.this, DelActivity.class);
				startActivity(i);
				Toast.makeText(MainActivity.this, "删除", 1000);
				pw= null;
			}
		});
		tvMod.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pw.dismiss();
				Intent i = new Intent();
				i.setClass(MainActivity.this, DelActivity.class);
				startActivity(i);
				Toast.makeText(MainActivity.this, "修改", 1000);
				pw = null;
			}
		});
	}
	
	private int getStateBar(){
		Rect frame = new Rect();
		getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		int statusBarHeight = frame.top;
		System.out.println("---------------------->"+ statusBarHeight);
		return statusBarHeight;
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
