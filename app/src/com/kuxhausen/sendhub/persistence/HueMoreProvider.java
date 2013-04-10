package com.kuxhausen.sendhub.persistence;

import java.util.HashMap;

import com.kuxhausen.sendhub.persistence.DatabaseDefinitions.ContactColumns;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;

public class HueMoreProvider extends ContentProvider {

	DatabaseHelper mOpenHelper;

	/**
	 * A projection map used to select columns from the database
	 */
	private static HashMap<String, String> sContactsProjectionMap;
	/**
	 * A UriMatcher instance
	 */
	private static final UriMatcher sUriMatcher;
	/*
	 * Constants used by the Uri matcher to choose an action based on the
	 * pattern of the incoming URI
	 */
	// The incoming URI matches the Groups URI pattern
	private static final int CONTACTS = 0;

	/**
	 * A block that instantiates and sets static objects
	 */
	static {

		/*
		 * Creates and initializes the URI matcher
		 */
		// Create a new instance
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

		{
			// Add a pattern that routes URIs terminated with "groups" to a
			// GROUPS
			// operation
			sUriMatcher.addURI(DatabaseDefinitions.AUTHORITY, "contacts", CONTACTS);
			// Creates a new projection map instance. The map returns a column
			// name
			// given a string. The two are usually equal.
			sContactsProjectionMap = new HashMap<String, String>();

			// Maps the string "_ID" to the column name "_ID"
			sContactsProjectionMap.put(BaseColumns._ID, BaseColumns._ID);

			sContactsProjectionMap.put(DatabaseDefinitions.ContactColumns.CONTACT_NAME,
					DatabaseDefinitions.ContactColumns.CONTACT_NAME);
			sContactsProjectionMap.put(DatabaseDefinitions.ContactColumns.CONTACT_NUMBER,
					DatabaseDefinitions.ContactColumns.CONTACT_NUMBER);
			}
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// Constructs a new query builder and sets its table name
		String table = null;

		Uri toNotify = uri;
		/**
		 * Choose the projection and adjust the "where" clause based on URI
		 * pattern-matching.
		 */
		switch (sUriMatcher.match(uri)) {
		// If the incoming URI is for notes, chooses the Notes projection

		case CONTACTS:
			table = (DatabaseDefinitions.ContactColumns.TABLE_NAME);
			toNotify = DatabaseDefinitions.ContactColumns.CONTACTS_URI;
			break;

		}

		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		db.delete(table, selection, selectionArgs);

		this.getContext().getContentResolver().notifyChange(uri, null);
		this.getContext().getContentResolver().notifyChange(toNotify, null);

		return 0;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		
		// Constructs a new query builder and sets its table name
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		/**
		 * Choose the projection and adjust the "where" clause based on URI
		 * pattern-matching.
		 */
		switch (sUriMatcher.match(uri)) {
		// If the incoming URI is for notes, chooses the Notes projection
		case CONTACTS:
			qb.setTables(DatabaseDefinitions.ContactColumns.TABLE_NAME);
			qb.setProjectionMap(sContactsProjectionMap);
			break;
		default:
			// If the URI doesn't match any of the known patterns, throw an
			// exception.
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		// Opens the database object in "read" mode, since no writes need to be
		// done.
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();

		long insertId = db.insert(qb.getTables(), null, values);
		if (insertId == -1) {
			// insert failed, do update
			//TODO update

		}

		this.getContext().getContentResolver().notifyChange(uri, null);

		return null;
	}

	@Override
	public boolean onCreate() {
		// Creates a new helper object. Note that the database itself isn't
		// opened until
		// something tries to access it, and it's only created if it doesn't
		// already exist.
		mOpenHelper = new DatabaseHelper(getContext());

		// Assumes that any failures will be reported by a thrown exception.
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {

		// Constructs a new query builder and sets its table name
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		String groupBy = null;
		/**
		 * Choose the projection and adjust the "where" clause based on URI
		 * pattern-matching.
		 */
		switch (sUriMatcher.match(uri)) {
		// If the incoming URI is for contacts, chooses the contacts projection
		case CONTACTS:
			qb.setTables(DatabaseDefinitions.ContactColumns.TABLE_NAME);
			qb.setProjectionMap(sContactsProjectionMap);
			groupBy = DatabaseDefinitions.ContactColumns.CONTACT_NAME;
			break;
		default:
			// If the URI doesn't match any of the known patterns, throw an
			// exception.
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		// Opens the database object in "read" mode, since no writes need to be
		// done.
		SQLiteDatabase db = mOpenHelper.getReadableDatabase();

		/*
		 * Performs the query. If no problems occur trying to read the database,
		 * then a Cursor object is returned; otherwise, the cursor variable
		 * contains null. If no records were selected, then the Cursor object is
		 * empty, and Cursor.getCount() returns 0.
		 */
		Cursor c = qb.query(db, // The database to query
				projection, // The columns to return from the query
				selection, // The columns for the where clause
				selectionArgs, // The values for the where clause
				groupBy, // don't group the rows
				null, // don't filter by row groups
				sortOrder // The sort order
				);

		// Tells the Cursor what URI to watch, so it knows when its source data
		// changes
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}
	private class DatabaseHelper extends SQLiteOpenHelper {

		private static final String DATABASE_NAME = "sendhub.db";
		private static final int DATABASE_VERSION = 1;

		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {

			db.execSQL("CREATE TABLE " + ContactColumns.TABLE_NAME + " ("
					+ BaseColumns._ID + " INTEGER PRIMARY KEY," + ContactColumns.CONTACT_NAME
					+ " TEXT," + ContactColumns.CONTACT_NUMBER + " TEXT" + ");");

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS notes");
			onCreate(db);
		}
	}
}
