package com.example.note;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;

public class Notepad extends ListActivity {
	
	public static final int ACTIVITY_CREATE = 0;
	public static final int ACTIVITY_EDIT = 1;
			
	public static final int INSERT_ID = Menu.FIRST;
	public static final int DELETE_ID = Menu.FIRST + 1;

	private DbAdapter DbHelper;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notepad);
		DbHelper = new DbAdapter(this);
		DbHelper.open();
		fillData();
		registerForContextMenu(getListView());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0,INSERT_ID,0,R.string.menu_insert);
		return true;
	}
	
	@Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()){
        case INSERT_ID:
        	createNote();
        	return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }
	
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, DELETE_ID, 0, R.string.menu_delete);
	}
	

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()){
		case DELETE_ID:
			AdapterContextMenuInfo info = (AdapterContextMenuInfo)item.getMenuInfo();
			DbHelper.deleteNote(info.id);
			fillData();
			return true;
		}
		return super.onContextItemSelected(item);
	}

	public void createNote(){
		Intent i = new Intent(this, EditNote.class);
		startActivityForResult(i, ACTIVITY_CREATE);
	}
	
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		
		Intent i = new Intent(this, EditNote.class);
		i.putExtra(DbAdapter.ROW_ID, id);
		startActivityForResult(i, ACTIVITY_EDIT);
	}
	
	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, intent);
		fillData();
	}

	private void fillData(){
		//Get all notes from database and create item list
		Cursor c = DbHelper.fetchAllNotes();
		startManagingCursor(c);
		
		//Create an array to specify fields we want to display in the list
		String[] from =  new String[] {DbAdapter.TITLE};
		
		//an an array of the fields we want to bind those fields to
		int[] to = new int[] {R.id.text1};
		
		//Create an array adapter and set it to display using our row
		SimpleCursorAdapter notes = new SimpleCursorAdapter(this,R.layout.note, c, from, to);
		setListAdapter(notes);
	}


}
