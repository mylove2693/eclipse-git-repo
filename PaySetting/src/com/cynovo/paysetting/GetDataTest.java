package com.cynovo.paysetting;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class GetDataTest extends Activity implements OnClickListener{

	private TextView tv_getdata;
	private Button btn_getdata;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_getdata_test);
		
		tv_getdata = (TextView)findViewById(R.id.tv_testdata);
		btn_getdata = (Button)findViewById(R.id.btn_getdata);
		
		btn_getdata.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		
		ContentResolver cr = GetDataTest.this.getContentResolver();
		Uri uri = Uri.parse("content://com.cynovo.paysetting.PaySettingProvider");
		
		Cursor c = cr.query(uri, null, null, null, null);
		if(c == null){
			Toast.makeText(GetDataTest.this, "C == null", Toast.LENGTH_SHORT).show();
			return;
		}
		
		c.moveToFirst();
		Map<String, Boolean> map = new HashMap<String, Boolean>();
		
		StringBuilder sb  = new StringBuilder();
		
		for(int i=0; i<c.getCount(); i++){
			map.put(c.getString(c.getColumnIndex("key")), Boolean.parseBoolean(c.getString(c.getColumnIndex("value"))));
			
			sb.append(c.getString(c.getColumnIndex("key")));
			sb.append(" : ");
			sb.append(c.getString(c.getColumnIndex("value")));
			sb.append("\n");
			c.moveToNext();
		}
		tv_getdata.setText(sb.toString());
		
		boolean yinlianEnable = (Boolean)map.get("yinlian");
		boolean zhifubaoEnable = (Boolean)map.get("zhifubao");
		boolean weixinEnable = (Boolean)map.get("weixin");
		boolean yibaoEnable = (Boolean)map.get("yibao");
		
	}

}
