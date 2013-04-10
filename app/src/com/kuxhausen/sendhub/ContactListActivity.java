package com.kuxhausen.sendhub;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Intent i = new Intent(this, ContactActivity.class);
		startActivity(i);
		

	}
}
