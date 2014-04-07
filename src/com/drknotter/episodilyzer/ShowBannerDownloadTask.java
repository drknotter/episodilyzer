package com.drknotter.episodilyzer;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

class ShowBannerDownloadTask extends AsyncTask<Show, Integer, Bitmap>
{
	private EpisodilyzerActivity mActivity;

	public ShowBannerDownloadTask(EpisodilyzerActivity activity)
	{
		mActivity = activity;
	}

	@Override
	protected Bitmap doInBackground(Show... shows)
	{
		Bitmap bannerBitmap = null;

		ConnectivityManager connManager = (ConnectivityManager) mActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo wifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if( !wifi.isConnected() )
		{
			return bannerBitmap;
		}

		if( shows[0].mBannerUrl.length() > 0 )
		{
			try
			{
				String url = "http://www.thetvdb.com/banners/" + shows[0].mBannerUrl;
				InputStream is = (InputStream) new URL(url).getContent();
				bannerBitmap = BitmapFactory.decodeStream(is);
				is.close();
			}
			catch( MalformedURLException e )
			{
				e.printStackTrace();
			}
			catch( IOException e )
			{
				e.printStackTrace();
			}
		}

		shows[0].mBanner = bannerBitmap;

		return bannerBitmap;
	}

	protected void onPostExecute(Bitmap bannerBitmap)
	{
		ShowListFragment leftPane = (ShowListFragment) mActivity.getFragmentManager().findFragmentById(R.id.leftpane);
		leftPane.notifyAdapterDataSetChanged();
	}
}
