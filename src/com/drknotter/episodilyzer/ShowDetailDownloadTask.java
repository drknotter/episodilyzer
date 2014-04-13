package com.drknotter.episodilyzer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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
import android.widget.Toast;

public class ShowDetailDownloadTask extends AsyncTask<Show, Integer, Void>
{
	private static final String TAG = "ShowDetailDownloadTask";

	private EpisodilyzerActivity mActivity;

	public ShowDetailDownloadTask(EpisodilyzerActivity activity)
	{
		mActivity = activity;
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

		// Download the .zip file.
		String zipFileDirectory = null;
		String zipFileString = null;
		try
		{
			URL url = new URL(urlString);
			URLConnection connection = url.openConnection();

			int contentLength = connection.getContentLength();
			Log.v(TAG, "Content length: " + contentLength);

			InputStream downloadStream = new BufferedInputStream(url.openStream());

			zipFileDirectory = mActivity.getFilesDir().getAbsolutePath() + "/" + Integer.toString(show.mSeriesId);
			zipFileString = zipFileDirectory + "/en.zip";

			// Create the show directory.
			new File(zipFileDirectory).mkdirs();

			OutputStream zipFileOutputStream = new FileOutputStream(zipFileString);
			byte[] buffer = new byte[1024];

			mActivity.runOnUiThread(new Runnable()
			{
				@Override
				public void run()
				{
					Toast.makeText(mActivity, "Downloading show zipfile...", Toast.LENGTH_SHORT).show();
				}
			});

			int count;
			while( (count = downloadStream.read(buffer)) != -1 )
			{
				zipFileOutputStream.write(buffer, 0, count);
			}

			zipFileOutputStream.flush();
			zipFileOutputStream.close();
			downloadStream.close();
		}
		catch( MalformedURLException e )
		{
			Log.e(TAG, "Malformed URL: " + urlString, e);
			return null;
		}
		catch( IOException e )
		{
			Log.e(TAG, "Could not open URL connection.", e);
			return null;
		}

		// Unzip the .zip file.
		unzip(zipFileDirectory, zipFileString);

		return null;
	}

	@Override
	protected void onPostExecute(Void result)
	{

	}

	public void unzip(String zipFileDirectory, String zipFileString)
	{
		try
		{
			ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFileString));
			ZipEntry zipEntry = null;
			while( (zipEntry = zipInputStream.getNextEntry()) != null )
			{
				Log.v(TAG, "Unzipping " + zipEntry.getName());

				if( zipEntry.isDirectory() )
				{
					new File(zipFileDirectory + zipEntry.getName()).mkdir();
				}
				else
				{
					byte[] buffer = new byte[1024];
					FileOutputStream fileOutputStream = new FileOutputStream(zipFileDirectory + zipEntry.getName());

					final String unzipTypeString = unzipType(zipEntry.getName());
					mActivity.runOnUiThread(new Runnable()
					{
						@Override
						public void run()
						{
							Toast.makeText(mActivity, "Unzipping " + unzipTypeString + "...", Toast.LENGTH_SHORT).show();
						}

					});

					int count;
					while( (count = zipInputStream.read(buffer)) != -1 )
					{
						fileOutputStream.write(buffer, 0, count);
					}

					zipInputStream.closeEntry();
					fileOutputStream.close();
				}

			}
			zipInputStream.close();
		}
		catch( Exception e )
		{
			Log.e(TAG, "Error unzipping file " + zipFileString, e);
		}
	}

	public String unzipType(String filename)
	{
		if( "en.xml".equals(filename) )
		{
			return "show details";
		}
		else if( "banners.xml".equals(filename) )
		{
			return "banner URLs";
		}
		else if( "actors.xml".equals(filename) )
		{
			return "actor information";
		}
		return "";
	}
}
