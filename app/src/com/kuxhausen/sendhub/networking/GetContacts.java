package com.kuxhausen.sendhub.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.kuxhausen.sendhub.api.Contact;
import com.kuxhausen.sendhub.api.Contacts;
import com.kuxhausen.sendhub.persistence.DatabaseDefinitions.PreferenceKeys;

public class GetContacts extends AsyncTask<Void, Void, Contact[]> {

	private Context context;
	private Gson gson = new Gson();
	private OnBulbListReturnedListener listener;

	public interface OnBulbListReturnedListener {
		/** Called by HeadlinesFragment when a list item is selected */
		public void onContactsListReturned(Contact[] result);
	}

	public GetContacts(Context cont, OnBulbListReturnedListener listen) {
		context = cont;
		listener = listen;
	}

	@Override
	protected Contact[] doInBackground(Void... voids) {

		// Get username and IP from preferences cache
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(context);
		String number = settings.getString(PreferenceKeys.USERNAME, null);
		String apiKey = "4cd29785eb1d0e21de8747d9e636f420028854a3"; // TODO
																	// secure
																	// this
																	// string

		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();

		HttpGet httpGet = new HttpGet(
				"https://api.sendhub.com/v1/contacts/?username=" + number
						+ "&api_key=" + apiKey);

		try {

			HttpResponse response = client.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();

			if (statusCode == 200) {

				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(content));

				String returnOutput = "";
				String line;
				while ((line = reader.readLine()) != null) {
					builder.append(line);
					returnOutput += line;
				}
				Contacts resultContacts = gson.fromJson(returnOutput,
						Contacts.class);
				return resultContacts.objects;
			} else {

			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();

		}
		return null;
	}

	@Override
	protected void onPostExecute(Contact[] result) {
		listener.onContactsListReturned(result);
	}

}
