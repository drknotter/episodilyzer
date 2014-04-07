package com.drknotter.episodilyzer;

import java.util.ArrayList;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import android.graphics.Bitmap;
import android.util.Log;

public class Show
{
	private static final String TAG = "Show";

	public int mSeriesId = -1;
	public String mSeriesName = "";
	public Bitmap mBanner = null;
	public String mOverview = "";
	public String mFirstAired = "";
	public String mBannerUrl = "";
	public ArrayList<String> mAliasNames = new ArrayList<String>();

	public Show()
	{
	}

	public Show(String seriesName)
	{
		mSeriesName = seriesName;
	}

	public Show(Element element)
	{
		try
		{
			mSeriesId = Integer.parseInt(getCharacterDataFromElement((Element) element.getElementsByTagName("seriesid").item(0)));
		}
		catch( NullPointerException e )
		{
			Log.w(TAG, "No series id found!");
		}
		
		try
		{
			mSeriesName = getCharacterDataFromElement((Element) element.getElementsByTagName("SeriesName").item(0));
		}
		catch( NullPointerException e )
		{
			Log.w(TAG, "No series name found!");
		}
		
		try
		{
			mOverview = getCharacterDataFromElement((Element) element.getElementsByTagName("Overview").item(0));
		}
		catch( NullPointerException e )
		{
			Log.w(TAG, "No overview found!");
		}
		
		try
		{
			mFirstAired = getCharacterDataFromElement((Element) element.getElementsByTagName("FirstAired").item(0));
		}
		catch( NullPointerException e )
		{
			Log.w(TAG, "No first air date found!");
		}
		
		try
		{
			mBannerUrl = getCharacterDataFromElement((Element) element.getElementsByTagName("banner").item(0));
		}
		catch( NullPointerException e )
		{
			Log.w(TAG, "No banner url found!");
		}
	}

	public String getDescriptionString()
	{
		String description = "";

		description += mSeriesName + "\n\n";
		description += "Overview: " + mOverview + "\n\n";
		description += "First Aired: " + mFirstAired + "\n\n";

		return description;
	}

	private static String getCharacterDataFromElement(Element element)
	{
		Node child = element.getFirstChild();
		if( child instanceof CharacterData )
		{
			CharacterData cd = (CharacterData) child;
			return cd.getData();
		}
		return "";
	}
}
