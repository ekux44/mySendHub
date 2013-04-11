package com.kuxhausen.sendhub;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.kuxhausen.sendhub.api.Contact;
import com.kuxhausen.sendhub.networking.CreateContact;
import com.kuxhausen.sendhub.networking.CreateContact.OnIdReturnedListener;
import com.kuxhausen.sendhub.persistence.DatabaseDefinitions.ContactColumns;
import com.kuxhausen.sendhub.persistence.DatabaseDefinitions.IntentExtraKeys;

//TODO validate EditText's when their next/done keyboard buttons pressed
public class ContactActivity extends Activity implements OnClickListener, OnIdReturnedListener {

	private Button messageButton, saveButton;
	private EditText contactNameEditText, contactNumberEditText;
	private String contactName, contactNumber, contactID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact);

		messageButton = (Button) this.findViewById(R.id.messageButton);
		messageButton.setOnClickListener(this);

		saveButton = (Button) this.findViewById(R.id.saveButton);
		saveButton.setOnClickListener(this);

		contactNameEditText = (EditText) this
				.findViewById(R.id.contactNameEditText);

		contactNumberEditText = (EditText) this
				.findViewById(R.id.contactNumberEditText);

		Bundle intentContactData = getIntent().getExtras();
		if (intentContactData != null) {
			contactName = intentContactData
					.getString(IntentExtraKeys.CONTACT_NAME);
			contactNameEditText.setText(contactName);
			this.getActionBar().setTitle(contactName);

			// Look up number from database
			String[] contactColumns = { ContactColumns.CONTACT_NUMBER,
					ContactColumns.CONTACT_ID };
			String[] mWereClause = { contactName };
			Cursor cursor = getContentResolver().query(
					ContactColumns.CONTACTS_URI, // content URI for the
													// provider.
					contactColumns, // Return the names and for each note.
					ContactColumns.CONTACT_NAME + "=?", // selection clause
					mWereClause, // selection clause args
					null // Use the default sort order.
					);
			cursor.moveToFirst();
			contactNumber = cursor.getString(0);
			contactNumberEditText.setText(contactNumber);
			contactID = cursor.getString(1);

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
		// TODO validate number and notify user if invalid instead of continuing
		switch (v.getId()) {
		case R.id.messageButton:
			Bundle contactData = new Bundle();
			contactData.putString(IntentExtraKeys.CONTACT_NAME,
					contactNameEditText.getText().toString());
			contactData.putString(IntentExtraKeys.CONTACT_ID, contactID);

			Intent messageIntent = new Intent(this, MessageActivity.class);
			messageIntent.putExtras(contactData);
			startActivity(messageIntent);
			break;
		case R.id.saveButton:
			// Upload to sendhub
			Contact current = new Contact();
			current.name= contactNameEditText.getText().toString();
			current.number = contactNumberEditText.getText().toString();
			
			CreateContact pollContacts = new CreateContact(this, this, current);
			pollContacts.execute();
			// TODO implement db update instead of using delete + insert
			// remove old values from local database
			if (contactID != null) { //if contactID != null, contact api call succeeded
				String contactSelect = ContactColumns.CONTACT_ID + "=?";
				String[] contactArg = { contactID };
				this.getContentResolver().delete(ContactColumns.CONTACTS_URI,
						contactSelect, contactArg);
			
			contactName = contactNameEditText.getText().toString();
			contactNumber = contactNumberEditText.getText().toString();

			// Defines an object to contain the values to insert
			ContentValues mNewValues = new ContentValues();

			// Sets the values of each column
			mNewValues.put(ContactColumns.CONTACT_NAME, contactName);
			mNewValues.put(ContactColumns.CONTACT_NUMBER, contactNumber);
			mNewValues.put(ContactColumns.CONTACT_ID, contactID);

			// Put updated values in local database
			this.getContentResolver().insert(ContactColumns.CONTACTS_URI,
					mNewValues);

			this.getActionBar().setTitle(contactName);
			}
			break;
		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case android.R.id.home:
			// up button pressed
			// TODO go up instead of back
			this.onBackPressed();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onIdReturned(String id) {
		if(id!=null){
			contactID = id;
		}
	}

}
