package com.baoji.jinlinggongshang.pulltorefresh;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.baoji.jinlinggongshang.R;

public class WanggeDetail extends Activity implements OnItemClickListener,
		OnClickListener {

	private int grid_index;
	private ListView mListView;
	private WanggeListAdapter mAdapter;
	private Button back,refresh;
	Intent callintent;
	public String phonenuminfo[];
	public String callnumbers[];
	TextView connectWay;
	

	public String wanggedetails1[];
	public String wanggedetails2[];
	public String wanggedetails3[];
	public String wanggedetails4[];
	public String wanggedetails5[];
	public String wanggedetails6[];
	public String wanggedetails7[];
	public String wanggedetails8[];
	public String wanggedetails9[];
	
	//网格详情页面中负责人的联系方式
	public int connectWayIds[] = { R.string.wangge_connectWay1,
			R.string.wangge_connectWay2, R.string.wangge_connectWay3,
			R.string.wangge_connectWay4, R.string.wangge_connectWay5,
			R.string.wangge_connectWay6, R.string.wangge_connectWay7,
			R.string.wangge_connectWay8, R.string.wangge_connectWay9 };

	List<TableItem> strings = new ArrayList<TableItem>();
	private List<TableItem> posters = new ArrayList<TableItem>();
	private List<TableItem> mStrings = new ArrayList<TableItem>();// 鏇存柊鍒楄〃鐨勬暟鎹泦鍚?

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.wangge_detail);
		TextView title = (TextView) findViewById(R.id.titleBar_tv);
		back = (Button) findViewById(R.id.back);
		refresh = (Button)findViewById(R.id.refresh);
		refresh.setVisibility(View.GONE);
		back.setOnClickListener(this);
		title.setVisibility(View.VISIBLE);
		title.setText("网格信息");
		
		//从字符串数组中初始化每个网格点击后所对应的网格信息（每个网格的ListView的显示信息）
		Resources rs = getResources();
		wanggedetails1 = rs.getStringArray(R.array.wanggedetails1);
		wanggedetails2 = rs.getStringArray(R.array.wanggedetails2);
		wanggedetails3 = rs.getStringArray(R.array.wanggedetails3);
		wanggedetails4 = rs.getStringArray(R.array.wanggedetails4);
		wanggedetails5 = rs.getStringArray(R.array.wanggedetails5);
		wanggedetails6 = rs.getStringArray(R.array.wanggedetails6);
		wanggedetails7 = rs.getStringArray(R.array.wanggedetails7);
		wanggedetails8 = rs.getStringArray(R.array.wanggedetails8);
		wanggedetails9 = rs.getStringArray(R.array.wanggedetails9);
		
		mListView = (ListView) findViewById(R.id.wanggeDetail_lv);
		mListView.setOnItemClickListener(this);

		connectWay = (TextView) findViewById(R.id.connectWay);
		connectWay.setOnClickListener(this);
		
		LayoutParams params = (LayoutParams) mListView.getLayoutParams();
		params.height = MainTabActivity.screenHeight / 3;
		mListView.setLayoutParams(params);

		mListView.setDividerHeight(4);

		mAdapter = new WanggeListAdapter(this, mStrings);
		mListView.setAdapter(mAdapter);

		grid_index = getIntent().getIntExtra("grid_index", 1);
		switch (grid_index) {
		case 1:
			loadListViewData(wanggedetails1);
			phonenuminfo = wanggedetails1;
			connectWay.setText(getString(connectWayIds[0]));
			callnumbers = getResources().getStringArray(R.array.callnumber1);
			break;
		case 2:
			loadListViewData(wanggedetails2);
			phonenuminfo = wanggedetails2;
			connectWay.setText(getString(connectWayIds[1]));
			callnumbers = getResources().getStringArray(R.array.callnumber2);
			break;
		case 3:
			loadListViewData(wanggedetails3);
			phonenuminfo = wanggedetails3;
			connectWay.setText(getString(connectWayIds[2]));
			callnumbers = getResources().getStringArray(R.array.callnumber3);
			break;
		case 4:
			loadListViewData(wanggedetails4);
			phonenuminfo = wanggedetails4;
			connectWay.setText(getString(connectWayIds[3]));
			callnumbers = getResources().getStringArray(R.array.callnumber4);
			break;
		case 5:
			loadListViewData(wanggedetails5);
			phonenuminfo = wanggedetails5;
			connectWay.setText(getString(connectWayIds[4]));
			callnumbers = getResources().getStringArray(R.array.callnumber5);
			break;
		case 6:
			loadListViewData(wanggedetails6);
			phonenuminfo = wanggedetails6;
			connectWay.setText(getString(connectWayIds[5]));
			callnumbers = getResources().getStringArray(R.array.callnumber6);
			break;
		case 7:
			loadListViewData(wanggedetails7);
			phonenuminfo = wanggedetails7;
			connectWay.setText(getString(connectWayIds[6]));
			callnumbers = getResources().getStringArray(R.array.callnumber7);
			break;
		case 8:
			loadListViewData(wanggedetails8);
			phonenuminfo = wanggedetails8;
			connectWay.setText(getString(connectWayIds[7]));
			callnumbers = getResources().getStringArray(R.array.callnumber8);
			break;
		case 9:
			loadListViewData(wanggedetails9);
			phonenuminfo = wanggedetails9;
			connectWay.setText(getString(connectWayIds[8]));
			callnumbers = getResources().getStringArray(R.array.callnumber9);
			break;
		}
	}

	public void loadListViewData(String details[]) {

		for (int i = 0; i < details.length; i++) {
			TableItem ti = new TableItem();
			ti.setTableText(null);
			ti.setTableTitle(details[i]);
			ti.setCommentsUrl(null);
			ti.setTableId(null);
			ti.setTablePic1(null);
			ti.setTablePic2(null);
			ti.setTableUrl(null);
			ti.setTime(null);
			posters.add(ti);
		}
		for (TableItem body : posters) {
			strings.add(body);
		}
		if (!strings.isEmpty()) {
			mStrings.addAll(strings);
			mAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

		callintent = new Intent(Intent.ACTION_DIAL,
				Uri.parse("tel:"
						+ phonenuminfo[arg2].substring(phonenuminfo[arg2]
								.length() - 11)));
		startActivity(callintent);
		overridePendingTransition(R.anim.rotate_fade_in, R.anim.rotate_fade_out);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.back:
			this.finish();
			overridePendingTransition(R.anim.push_right_in,
					R.anim.push_left_out);
			break;
		case R.id.refresh:
			break;
		case R.id.connectWay:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("选择拨出的号码")
					.setItems(callnumbers,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									switch (which) {
									case 0:
										callintent = new Intent(
												Intent.ACTION_DIAL,
												Uri.parse("tel:"
														+ callnumbers[which]));
										startActivity(callintent);
										overridePendingTransition(
												R.anim.rotate_fade_in,
												R.anim.rotate_fade_out);
										break;
									case 1:
										callintent = new Intent(
												Intent.ACTION_DIAL,
												Uri.parse("tel:"
														+ callnumbers[which]));
										startActivity(callintent);
										overridePendingTransition(
												R.anim.rotate_fade_in,
												R.anim.rotate_fade_out);
										break;
									case 2:
										break;
									}
								}
							}).show();
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			this.finish();
			overridePendingTransition(R.anim.push_right_in,
					R.anim.push_left_out);
		}
		return true;
	}

}
