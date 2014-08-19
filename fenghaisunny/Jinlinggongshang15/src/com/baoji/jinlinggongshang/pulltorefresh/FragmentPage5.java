package com.baoji.jinlinggongshang.pulltorefresh;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
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
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baoji.jinlinggongshang.FolkActivity;
import com.baoji.jinlinggongshang.R;
import com.baoji.jinlinggongshang.DB.DBHelper;
import com.baoji.jinlinggongshang.DB.Datarecords.Datarecord;
import com.baoji.jinlinggongshang.pulltorefresh.PullDownView.OnPullDownListener;
import com.baoji.jinlinggongshang.util.ImageLoaderUtils;
import com.google.zxing.oned.rss.FinderPattern;

public class FragmentPage5 extends Fragment implements OnClickListener,
		OnItemClickListener, OnPullDownListener {

	private Gallery gallery;
	private TabAdapter textAdapter;

	private static final String[] TAB_NAMES = {

	"���ʵǼ�", "���ʵǼ�", "�̱�ע��", "�̱�����","�г�����","�������" };

	DBHelper helper = new DBHelper(getActivity());
	ContentResolver rp = null;
	Uri uri = Datarecord.CONTENT_URI;

	private LinearLayout mTabLayout_One;
	private LinearLayout mTabLayout_Two;
	private LinearLayout mTabLayout_Three;
	private LinearLayout mTabLayout_Four;
	private LinearLayout mTabLayout_Five;
	private LinearLayout mTabLayout_Six;

	private static final int WHAT_DID_LOAD_DATA = 0;// �״μ���
	private static final int WHAT_DID_REFRESH = 1;// ����ˢ��
	private static final int WHAT_DID_MORE = 2;// ��������

	private int currentCount = 8;// ������μ��ص�ListView�����������Ը������������޶���
	private static final int LOADCOUNT = 4;// �Զ���ÿ�μ���ListView�����������Ը������������޶���
	private int jsonSize = 0;// json�������������ֵ����Ϊˢ�µĽ���

	private String url = "http://124.116.242.162:8080/JsonWeb01/JsonServlet";
	private String url2 = "http://124.116.242.162:8080/JsonWeb05/JsonServlet";

	private ListView mListView, mListView2;// PullDownView�е���ǶListView
	private ListAdapter mAdapter;// ΪListView�������ݵ�����������Ϊ�󻷾�Ϊ���PullDownView�����У�

	private Boolean isLoadedItem2 = false;
	private Boolean isLoadedItem1 = false;
	private int itemIndex = 1;// ����item�ı��

	private PullDownView mPullDownView, mPullDownView2;// ʢ��֧��ˢ�µ�ListView������
	private List<TableItem> mStrings = new ArrayList<TableItem>();// ����������������

	private List<TableItem> posters = new ArrayList<TableItem>();// ʢ��ÿ�λ�ȡ��json���ݵ�����
	private List<TableItem> tempposters1 = new ArrayList<TableItem>();//
	private List<TableItem> tempposters2 = new ArrayList<TableItem>();//
	private int pageIndex = 0;//
	private Message msg;//

	public boolean isPlaying_internet = false;
	public boolean isPlaying_local = false;
	public String playdir = "";

	public static final int CAMERA = -2;// ����
	public static final int TWODIMEN = 2;// ��ά��

	String str = null;
	Date date = null;

	Cursor c;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment5, null);
		// activity������Ĭ�ϼ���mPullDownView�����л�Gallery�Ƕ�̬�ı�PullDownView��ʵ��
		mPullDownView = (PullDownView) view.findViewById(R.id.pull_down_view);
		mPullDownView2 = (PullDownView) view.findViewById(R.id.pull_down_view2);
		Button back = (Button)view.findViewById(R.id.back);
		Button refresh = (Button)view.findViewById(R.id.refresh);
		back.setOnClickListener(this);
		refresh.setOnClickListener(this);
		
		TextView titlebar_tv = (TextView) view.findViewById(R.id.titleBar_tv);
		titlebar_tv.setText("����ָ��");

		mAdapter = new ListAdapter(getActivity(), mStrings);

		ImageLoaderUtils.imageLoderIni(getActivity());

		gallery = (Gallery) view.findViewById(R.id.gallery);
		textAdapter = new TabAdapter(getActivity(), TAB_NAMES);
		gallery.setAdapter(textAdapter);
		gallery.setSelection(80);// ����������Tab���Լ���һ�£�����ߵ���΢��һ�㣬��Ҫһ���ͻ���ͷ

		mTabLayout_One = (LinearLayout) view.findViewById(R.id.TabLayout_One);
		mTabLayout_Two = (LinearLayout) view.findViewById(R.id.TabLayout_Two);
		mTabLayout_Three = (LinearLayout) view
				.findViewById(R.id.TabLayout_Three);
		mTabLayout_Four = (LinearLayout) view.findViewById(R.id.TabLayout_Four);
		mTabLayout_Five = (LinearLayout) view.findViewById(R.id.TabLayout_Five);
		mTabLayout_Six = (LinearLayout) view.findViewById(R.id.TabLayout_Six);

		mTabLayout_One.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.back_behind_menu));

		setVisibleTabLayout(View.GONE, View.GONE, View.VISIBLE, View.GONE, View.GONE, View.GONE);
		alignGalleryToLeft(gallery);
		
		gallery.setSelected(true);
		gallery.setSelection(0);

		gallery.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				TabAdapter adapter = (TabAdapter) parent.getAdapter();
				adapter.setSelectedTab(position);
				switch (position % TAB_NAMES.length) {
				case 0:
					setVisibleTabLayout(View.GONE, View.GONE, View.VISIBLE,
							View.GONE,View.GONE,View.GONE);
					break;
				case 1:
					itemIndex = 2;
					setVisibleTabLayout(View.GONE, View.VISIBLE, View.GONE,
							View.GONE,View.GONE,View.GONE);
					changePullView(mPullDownView2, mListView2);
					if (isLoadedItem2) {
						for (int i = 0; i < tempposters2.size(); i++) {
							System.out.println(tempposters2.get(i)
									.getTableTitle());
						}
						mStrings.clear();
						mStrings.addAll(tempposters2);
						mPullDownView2.notifyDidLoad();
					} else {
						posters.clear();
						mStrings.clear();
						mAdapter.notifyDataSetChanged();
						loadData(0);
						isLoadedItem2 = true;
					}
					break;
				case 2:
					itemIndex = 1;
					setVisibleTabLayout(View.VISIBLE, View.GONE, View.GONE,
							View.GONE,View.GONE,View.GONE);
					changePullView(mPullDownView, mListView);
					if (isLoadedItem1) {
						for (int i = 0; i < tempposters1.size(); i++) {
							System.out.println(tempposters1.get(i)
									.getTableTitle());
						}
						posters.clear();
						mStrings.clear();
						mStrings.addAll(tempposters1);
						mAdapter.notifyDataSetChanged();
						mPullDownView.notifyDidLoad();
					} else {
						posters.clear();
						mStrings.clear();
						mAdapter.notifyDataSetChanged();
						loadData(0);
						isLoadedItem1 = true;
					}
					break;
				case 3:
					setVisibleTabLayout(View.GONE, View.GONE, View.GONE,
							View.VISIBLE,View.GONE,View.GONE);
					break;
				case 4:
					setVisibleTabLayout(View.GONE, View.GONE, View.GONE,
							View.GONE,View.VISIBLE,View.GONE);
					break;
				case 5:
					setVisibleTabLayout(View.GONE, View.GONE, View.GONE,
							View.GONE,View.GONE,View.VISIBLE);
					break;
				}
			}
		});

		return view;
	}

	private void alignGalleryToLeft(Gallery gallery) {
		int galleryWidth = MainTabActivity.screenWidth;// �õ�Parent�ؼ��Ŀ��
		// ��������Ǳ����ȴ���Դ�ߴ��еõ��ӿؼ��Ŀ�ȸ���࣬��Ϊ:
		// 1. ������ʱ�������޷��õ����(��ΪGallery����࣬û��������Ȩ��)
		// 2.�п��������еÿ�ȵ�ʱ��item��Դ��û��׼���á�

		int itemWidth = MainTabActivity.screenWidth / 4;
		int spacing = 1;
		// ��ô���ƫ�����Ƕ��٣����ǽ���ߵ�gallery����ģ��ĵ�һ��������
		int offset;
		if (galleryWidth <= itemWidth) {
			offset = galleryWidth / 2 - itemWidth / 2 - spacing;
		} else {
			offset = galleryWidth - itemWidth - 2 * spacing;
		}
		// ���ھͿ��Ը��ݸ��µĲ��ֲ��������������ˡ�
		MarginLayoutParams mlp = (MarginLayoutParams) gallery.getLayoutParams();
		mlp.setMargins(-offset, mlp.topMargin, mlp.rightMargin,
				mlp.bottomMargin);
	}

	public void setVisibleTabLayout(int visibleOrGone1, int visibleOrGone2,
			int visibleOrGone3, int visibleOrGone4,int visibleOrGone5, int visibleOrGone6) {

		mTabLayout_One.setVisibility(visibleOrGone1);
		mTabLayout_Two.setVisibility(visibleOrGone2);
		mTabLayout_Three.setVisibility(visibleOrGone3);
		mTabLayout_Four.setVisibility(visibleOrGone4);
		mTabLayout_Five.setVisibility(visibleOrGone5);
		mTabLayout_Six.setVisibility(visibleOrGone6);
	}

	/**
	 * 2014-4-4
	 * 
	 * @author Sunhaifeng
	 * @param pv
	 *            Ҫ���ɵ�PullDownView
	 * @param lv
	 *            ���ɵ�PullDownView��ʢ�ŵ�ListView
	 *            description:��̬�ı�PullDownViewʵ��������ӦGallery���л�
	 */
	public void changePullView(PullDownView pv, ListView lv) {
		pv.setOnPullDownListener(this);
		pv.setVerticalFadingEdgeEnabled(false);
		lv = pv.getListView();
		lv.setDivider(getResources().getDrawable(R.drawable.whitedivider));
		lv.setPadding(30, 20, 30, 20);
		lv.setOnItemClickListener(this);
		lv.setAdapter(mAdapter);
		pv.enableAutoFetchMore(true, 1);
	}

	private void loadData(final int type) {

		new Thread(new Runnable() {

			@Override
			public void run() {

				if (type == 0) {
					if (itemIndex == 2) {
						currentCount = 8;
						getJsonData(0, currentCount, url2);
						if(tempposters2.size() == 0){
							tempposters2.addAll(posters);
						}
					} else if (itemIndex == 1) {
						currentCount = 8;
						getJsonData(0, currentCount, url);
						if(tempposters1.size() == 0){
							tempposters1.addAll(posters);
						}
					}
					System.out.println("��ʼ���ص�һҳ");
					System.out.println(posters.size());
				}
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				pageIndex = pageIndex + 1;
				switch (type) {
				case 0:
					msg = mUIHandler.obtainMessage(WHAT_DID_LOAD_DATA);
					break;
				case 1:
					msg = mUIHandler.obtainMessage(WHAT_DID_MORE);
					break;
				case 2:
					msg = mUIHandler.obtainMessage(WHAT_DID_REFRESH);
					pageIndex = 1;
					break;

				default:
					break;
				}
				msg.sendToTarget();
			}
		}).start();
	}

	/**
	 * 2014-4-4
	 * 
	 * @author Sunhaifeng
	 * @param startIndex
	 *            ��ȡJson���ݵ����
	 * @param endIndex
	 *            ��ȡJson���ݵ��յ�
	 * @param url
	 *            Ϊʵ��ListView���ݵĶ�̬���ط�װ�ķ���
	 */
	public void getJsonData(int startIndex, int endIndex, String url) {

		List<TableItem> strings = new ArrayList<TableItem>();

		try {
			DefaultHttpClient mHttpClient = new DefaultHttpClient();
			HttpPost mPost = new HttpPost(url);
			HttpResponse response = mHttpClient.execute(mPost);
			HttpEntity entity = response.getEntity();
			String info = EntityUtils.toString(entity);

			System.out.println(info);

			JSONArray array = new JSONArray(info);
			jsonSize = array.length();
			for (int i = startIndex; i < endIndex; i++) {
				TableItem ti = new TableItem();
				JSONObject json = array.getJSONObject(i);

				String name = json.getString("name");
				String addr = json.getString("address");
				String imageurl = json.getString("imageUrl");

				ti.setTableUrl(imageurl);
				ti.setTableText(name);
				ti.setTableTitle(addr);
				ti.setCommentsUrl(null);
				ti.setTableId(null);
				ti.setTime(null);
				posters.add(ti);
			}
			for (TableItem body : posters) {
				strings.add(body);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	@Override
	public void onRefresh() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				loadData(2);
			}
		}).start();
	}

	/**
	 * ���ظ��च
	 */
	@Override
	public void onMore() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				loadData(1);
			}
		}).start();
	}

	private Handler mUIHandler = new Handler() {

		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case WHAT_DID_LOAD_DATA: {
				try {
					System.out.println(posters.size());
					List<TableItem> strings = new ArrayList<TableItem>();
					if (posters != null) {
						for (int i = 0; i < currentCount; i++) {
							strings.add(posters.get(i));
						}
						if (!strings.isEmpty()) {
							mStrings.addAll(strings);
							mAdapter.notifyDataSetChanged();
						}
					}
					mAdapter.notifyDataSetChanged();
					// ���ݼ������
					if (itemIndex == 1) {
						mPullDownView.notifyDidLoad();
					} else if (itemIndex == 2) {
						mPullDownView2.notifyDidLoad();
					}

					break;
				} catch (IndexOutOfBoundsException e) {
					e.printStackTrace();
					if (itemIndex == 1) {
						posters.clear();
						mStrings.clear();
						mAdapter.notifyDataSetChanged();
						isLoadedItem1 = false;
					} else if (itemIndex == 2) {
						isLoadedItem2 = false;
						posters.clear();
						mStrings.clear();
						mAdapter.notifyDataSetChanged();
					}
				}

			}
			case WHAT_DID_REFRESH: {
				try {
					List<TableItem> strings = new ArrayList<TableItem>();
					currentCount += LOADCOUNT;
					if (currentCount > jsonSize) {
						Toast.makeText(getActivity(), "�ף�û�и�����Ŷ��",
								Toast.LENGTH_SHORT).show();
						posters.clear();
						if (itemIndex == 1) {
							getJsonData(currentCount - LOADCOUNT, jsonSize, url);
							strings.addAll(posters);
							if (!strings.isEmpty()) {
								System.out.println(strings.size());
								mStrings.addAll(strings);
								mAdapter.notifyDataSetChanged();
							}
							mPullDownView.notifyDidRefresh();
						} else if (itemIndex == 2) {
							getJsonData(currentCount - LOADCOUNT, jsonSize,
									url2);
							strings.addAll(posters);
							if (!strings.isEmpty()) {
								System.out.println(strings.size());
								mStrings.addAll(strings);
								mAdapter.notifyDataSetChanged();
							}
							mPullDownView2.notifyDidRefresh();
						}
						break;
					}
					posters.clear();
					if (itemIndex == 1) {
						getJsonData(currentCount - LOADCOUNT, currentCount, url);
						strings.addAll(posters);
						if (!strings.isEmpty()) {
							System.out.println(strings.size());
							mStrings.addAll(strings);
							mAdapter.notifyDataSetChanged();
						}
						mPullDownView.notifyDidRefresh();
					} else if (itemIndex == 2) {
						getJsonData(currentCount - LOADCOUNT, currentCount,
								url2);
						strings.addAll(posters);
						if (!strings.isEmpty()) {
							System.out.println(strings.size());
							mStrings.addAll(strings);
							mAdapter.notifyDataSetChanged();
						}
						mPullDownView2.notifyDidRefresh();
					}
					break;

				} catch (IndexOutOfBoundsException e) {
					e.printStackTrace();
					if (itemIndex == 1) {
						isLoadedItem1 = false;
						posters.clear();
						mStrings.clear();
						mAdapter.notifyDataSetChanged();

					} else if (itemIndex == 2) {
						isLoadedItem2 = false;
						posters.clear();
						mStrings.clear();
						mAdapter.notifyDataSetChanged();
					}
				}

			}
			case WHAT_DID_MORE: {
				List<TableItem> strings = new ArrayList<TableItem>();
				currentCount += LOADCOUNT;
				if (currentCount > jsonSize) {
					Toast.makeText(getActivity(), "�ף�û�и�����Ŷ��",
							Toast.LENGTH_SHORT).show();
					posters.clear();
					if (itemIndex == 1) {
						getJsonData(currentCount - LOADCOUNT, jsonSize, url);
						strings.addAll(posters);
						if (!strings.isEmpty()) {
							mStrings.addAll(strings);
							mAdapter.notifyDataSetChanged();
						}
						mPullDownView.notifyDidMore();
					} else if (itemIndex == 2) {
						getJsonData(currentCount - LOADCOUNT, jsonSize, url2);
						strings.addAll(posters);
						if (!strings.isEmpty()) {
							mStrings.addAll(strings);
							mAdapter.notifyDataSetChanged();
						}
						mPullDownView2.notifyDidMore();
					}

					break;
				}
				posters.clear();
				if (itemIndex == 1) {
					getJsonData(currentCount - LOADCOUNT, currentCount, url);
					strings.addAll(posters);
					if (!strings.isEmpty()) {
						System.out.println(strings.size());
						mStrings.addAll(strings);
						mAdapter.notifyDataSetChanged();
					}
					mPullDownView.notifyDidMore();
				} else if (itemIndex == 2) {
					getJsonData(currentCount - LOADCOUNT, currentCount, url2);
					strings.addAll(posters);
					if (!strings.isEmpty()) {
						System.out.println(strings.size());
						mStrings.addAll(strings);
						mAdapter.notifyDataSetChanged();
					}
					mPullDownView2.notifyDidMore();
				}

				break;
			}
			}
		}

	};

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		gallery.setSelected(true);
		gallery.setSelection(0);
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		gallery.setSelected(true);
		gallery.setSelection(0);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (position == 0) {
			Intent intent = new Intent(getActivity(), FolkActivity.class);
			getActivity().startActivity(intent);
		}
		System.out.println(mStrings.get(position).getTableUrl());
	}

	@SuppressLint("SimpleDateFormat")
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.back){
			getActivity().finish();
			getActivity().overridePendingTransition(R.anim.push_right_in,
					R.anim.push_left_out);
		}else if(v.getId() == R.id.refresh){
			posters.clear();
			mStrings.clear();
			mAdapter.notifyDataSetChanged();
			loadData(0);
		}
	}

}