package com.huaqin.punan.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;

public class DatabaseActivity extends Activity {
	
	private Button btn_test;
	private DBManager mgr;
	private ListView listView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_database);
	
		listView = (ListView)findViewById(R.id.listView);
		//init DBManager
		mgr = new DBManager(this);
		
		btn_test = (Button) findViewById(R.id.btn_test);
		btn_test.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(DatabaseActivity.this, TestActivity.class);
				startActivity(intent);
			}});
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.database, menu);
		return true;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mgr.closeDB();
	}
	
	public void add(View view){
		ArrayList<Person> persons = new ArrayList<Person>();
    	Person person1 = new Person("Ella", 22, "lively girl");
    	Person person2 = new Person("Jenny", 22, "beautiful girl");
    	Person person3 = new Person("Jessica", 23, "sexy girl");
    	Person person4 = new Person("Kelly", 23, "hot baby");
    	Person person5 = new Person("Jane", 25, "a pretty woman");
    	
    	persons.add(person1);
    	persons.add(person2);
    	persons.add(person3);
    	persons.add(person4);
    	persons.add(person5);
    	
    	mgr.add(persons);
	}
	
	public void update(View view){
    	Person person = new Person();
    	person.name = "Jane";
    	person.age = 30;
    	mgr.updateAge(person);
	}
	
	public void delete(View view){
    	Person person = new Person();
    	person.age = 30;
    	mgr.deleteOldPerson(person);
	}
	
	public void query(View view){
    	List<Person> persons = mgr.query();
    	ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
    	for (Person person : persons) {
    		HashMap<String, String> map = new HashMap<String, String>();
    		map.put("name", person.name);
    		map.put("info", person.age + " years old, " + person.info);
    		list.add(map);
    	}
    	SimpleAdapter adapter = new SimpleAdapter(this, list, android.R.layout.simple_list_item_2,
    				new String[]{"name", "info"}, new int[]{android.R.id.text1, android.R.id.text2});
    	listView.setAdapter(adapter);
	}
	
	public void queryTheCursor(View view){
		Cursor c = mgr.queryTheCursor();
		startManagingCursor(c);
		CursorWrapper cursorWrapper = new CursorWrapper(c){

			@Override
			public String getString(int columnIndex) {
				// TODO Auto-generated method stub
				if (getColumnName(columnIndex).equals("info")) {
    				int age = getInt(getColumnIndex("age"));
    				return age + " years old, " + super.getString(columnIndex);
				}
				return super.getString(columnIndex);
			}
		};
		SimpleCursorAdapter adapter = 
				new SimpleCursorAdapter(this, 
						android.R.layout.simple_list_item_2,
						cursorWrapper, 
						new String[]{"name", "info"}, 
						new int[]{android.R.id.text1, android.R.id.text2}
				);
		ListView listView = (ListView) findViewById(R.id.listView);
		listView.setAdapter(adapter);
	}

}
