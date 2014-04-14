package com.drknotter.episodilyzer;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.ParseException;
import android.os.AsyncTask;

class SearchShowsTask extends AsyncTask<String, Integer, LinkedList<Show>>
{
	private EpisodilyzerActivity mActivity;
	private boolean mSuccess = false;

	public SearchShowsTask(EpisodilyzerActivity activity)
	{
		mActivity = activity;
	}

	@Override
	protected LinkedList<Show> doInBackground(String... seriesNames)
	{
		LinkedList<Show> showList = new LinkedList<Show>();

		ConnectivityManager connManager = (ConnectivityManager) mActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo wifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if( !wifi.isConnected() )
		{
			mSuccess = false;
			return showList;
		}

		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost("http://thetvdb.com/api/GetSeries.php");
		HttpResponse response = null;

		try
		{
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("seriesname", seriesNames[0]));
			nameValuePairs.add(new BasicNameValuePair("language", "en"));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			response = httpclient.execute(httppost);

		}
		catch( ClientProtocolException e )
		{
			e.printStackTrace();
		}
		catch( IOException e )
		{
			e.printStackTrace();
		}

		String xml = "";
		try
		{
			xml = EntityUtils.toString(response.getEntity());
		}
		catch( ParseException e )
		{
			e.printStackTrace();
		}
		catch( IOException e )
		{
			e.printStackTrace();
		}

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try
		{
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource inStream = new InputSource();
			inStream.setCharacterStream(new StringReader(xml));
			Document document = db.parse(inStream);

			NodeList seriesNodes = document.getElementsByTagName("Series");
			int bannerlessShows = 0;
			for( int i = 0; i < seriesNodes.getLength(); i++ )
			{
				Show s = new Show(seriesNodes.item(i));
				if( s.containsKey("overview") )
				{
					new ShowBannerDownloadTask(mActivity).execute(s);
					if( !s.containsKey("banner") )
					{
						showList.addLast(s);
						bannerlessShows++;
					}
					else
					{
						showList.add(showList.size()-bannerlessShows, s);
					}
				}
			}
		}
		catch( ParserConfigurationException e )
		{
			e.printStackTrace();
		}
		catch( SAXException e )
		{
			e.printStackTrace();
		}
		catch( IOException e )
		{
			e.printStackTrace();
		}

		mSuccess = true;
		return showList;
	}

	protected void onPostExecute(LinkedList<Show> searchResults)
	{
		if( mSuccess )
		{
			mActivity.presentSearchResults(searchResults);
		}
		else
		{
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mActivity);
			alertDialogBuilder.setTitle("No Wi-Fi!")
					.setMessage("By default, Episodilyzer doesn't search for shows without a wi-fi connection.")
					.setCancelable(false)
					.setPositiveButton("OK", new DialogInterface.OnClickListener()
					{
						public void onClick(DialogInterface dialog, int id)
						{
							mActivity.finish();
						}
					});

			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
		}
	}	
}
