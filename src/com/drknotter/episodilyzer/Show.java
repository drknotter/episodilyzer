package com.drknotter.episodilyzer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Locale;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

@SuppressWarnings("serial")
public class Show extends HashMap<String, String>
{
	@SuppressWarnings("unused")
	private static final String TAG = "Show";

	public static final String SERIESID = "seriesid";
	public static final String ID = "id";
	public static final String SERIESNAME = "seriesname";
	public static final String OVERVIEW = "overview";
	public static final String FIRSTAIRED = "firstaired";

	TreeMap<String, Season> mSeasons = new TreeMap<String, Season>();
	Bitmap mBannerBitmap;

	public Show(File showDir)
	{
		File seriesDetailsXmlFile = new File(showDir, "en.xml");

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try
		{
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource inStream = new InputSource();
			inStream.setCharacterStream(new FileReader(seriesDetailsXmlFile));
			Document document = db.parse(inStream);

			// Initialize the series information.
			Node seriesNode = document.getElementsByTagName("Series").item(0);
			if( seriesNode != null )
			{
				initializeSeriesFromXmlNode(seriesNode);
			}

			// Load the banner image, if it exists.
			File bannerFile = new File(showDir, "banner.png");
			if( bannerFile.exists() )
			{
				mBannerBitmap = BitmapFactory.decodeStream(new FileInputStream(bannerFile));
			}

			// Initialize the seasons/episodes.
			NodeList episodeNodeList = document.getElementsByTagName("Episode");
			for( int i = 0; i < episodeNodeList.getLength(); i++ )
			{
				Episode episode = new Episode(episodeNodeList.item(i));
				String seasonNumber = episode.get(Episode.SEASONNUMBER);
				String episodeNumber = episode.get(Episode.EPISODENUMBER);
				if( !mSeasons.containsKey(seasonNumber) )
				{
					mSeasons.put(seasonNumber, new Season());
				}
				mSeasons.get(seasonNumber).put(episodeNumber, episode);
			}
		}
		catch( Exception e )
		{
			Log.e(TAG, "Error parsing .xml file in show constructor", e);
		}
	}

	public Show(Node seriesNode)
	{
		initializeSeriesFromXmlNode(seriesNode);
	}

	public void initializeSeriesFromXmlNode(Node seriesNode)
	{
		if( seriesNode.getNodeType() == Node.ELEMENT_NODE && seriesNode.getNodeName().equals("Series") )
		{
			NodeList childNodes = seriesNode.getChildNodes();
			for( int i = 0; i < childNodes.getLength(); i++ )
			{
				Node childNode = childNodes.item(i);
				if( childNode.getNodeType() == Node.ELEMENT_NODE )
				{
					this.put(childNode.getNodeName().toLowerCase(Locale.ENGLISH), childNode.getTextContent());
				}
			}
		}
	}
}
