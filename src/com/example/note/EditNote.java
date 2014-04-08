package com.example.note;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditNote extends Activity{
	
	private EditText titleText;
	private EditText bodyText;
	private Long rowId;
	private DbAdapter DbHelper;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		DbHelper = new DbAdapter(this);
		DbHelper.open();
		
		setContentView(R.layout.edit_note);
		setTitle(R.string.edit_note);
		
		titleText = (EditText)findViewById(R.id.title);
		bodyText = (EditText)findViewById(R.id.body);
		
		Button confirmBtn = (Button)findViewById(R.id.confirm);
		
		rowId = (savedInstanceState == null) ? null :
			(Long) savedInstanceState.getSerializable(DbAdapter.ROW_ID);
		if(rowId == null){
			Bundle extras = getIntent().getExtras();
			rowId = extras != null ? extras.getLong(DbAdapter.ROW_ID) : null;
		}
		
		populateFields();
		
		confirmBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			  setResult(RESULT_OK);
			  finish();
			}
		});
	}
	
	private void populateFields(){
		if(rowId != null){
			Cursor note = DbHelper.fetchNote(rowId);
			startManagingCursor(note);
			titleText.setText(note.getString(note.getColumnIndex(DbAdapter.TITLE)));
			bodyText.setText(note.getString(note.getColumnIndex(DbAdapter.BODY)));
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		saveState();
		outState.putSerializable(DbAdapter.ROW_ID, rowId);
	}
	
	@Override
	protected void onPause(){
		super.onPause();
		saveState();
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		populateFields();
	}
	
	private void saveState(){
		String title = titleText.getText().toString();
		String body = bodyText.getText().toString();
		
		if(rowId ==null){
			long id = DbHelper.createNote(title, body);
			if(id>0){
				rowId = id;
			}
		}else{
			DbHelper.updateNote(rowId, title, body);
		}
	}
	
}
