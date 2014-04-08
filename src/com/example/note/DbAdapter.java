package com.example.note;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbAdapter {
	
	public static final String TITLE = "title";
	public static final String BODY = "body";
	public static final String ROW_ID = "_id";
	
	private static final String TAG = "DbAdapter";
	private DatabaseHelper DbHelper;
	private SQLiteDatabase Db;
	
	//Create Database
	public static final String DATABASE_CREATE = "create table notes (_id integer primary key autoincrement, " + "title text not null, body text not null);";
	
	private static final String DATABASE_NAME = "data";
	private static final String DATABASE_TABLE = "notes";
	private static final int DATABASE_VERSION = 2;
	
	private final Context Ctx;
	
	private static class DatabaseHelper extends SQLiteOpenHelper{
		DatabaseHelper(Context context){
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
		
		@Override 
		public void onCreate(SQLiteDatabase db){
			db.execSQL(DATABASE_CREATE);
		}
		
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
			Log.w(TAG, "Upgrading database from version " + oldVersion + "to" + newVersion +",which will destroy the old data");
			db.execSQL("DROP TABLE IF EXIXTS NOTES");
			onCreate(db);
		}
	}
	
	//Constructor
	public DbAdapter(Context ctx){
		this.Ctx = ctx;
	}
	
	//open the database
	public DbAdapter open() throws SQLException{
		DbHelper = new DatabaseHelper(Ctx);
		Db = DbHelper.getWritableDatabase();
		return this;
	}
	
	public void close(){
		DbHelper.close();
	}
	
	//Create a new note using the title and body provided
	public long createNote(String title,String body){
		ContentValues initialValues = new ContentValues();
		initialValues.put(TITLE,title);
		initialValues.put(BODY,body);
		
		return Db.insert(DATABASE_TABLE,null,initialValues);
	}
	
	//Delete note with given row id
	public boolean deleteNote(long rowId){
		return Db.delete(DATABASE_TABLE, ROW_ID + "=" +rowId, null) >0;
	}
	
	//return a cursor over list of notes in the database
		public Cursor fetchAllNotes(){
			return Db.query(DATABASE_TABLE, new String[] {ROW_ID,TITLE,BODY}, null, null, null, null, null);
		}	
		
	//return a cursor positioned at note matching row id
		public Cursor fetchNote(long rowId)throws SQLException{
			Cursor mCursor = Db.query(DATABASE_TABLE, new String[] {ROW_ID,TITLE,BODY}, ROW_ID + "=" +rowId, null, null, null, null);
			if(mCursor != null){
				mCursor.moveToFirst();
			}
			return mCursor;
		}
		
		//update notre using details provided
		public boolean updateNote(long rowId,String title,String body){
			ContentValues args = new ContentValues();
			args.put(TITLE,title);
			args.put(BODY, body);
			return Db.update(DATABASE_TABLE, args, ROW_ID + "=" +rowId, null) > 0;
		}

}
