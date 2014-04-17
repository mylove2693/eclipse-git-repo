package com.huaqin.punan.smsint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private ViewPager mViewPager;
	private List<View> listViews;
	private ImageView mImageView;
	private TextView tv1, tv2, tv3;
	private int offset = 0;
	private int zero = 0;
	private int one = 0;
	private int two = 0;
	private int currIndex = 1;
	private int bmpWidth;

	//
	private EditText et_phonenum;
	private CheckBox cb_savemsg;
	private Button btn_addNum;
	//
	private ListView lv_num;
	private Button btn_delnum;
	//
	private ListView lv_msg;
	private Button btn_clrmsg;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		InitImageView();
		InitTextView();
		InitViewPager();
		
		getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        
		
	}

	private void InitTextView() {
		tv1 = (TextView) findViewById(R.id.text1);
		tv2 = (TextView) findViewById(R.id.text2);
		tv3 = (TextView) findViewById(R.id.text3);

		tv1.setOnClickListener(new MyOnClickListener(0));
		tv2.setOnClickListener(new MyOnClickListener(1));
		tv3.setOnClickListener(new MyOnClickListener(2));
	}

	private void InitViewPager() {
		mViewPager = (ViewPager) findViewById(R.id.vPager);
		listViews = new ArrayList<View>();
		LayoutInflater mInflater = getLayoutInflater();
		listViews.add(mInflater.inflate(R.layout.view1, null));
		listViews.add(mInflater.inflate(R.layout.view2, null));
		listViews.add(mInflater.inflate(R.layout.view3, null));
		mViewPager.setAdapter(new MyPagerAdapter(listViews));
		mViewPager.setCurrentItem(currIndex);
		mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		
	}

	private void InitImageView() {
		mImageView = (ImageView) findViewById(R.id.cursor);
		bmpWidth = BitmapFactory.decodeResource(getResources(),
				R.drawable.indicate).getWidth();
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;
		offset = (screenW / 3 - bmpWidth) / 2;

		zero = 0;
		one = offset * 2 + bmpWidth;
		two = one * 2;
		
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		mImageView.setImageMatrix(matrix);
		
		mImageView.setVisibility(View.INVISIBLE);
		
	}

	public class MyPagerAdapter extends PagerAdapter {
		public List<View> mListViews;

		public MyPagerAdapter(List<View> mListViews) {
			this.mListViews = mListViews;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(mListViews.get(arg1));
		}

		@Override
		public void finishUpdate(View arg0) {
		}

		@Override
		public int getCount() {
			return mListViews.size();
		}

		@Override
		public Object instantiateItem(View collection, int position) {
			((ViewPager) collection).addView(mListViews.get(position), 0);
			
			cb_savemsg = (CheckBox)findViewById(R.id.cb_savemsg);
			
			et_phonenum = (EditText)findViewById(R.id.et_num);
			
			btn_addNum  = (Button)findViewById(R.id.btn_add);
			btn_addNum.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Toast.makeText(MainActivity.this, "Add New Number", Toast.LENGTH_LONG).show();
				}});
			
			lv_num = (ListView)findViewById(R.id.lv_phonenum);
			btn_delnum = (Button)findViewById(R.id.btn_delete);
			

			lv_msg = (ListView)findViewById(R.id.lv_message);
			btn_clrmsg = (Button)findViewById(R.id.btn_clearmsg);
			
			
	        ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
	        
	        for(int i=0;i<10;i++)
	        {
	        	HashMap<String, Object> map = new HashMap<String, Object>();
	        	map.put("ItemSelected", false);
	        	map.put("ItemTitle", "Level "+i);
	        	map.put("ItemText", "Finished in 1 Min 54 Secs, 70 Moves! ");
	        	listItem.add(map);
	        }
	        
	        SimpleAdapter listItemAdapter = new SimpleAdapter(MainActivity.this,listItem,
	        		R.layout.phonenum_listview,
	        		new String[] {"ItemTitle", "ItemText"},
	        		new int[] {R.id.ItemTitle,R.id.ItemText}
	        		);
	        
			
			if(position == 0){
				btn_delnum.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Toast.makeText(MainActivity.this, "Add New Number", Toast.LENGTH_LONG).show();
					}});
				lv_num.setAdapter(listItemAdapter);
			}else if(position == 1){
				
			}else{
				btn_clrmsg.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Toast.makeText(MainActivity.this, "Add New Number", Toast.LENGTH_LONG).show();
					}});
				lv_msg.setAdapter(listItemAdapter);
			}
			
			
			Toast.makeText(MainActivity.this, "position="+position, Toast.LENGTH_SHORT).show();
			
			return mListViews.get(position);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == (arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}
	}

	public class MyOnClickListener implements View.OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			mViewPager.setCurrentItem(index);
		}
	};

	public class MyOnPageChangeListener implements OnPageChangeListener {
		
		@Override
		public void onPageSelected(int arg0) {
			mImageView.setVisibility(View.VISIBLE);
			Animation animation = null;
			switch (arg0) {
			case 0:
				if (currIndex == 1) {
					animation = new TranslateAnimation(one, 0, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, 0, 0, 0);
				}
				break;
			case 1:
				if (currIndex == 0) {
					animation = new TranslateAnimation(0, one, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, one, 0, 0);
				}
				break;
			case 2:
				if (currIndex == 0) {
					animation = new TranslateAnimation(0, two, 0, 0);
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(one, two, 0, 0);
				}
				break;
			}
			currIndex = arg0;
			animation.setFillAfter(true);
			animation.setDuration(300);
			mImageView.startAnimation(animation);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
