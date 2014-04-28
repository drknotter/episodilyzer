package com.drknotter.episodilyzer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import android.annotation.SuppressLint;
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

	ArrayList<Episode> mEpisodesList;
	Bitmap mBannerBitmap;

	public Show()
	{
	}

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
			NodeList seriesNodeList = document.getElementsByTagName("Series");
			initializeSeriesFromXmlElement(seriesNodeList.item(0));
			
			// Load the banner image, if it exists.
			File bannerFile = new File(showDir, "banner.png");
			if( bannerFile.exists() )
			{
				mBannerBitmap = BitmapFactory.decodeStream(new FileInputStream(bannerFile));
			}
		}
		catch( Exception e )
		{
			Log.e(TAG, "Error parsing .xml file in show constructor", e);
		}
	}

	public Show(Node seriesElement)
	{
		initializeSeriesFromXmlElement(seriesElement);
	}
	
	@SuppressLint("DefaultLocale")
	public void initializeSeriesFromXmlElement(Node seriesElement)
	{
		if( seriesElement.getNodeType() == Node.ELEMENT_NODE && seriesElement.getNodeName().equals("Series") )
		{
			NodeList childNodes = seriesElement.getChildNodes();
			for( int i=0; i<childNodes.getLength(); i++ )
			{
				Node childNode = childNodes.item(i);
				if( childNode.getNodeType() == Node.ELEMENT_NODE )
				{
					this.put(childNode.getNodeName().toLowerCase(), childNode.getTextContent());
				}
			}
		}
	}

	public String getDescriptionString()
	{
		String description = "";

		description += this.get(SERIESID) + "\n\n";
		description += "Overview: " + this.get(OVERVIEW) + "\n\n";
		description += "First Aired: " + this.get(FIRSTAIRED) + "\n\n";

		return description;
	}

}
