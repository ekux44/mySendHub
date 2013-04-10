package com.kuxhausen.sendhub;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;


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
		contactNameEditText.setOnClickListener(this);
		
		contactNumberEditText = (EditText)this.findViewById(R.id.contactNumberEditText);
		contactNumberEditText.setOnClickListener(this);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.contact_list, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.messageButton:
			Intent i = new Intent(this, MessageActivity.class);
			startActivity(i);
			break;
		case R.id.saveButton:
			//TODO save
			break;
		}
		
	}
	
}
