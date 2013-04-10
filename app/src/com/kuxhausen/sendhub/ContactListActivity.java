package com.kuxhausen.sendhub;

import com.kuxhausen.sendhub.DatabaseDefinitions.IntentExtraKeys;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ContactListActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_list);
		
		String[] dummyArrayItems = { "1", "2", "3", "4", "5" };
		ArrayAdapter<String> placeholderAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_activated_1,
				dummyArrayItems);
		setListAdapter(placeholderAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.contact_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {

		case R.id.action_add:
			//Start the contact activity with no data
			Intent contactIntent = new Intent(this, ContactActivity.class);
			startActivity(contactIntent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Bundle contactData = new Bundle();
		contactData.putString(IntentExtraKeys.CONTACT_NAME, ((TextView)v).getText().toString());
		
		Intent contactIntent = new Intent(this, ContactActivity.class);
		contactIntent.putExtras(contactData);
		startActivity(contactIntent);
		

	}
}
