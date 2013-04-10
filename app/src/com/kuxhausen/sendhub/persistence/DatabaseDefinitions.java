package com.kuxhausen.sendhub.persistence;

import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseDefinitions {
	public static final class IntentExtraKeys {
		public static final String CONTACT_NAME = "Contact_Name";
		public static final String CONTACT_NUMBER = "Contact_Number";

		// This class cannot be instantiated
		private IntentExtraKeys() {
		}

	}

	public static final class ContactColumns implements BaseColumns {
		/**
		 * The table name offered by this provider
		 */
		public static final String TABLE_NAME = "contacts";

		/**
		 * The scheme part for this provider's URI
		 */
		private static final String SCHEME = "content://";

		public static final String PATH_CONTACTS = "/contacts";
		/**
		 * The content:// style URL for this table
		 */
		public static final Uri CONTACTS_URI = Uri.parse(SCHEME + AUTHORITY
				+ PATH_CONTACTS);

		/**
		 * Contact name column
		 */
		public static final String CONTACT_NAME = "Contact_Name";

		/**
		 * Contact number column
		 */
		public static final String CONTACT_NUMBER = "Contact_Number";

		// This class cannot be instantiated
		private ContactColumns() {
		}
	}

	public static final String AUTHORITY = "com.kuxhausen.provider.sendhub.persistence";

	// This class cannot be instantiated
	private DatabaseDefinitions() {
	}
}
