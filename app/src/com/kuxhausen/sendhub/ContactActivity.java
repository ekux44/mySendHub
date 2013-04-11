package com.kuxhausen.sendhub;
import java.util.ArrayList;

import com.kuxhausen.sendhub.persistence.DatabaseDefinitions.ContactColumns;
import com.kuxhausen.sendhub.persistence.DatabaseDefinitions.IntentExtraKeys;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

//TODO validate EditText's when their next/done keyboard buttons pressed
public class ContactActivity extends Activity implements OnClickListener{

	private Button messageButton, saveButton;
	private EditText contactNameEditText, contactNumberEditText;
	private String initialContactName, initialContactNumber;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact);
		
		messageButton = (Button)this.findViewById(R.id.messageButton);
		messageButton.setOnClickListener(this);
		
		saveButton = (Button)this.findViewById(R.id.saveButton);
		saveButton.setOnClickListener(this);
		
		contactNameEditText = (EditText)this.findViewById(R.id.contactNameEditText);
		
		contactNumberEditText = (EditText)this.findViewById(R.id.contactNumberEditText);
		
		Bundle intentContactData = getIntent().getExtras();
		if(intentContactData!=null){
			initialContactName = intentContactData.getString(IntentExtraKeys.CONTACT_NAME);
			contactNameEditText.setText(initialContactName);
			this.getActionBar().setTitle(initialContactName);
			
			//Look up number from database
			String[] contactColumns = { ContactColumns.CONTACT_NUMBER };
			String[] mWereClause = {initialContactName};
			Cursor cursor = getContentResolver().query(
					ContactColumns.CONTACTS_URI, // content URI for the provider.
					contactColumns, // Return the names and  for each note.
					ContactColumns.CONTACT_NAME + "=?", // selection clause
					mWereClause, // selection clause args
					null // Use the default sort order.
					);
			cursor.moveToFirst();
			initialContactNumber = cursor.getString(0);
			contactNumberEditText.setText(initialContactNumber);
			
		}
		this.getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.contact, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		//TODO validate number and notify user if invalid instead of continuing
		switch(v.getId()){
		case R.id.messageButton:
			Bundle contactData = new Bundle();
			contactData.putString(IntentExtraKeys.CONTACT_NAME, contactNameEditText.getText().toString());
			contactData.putString(IntentExtraKeys.CONTACT_NUMBER, contactNumberEditText.getText().toString());
			
			Intent messageIntent = new Intent(this, MessageActivity.class);
			messageIntent.putExtras(contactData);
			startActivity(messageIntent);
			break;
		case R.id.saveButton:
			//TODO implement database update instead of using delete + insert
			
			//remove old values
			String contactSelect = ContactColumns.CONTACT_NAME + "=?";
			String[] contactArg = { initialContactName };
			this.getContentResolver().delete(
					ContactColumns.CONTACTS_URI, contactSelect,
					contactArg);
			
			String newContactName = contactNameEditText.getText().toString();
			String newContactNumber = contactNumberEditText.getText().toString();
			
			// Defines an object to contain the values to insert
			ContentValues mNewValues = new ContentValues();

			// Sets the values of each column
			mNewValues.put(ContactColumns.CONTACT_NAME,newContactName);
			mNewValues.put(ContactColumns.CONTACT_NUMBER,newContactNumber);
			
			this.getContentResolver().insert(ContactColumns.CONTACTS_URI,mNewValues);

			
			this.getActionBar().setTitle(newContactName);
			initialContactNumber = newContactNumber;
			initialContactName = newContactName;
			break;
		}
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case android.R.id.home:
			//up button pressed
			//TODO go up instead of back
			this.onBackPressed();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
}
