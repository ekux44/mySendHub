package com.kuxhausen.sendhub;

import com.kuxhausen.sendhub.DatabaseDefinitions.IntentExtraKeys;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class MessageActivity extends Activity implements OnClickListener{

	private String name, number;
	private EditText messageBodyEditText;
	private Button sendButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//Abort if no contact data passed into messenger
		Bundle intentContactData = getIntent().getExtras();
		if(intentContactData==null)
			finish();
		else{
			setContentView(R.layout.activity_message);
			
			name = intentContactData.getString(IntentExtraKeys.CONTACT_NAME);
			number = intentContactData.getString(IntentExtraKeys.CONTACT_NUMBER);
			
			this.getActionBar().setTitle(name);
			
			messageBodyEditText = (EditText)this.findViewById(R.id.messageBodyEditText);
			
			sendButton = (Button)this.findViewById(R.id.sendButton);
			sendButton.setOnClickListener(this);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.message, menu);
		return true;
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.sendButton:
			//TODO send
			break;
		}
		
	}

}
