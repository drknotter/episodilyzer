package com.drknotter.episodilyzer;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

public class ShowDetailDownloadTask extends AsyncTask<Show, Integer, Void>
{
	private static final String TAG = "ShowDetailDownloadTask";

	private EpisodilyzerActivity mActivity;
	private ProgressDialog mProgressDialog;

	public ShowDetailDownloadTask(EpisodilyzerActivity activity)
	{
		mActivity = activity;
	}
	
	@Override
	protected void onPreExecute()
	{
		mProgressDialog = new ProgressDialog(mActivity);
		mProgressDialog.setMessage("Downloading show details...");
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		mProgressDialog.setCancelable(true);
		mProgressDialog.show();
	}

	@Override
	protected Void doInBackground(Show... args)
	{
		Show show = args[0];

		ConnectivityManager connManager = (ConnectivityManager) mActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo wifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if( !wifi.isConnected() )
		{
			return null;
		}

		// Create the api call.
		String urlString = "http://thetvdb.com/api/" +
				mActivity.getResources().getString(R.string.api_key) +
				"/series/" + show.mSeriesId +
				"/all/en.zip";

		URL url;
		try
		{
			url = new URL(urlString);
			URLConnection connection = url.openConnection();

			int contentLength = connection.getContentLength();
			Log.v(TAG, "Content length: " + contentLength);

			InputStream input = new BufferedInputStream(url.openStream());
			//OutputStream output = new FileOutputStream("/sdcard/some_photo_from_gdansk_poland.jpg");
			byte[] buffer = new byte[1024];

			long total = 0, count;
			while( (count = input.read(buffer)) != -1 )
			{
				total += count;
				publishProgress((int) (100 * total / contentLength));
				//output.write(buffer, 0, count);
			}
			
			//output.flush();
			//output.close();
			input.close();
		}
		catch( MalformedURLException e )
		{
			Log.e(TAG, "Malformed URL in show detail download task.");
			e.printStackTrace();
		}
		catch( IOException e )
		{
			Log.e(TAG, "Could not open connection in show detail download task.");
			e.printStackTrace();
		}

		return null;
	}
	
	@Override
	protected void onProgressUpdate(Integer... progress)
	{
		Log.d(TAG, "Progress: " + progress[0]);
		mProgressDialog.setProgress(progress[0]);
	}
	
	@Override
	protected void onPostExecute(Void result)
	{
		mProgressDialog.dismiss();
	}

}
