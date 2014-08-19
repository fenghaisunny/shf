package com.baoji.jinlinggongshang.pulltorefresh;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baoji.jinlinggongshang.MainActivity;
import com.baoji.jinlinggongshang.R;
import com.baoji.jinlinggongshang.DB.DBHelper;
import com.baoji.jinlinggongshang.DB.Datarecords.Datarecord;

public class FragmentPage6 extends Fragment implements OnClickListener {

	DBHelper helper = new DBHelper(getActivity());
	ContentResolver rp = null;
	Uri uri = Datarecord.CONTENT_URI;

	private View view;
	private Timer timer;
	Context context;
	Intent wanggedetail;

	private LinearLayout map, grid1, grid2, grid3, grid4, grid5, grid6, grid7,
			grid8, grid9;

	public static final int CAMERA = -2;// 拍照
	public static final int TWODIMEN = 2;// 二维码

	public static String wanggedetails1[] = { "好乐迪KTV： 09171234567",
			"好乐迪KTV： 09171234567", "好乐迪KTV： 09171234567",
			"好乐迪KTV： 09171234567", "好乐迪KTV： 09171234567", "好乐迪KTV： 09171234567" };
	public static String wanggedetails2[] = { "凯乐迪KTV： 09171234567",
			"凯乐迪KTV： 09171234567", "凯乐迪KTV： 09171234567",
			"好乐迪KTV： 09171234567", "凯乐迪KTV： 09171234567", };
	public static String wanggedetails3[] = { "联盟宾馆： 09171234567",
			"联盟宾馆： 09171234567", "联盟宾馆： 09171234567" };
	public static String wanggedetails4[] = { "联盟宾馆： 09171234567",
			"联盟宾馆： 09171234567", "联盟宾馆： 09171234567", };
	public static String wanggedetails5[] = { "好乐迪KTV： 09171234567",
			"好乐迪KTV： 09171234567", "好乐迪KTV： 09171234567",
			"好乐迪KTV： 09171234567", "好乐迪KTV： 09171234567", "好乐迪KTV： 09171234567" };
	public static String wanggedetails6[] = { "好乐迪KTV： 09171234567",
			"好乐迪KTV： 09171234567", "好乐迪KTV： 09171234567",
			"好乐迪KTV： 09171234567", "好乐迪KTV： 09171234567", "好乐迪KTV： 09171234567" };
	public static String wanggedetails7[] = { "好乐迪KTV： 09171234567",
			"好乐迪KTV： 09171234567", "好乐迪KTV： 09171234567",
			"好乐迪KTV： 09171234567", "好乐迪KTV： 09171234567", "好乐迪KTV： 09171234567" };
	public static String wanggedetails8[] = { "好乐迪KTV： 09171234567",
			"好乐迪KTV： 09171234567", "好乐迪KTV： 09171234567",
			"好乐迪KTV： 09171234567", "好乐迪KTV： 09171234567", "好乐迪KTV： 09171234567" };
	public static String wanggedetails9[] = { "好乐迪KTV： 09171234567",
			"好乐迪KTV： 09171234567", "好乐迪KTV： 09171234567",
			"好乐迪KTV： 09171234567", "好乐迪KTV： 09171234567", "好乐迪KTV： 09171234567" };
	public static int connectWayIds[] = { R.string.wangge_connectWay1,
			R.string.wangge_connectWay2, R.string.wangge_connectWay3,
			R.string.wangge_connectWay4, R.string.wangge_connectWay5,
			R.string.wangge_connectWay6, R.string.wangge_connectWay7,
			R.string.wangge_connectWay8, R.string.wangge_connectWay9 };

	String str = null;
	Date date = null;

	public static int mapWith, mapHeight;
	private List<String> session = new ArrayList<String>();

	Cursor c;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.fragment6, null);
		map = (LinearLayout) view.findViewById(R.id.map);
		final Handler myHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					if (map.getWidth() != 0) {
						mapWith = map.getWidth();
						mapHeight = map.getHeight();
						// 刷新视图，以免网格在Fragment初始化的时候网格未绘制成功
						view.postInvalidate();
						timer.cancel();
					}
				}
			}
		};

		timer = new Timer();
		TimerTask task = new TimerTask() {
			public void run() {
				Message message = new Message();
				message.what = 1;
				myHandler.sendMessage(message);
			}
		};
		// 延迟每次延迟10 毫秒 隔1秒执行一次
		timer.schedule(task, 10, 1000);

		/*
		 * for(int i=0;i<wanggedetails.length;i++){
		 * session.add(wanggedetails[i]); }
		 */
		grid1 = (LinearLayout) view.findViewById(R.id.grid1);
		grid2 = (LinearLayout) view.findViewById(R.id.grid2);
		grid3 = (LinearLayout) view.findViewById(R.id.grid3);
		grid4 = (LinearLayout) view.findViewById(R.id.grid4);
		grid5 = (LinearLayout) view.findViewById(R.id.grid5);
		grid6 = (LinearLayout) view.findViewById(R.id.grid6);
		grid7 = (LinearLayout) view.findViewById(R.id.grid7);
		grid8 = (LinearLayout) view.findViewById(R.id.grid8);
		grid9 = (LinearLayout) view.findViewById(R.id.grid9);

		Button back = (Button) view.findViewById(R.id.back);
		Button refresh = (Button) view.findViewById(R.id.refresh);
		refresh.setVisibility(View.GONE);

		grid1.setOnClickListener(this);
		grid2.setOnClickListener(this);
		grid3.setOnClickListener(this);
		grid4.setOnClickListener(this);
		grid5.setOnClickListener(this);
		grid6.setOnClickListener(this);
		grid7.setOnClickListener(this);
		grid8.setOnClickListener(this);
		grid9.setOnClickListener(this);
		back.setOnClickListener(this);
		refresh.setOnClickListener(this);

		TextView titlebar_tv = (TextView) view.findViewById(R.id.titleBar_tv);
		titlebar_tv.setText("渭滨网格");

		context = view.getContext();
		wanggedetail = new Intent(getActivity(), WanggeDetail.class);
		wanggedetail.putExtra("session", (Serializable) session);

		return view;

	}

	@SuppressLint("SimpleDateFormat")
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.back:
			getActivity().finish();
			getActivity().overridePendingTransition(R.anim.push_right_in,
					R.anim.push_left_out);
			break;
		case R.id.refresh:
			view.postInvalidate();
			break;
		case R.id.grid1:
			wanggedetail.putExtra("grid_index", 1);
			context.startActivity(wanggedetail);
			getActivity().overridePendingTransition(R.anim.push_down_in,
					R.anim.push_down_out);
			break;
		case R.id.grid2:
			wanggedetail.putExtra("grid_index", 2);
			context.startActivity(wanggedetail);
			getActivity().overridePendingTransition(R.anim.push_down_in,
					R.anim.push_down_out);
			break;
		case R.id.grid3:
			wanggedetail.putExtra("grid_index", 3);
			context.startActivity(wanggedetail);
			getActivity().overridePendingTransition(R.anim.push_down_in,
					R.anim.push_down_out);
			break;
		case R.id.grid4:
			wanggedetail.putExtra("grid_index", 4);
			context.startActivity(wanggedetail);
			getActivity().overridePendingTransition(R.anim.push_down_in,
					R.anim.push_down_out);
			break;
		case R.id.grid5:
			wanggedetail.putExtra("grid_index", 5);
			context.startActivity(wanggedetail);
			getActivity().overridePendingTransition(R.anim.push_down_in,
					R.anim.push_down_out);
			break;
		case R.id.grid6:
			wanggedetail.putExtra("grid_index", 6);
			context.startActivity(wanggedetail);
			getActivity().overridePendingTransition(R.anim.push_down_in,
					R.anim.push_down_out);
			break;
		case R.id.grid7:
			wanggedetail.putExtra("grid_index", 7);
			context.startActivity(wanggedetail);
			getActivity().overridePendingTransition(R.anim.push_down_in,
					R.anim.push_down_out);
			break;
		case R.id.grid8:
			wanggedetail.putExtra("grid_index", 8);
			context.startActivity(wanggedetail);
			getActivity().overridePendingTransition(R.anim.push_down_in,
					R.anim.push_down_out);
			break;
		case R.id.grid9:
			wanggedetail.putExtra("grid_index", 9);
			context.startActivity(wanggedetail);
			getActivity().overridePendingTransition(R.anim.push_down_in,
					R.anim.push_down_out);
			break;
		}
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		view.postInvalidate();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		view.postInvalidate();
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		view.postInvalidate();
	}

}