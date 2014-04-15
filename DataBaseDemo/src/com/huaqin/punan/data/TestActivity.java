package com.huaqin.punan.data;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

public class TestActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.test_activity);
		
		//open or create database test.db
		SQLiteDatabase db = openOrCreateDatabase("test.db", Context.MODE_PRIVATE, null);
		db.execSQL("DROP TABLE IF EXISTS person");
		
		//create person table
		db.execSQL("CREATE TABLE person (_id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR, age SMALLINT)");
		
		Person person = new Person();
		person.name = "john";
		person.age = 30;
		
		//insert data
		db.execSQL("INSERT INTO person VALUES (NULL, ?, ?)", new Object[]{person.name, person.age});
		
		person.name = "david";
		person.age = 33;
		
		//insert data use ContentValues
		ContentValues cv = new ContentValues();
		cv.put("name", person.name);
		cv.put("age", person.age);
		
		db.insert("person", null, cv);
		
		//update db
		cv = new ContentValues();
		cv.put("age", 35);
		db.update("person", cv, "name = ?", new String[]{"john"});
		
		//query db
		Cursor c = db.rawQuery("SELECT * FROM person WHERE age >= ?", new String[]{"33"});
		while (c.moveToNext()){
			int _id = c.getInt(c.getColumnIndex("_id"));
			String name = c.getString(c.getColumnIndex("name"));
			int age = c.getInt(c.getColumnIndex("age"));
			
			Log.i("db", "_id=>" + _id + ", name=>" + name + ", age=>" + age);
		}
		c.close();
		
		//delete data
		db.delete("person", "age < ?", new String[]{"35"});
		//close database
		db.close();
		//delete database
		//deleteDatabase("test.db");
	}

}
