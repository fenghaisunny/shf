package com.baoji.jinlinggongshang.pulltorefresh;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.baoji.jinlinggongshang.R;

/**
 * @author yangyu �����������Զ���TabHost
 */
public class MainTabActivity extends FragmentActivity {
	public static int screenWidth, screenHeight;
	// ����FragmentTabHost����
	private  FragmentTabHost mTabHost;

	// ����һ������
	private LayoutInflater layoutInflater;

	// �������������Fragment����
	private Class<?> fragmentArray[] = { FragmentPage1.class,
			FragmentPage2.class, FragmentPage3.class, FragmentPage4.class,
			FragmentPage5.class, FragmentPage6.class };

	// ������������Ű�ťͼƬ
	private int mImageViewArray[] = { R.drawable.tab_jigou_btn,
			R.drawable.tab_news_btn, R.drawable.tab_shequ_btn,
			R.drawable.tab_zhengwu_btn, R.drawable.tab_banshi_btn,
			R.drawable.tab_wangge_btn };

	// Tabѡ�������
	private String mTextviewArray[] = { "��������", "���Ŷ�̬", "��������", "���񹫿�", "����ָ��",
			"μ������" };

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_tab_layout);

		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);

		screenWidth = metric.widthPixels;
		screenHeight = metric.heightPixels;
		initView();
	}

	/**
	 * ��ʼ�����
	 */
	private void initView() {
		// ʵ�������ֶ���
		layoutInflater = LayoutInflater.from(this);

		// ʵ����TabHost���󣬵õ�TabHost
		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

		// �õ�fragment�ĸ���
		int count = fragmentArray.length;

		for (int i = 0; i < count; i++) {
			// Ϊÿһ��Tab��ť����ͼ�ꡢ���ֺ�����
			TabSpec tabSpec = mTabHost.newTabSpec(mTextviewArray[i])
					.setIndicator(getTabItemView(i));
			// ��Tab��ť��ӽ�Tabѡ���
			mTabHost.addTab(tabSpec, fragmentArray[i], null);
			// ����Tab��ť�ı���
			mTabHost.getTabWidget().getChildAt(i)
					.setBackgroundResource(R.drawable.selector_tab_background);
		}
		
		int item_index = getIntent().getIntExtra("item_index", 0);
		mTabHost.setCurrentTab(item_index);
	}

	/**
	 * ��Tab��ť����ͼ�������
	 */
	private View getTabItemView(int index) {
		View view = layoutInflater.inflate(R.layout.tab_item_view, null);

		ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
		imageView.setImageResource(mImageViewArray[index]);

		TextView textView = (TextView) view.findViewById(R.id.textview);
		textView.setText(mTextviewArray[index]);

		return view;
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
