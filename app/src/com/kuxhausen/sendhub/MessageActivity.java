package com.kuxhausen.sendhub;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.kuxhausen.sendhub.api.Message;
import com.kuxhausen.sendhub.networking.SendMessage;
import com.kuxhausen.sendhub.persistence.DatabaseDefinitions.IntentExtraKeys;

public class MessageActivity extends Activity implements OnClickListener {

	private String name;
	private String[] contacts;
	private EditText messageBodyEditText;
	private Button sendButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Abort if no contact data passed into messenger
		Bundle intentContactData = getIntent().getExtras();
		if (intentContactData == null)
			finish();
		else {
			setContentView(R.layout.activity_message);

			name = intentContactData.getString(IntentExtraKeys.CONTACT_NAME);
			contacts = new String[1];
			contacts[0] = intentContactData
					.getString(IntentExtraKeys.CONTACT_ID);

			this.getActionBar().setTitle(name);

			messageBodyEditText = (EditText) this
					.findViewById(R.id.messageBodyEditText);

			sendButton = (Button) this.findViewById(R.id.sendButton);
			sendButton.setOnClickListener(this);

			this.getActionBar().setDisplayHomeAsUpEnabled(true);

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
		switch (v.getId()) {
		case R.id.sendButton:
			Message message = new Message();
			message.text = messageBodyEditText.getText().toString();
			message.contacts = contacts;
			SendMessage transmit = new SendMessage(this, message);
			transmit.execute();
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
}
