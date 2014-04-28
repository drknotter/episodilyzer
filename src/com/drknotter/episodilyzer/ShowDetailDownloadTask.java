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
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class ShowDetailDownloadTask extends AsyncTask<Show, Integer, Void>
{
	private static final String TAG = "ShowDetailDownloadTask";

	private SearchResultsActivity mActivity;

	public ShowDetailDownloadTask(SearchResultsActivity activity)
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
				"/series/" + show.get("seriesid") +
				"/all/en.zip";

		// Download the .zip file.
		String zipFileDirectory = null;
		String zipFileString = null;
		try
		{
			URL url = new URL(urlString);
			InputStream downloadStream = new BufferedInputStream(url.openStream());

			zipFileDirectory = mActivity.getFilesDir().getAbsolutePath() + "/" + show.get("seriesid") + "/";
			zipFileString = zipFileDirectory + "en.zip";

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

		// Save the banner image.
		saveBanner(zipFileDirectory, show);

		// Delete the .zip file.
		new File(zipFileString).delete();

		return null;
	}

	public void unzip(String zipFileDirectory, String zipFileString)
	{
		try
		{
			ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFileString));
			ZipEntry zipEntry = null;
			while( (zipEntry = zipInputStream.getNextEntry()) != null )
			{
				if( zipEntry.isDirectory() )
				{
					new File(zipFileDirectory + zipEntry.getName()).mkdir();
				}
				else
				{
					byte[] buffer = new byte[1024];
					FileOutputStream fileOutputStream = new FileOutputStream(zipFileDirectory + zipEntry.getName());
					Log.v(TAG, "Unzipping " + zipEntry.getName() + " to file " + zipFileDirectory + zipEntry.getName());

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

	private void saveBanner(String directoryString, Show show)
	{
		if( show.mBannerBitmap != null )
		{
			FileOutputStream out;
			try
			{
				out = new FileOutputStream(directoryString + "banner.png");
				show.mBannerBitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
				out.close();
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}
		}
	}

	private String unzipType(String filename)
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
