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
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.kuxhausen.sendhub.persistence.DatabaseDefinitions.PreferenceKeys;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

public class SendMessage extends AsyncTask<Object, Void, Boolean> {

	Context context;
	Message message;
	Gson gson = new Gson();

	public SendMessage(Context cont, Message msg){
		context = cont;
		message = msg;
	}
	
	@Override
	protected Boolean doInBackground(Object... params) {

		// Get username and IP from preferences cache
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(context);
		String number = settings.getString(PreferenceKeys.USERNAME,null);
		String apiKey = "4cd29785eb1d0e21de8747d9e636f420028854a3"; //TODO secure this string

		StringBuilder builder = new StringBuilder();
		HttpClient httpclient = new DefaultHttpClient();

		//https://api.sendhub.com/v1/messages/?username=NUMBER&api_key=APIKEY
		HttpPost httppost = new HttpPost("https://api.sendhub.com/v1/messages/?username="+number+ "&api_key="+apiKey);
		try {

			StringEntity se = new StringEntity(gson.toJson(message));
			// sets the post request as the resulting string
			httppost.setEntity(se);
			// sets a request header so the page receiving the request
			// will know what to do with it
			httppost.setHeader("Accept", "application/json");
			httppost.setHeader("Content-type", "application/json");

			// execute HTTP post request
			HttpResponse response = httpclient.execute(httppost);

			// TODO analyze the response
			return true;

		} catch (ClientProtocolException e) {

			// TODO Auto-generated catch block
		} catch (IOException e) {

			// TODO Auto-generated catch block
		} catch (java.lang.IllegalArgumentException e) {
			// TODO deal with null IP from getBridge
		}
		return false;
	}

	@Override
	protected void onPostExecute(Boolean success) {
	}

}
