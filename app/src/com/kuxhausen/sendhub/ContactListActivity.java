package com.kuxhausen.sendhub;

import com.kuxhausen.sendhub.persistence.DatabaseDefinitions.ContactColumns;
import com.kuxhausen.sendhub.persistence.DatabaseDefinitions.IntentExtraKeys;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.database.Cursor;
import android.app.LoaderManager;

public class ContactListActivity extends ListActivity implements
LoaderManager.LoaderCallbacks<Cursor> {

	// Identifies a particular Loader being used in this component
	private static final int CONTACTS_LOADER = 0;
	private CursorAdapter dataSource;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_list);
		
		/*
		 * Initializes the CursorLoader. The GROUPS_LOADER value is eventually
		 * passed to onCreateLoader().
		 */
		getLoaderManager().initLoader(CONTACTS_LOADER, null, this);

		String[] columns = { ContactColumns.CONTACT_NAME, ContactColumns._ID };
		dataSource = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_activated_1, null,
				columns, new int[] { android.R.id.text1 }, 0);

		setListAdapter(dataSource);
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
	
	
	/**
	 * Callback that's invoked when the system has initialized the Loader and is
	 * ready to start the query. This usually happens when initLoader() is
	 * called. The loaderID argument contains the ID value passed to the
	 * initLoader() call.
	 */
	@Override
	public Loader<Cursor> onCreateLoader(int loaderID, Bundle arg1) {
		/*
		 * Takes action based on the ID of the Loader that's being created
		 */
		switch (loaderID) {
		case CONTACTS_LOADER:
			// Returns a new CursorLoader
			String[] columns = { ContactColumns.CONTACT_NAME, ContactColumns._ID };
			return new CursorLoader(this, // activity context
					ContactColumns.CONTACTS_URI, // Table
					columns, // Projection to return
					null, // No selection clause
					null, // No selection arguments
					null // Default sort order
			);
		default:
			// An invalid id was passed in
			return null;
		}
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {
		/*
		 * Moves the query results into the adapter, causing the ListView
		 * fronting this adapter to re-display
		 */
		dataSource.changeCursor(cursor);
		registerForContextMenu(getListView());
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		/*
		 * Clears out the adapter's reference to the Cursor. This prevents
		 * memory leaks.
		 */
		// unregisterForContextMenu(getListView());
		dataSource.changeCursor(null);
	}
}
