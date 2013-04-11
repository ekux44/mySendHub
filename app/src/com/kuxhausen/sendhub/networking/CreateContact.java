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
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.kuxhausen.sendhub.api.Contact;
import com.kuxhausen.sendhub.persistence.DatabaseDefinitions.PreferenceKeys;

public class CreateContact extends AsyncTask<Void, Void, String> {

	private Context context;
	private Contact contact;
	private Gson gson = new Gson();
	private OnIdReturnedListener listener;

	public interface OnIdReturnedListener {
		/** Called by HeadlinesFragment when a list item is selected */
		public void onIdReturned(String id);
	}

	public CreateContact(Context cont, OnIdReturnedListener listen, Contact con) {
		context = cont;
		listener = listen;
		contact = con;
	}

	@Override
	protected String doInBackground(Void... voids) {

		// Get username and IP from preferences cache
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(context);
		String number = settings.getString(PreferenceKeys.USERNAME, null);
		String apiKey = "4cd29785eb1d0e21de8747d9e636f420028854a3"; // TODO
																	// secure
																	// this
																	// string

		StringBuilder builder = new StringBuilder();
		HttpClient httpclient = new DefaultHttpClient();

		HttpPost httppost = new HttpPost(
				"https://api.sendhub.com/v1/contacts/?username=" + number
						+ "&api_key=" + apiKey);
		try {

			StringEntity se = new StringEntity(gson.toJson(contact));
			// sets the post request as the resulting string
			httppost.setEntity(se);
			// sets a request header so the page receiving the request
			// will know what to do with it
			httppost.setHeader("Accept", "application/json");
			httppost.setHeader("Content-type", "application/json");

			// execute HTTP post request
			HttpResponse response = httpclient.execute(httppost);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();

			if (statusCode == 201) {

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
				Contact resultContacts = gson.fromJson(returnOutput,
						Contact.class);
				return resultContacts.id;
			} else {
				// TODO handle error responses
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}
		return null;
	}

	@Override
	protected void onPostExecute(String contactID) {
		listener.onIdReturned(contactID);
	}

}
