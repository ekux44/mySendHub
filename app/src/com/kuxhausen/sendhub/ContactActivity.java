package com.kuxhausen.sendhub;
import com.kuxhausen.sendhub.DatabaseDefinitions.IntentExtraKeys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

//TODO validate EditText's when their next/done keyboard buttons pressed
public class ContactActivity extends Activity implements OnClickListener{

	private Button messageButton, saveButton;
	private EditText contactNameEditText, contactNumberEditText;
	
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
			String contactName = intentContactData.getString(IntentExtraKeys.CONTACT_NAME);
			contactNameEditText.setText(contactName);
			this.getActionBar().setTitle(contactName);
			//TODO look up number from database if it exists
		}
		
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
			//TODO save
			this.getActionBar().setTitle(contactNameEditText.getText().toString());
			break;
		}
		
	}
	
}
