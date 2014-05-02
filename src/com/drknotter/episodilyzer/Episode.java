package com.drknotter.episodilyzer;

import java.util.HashMap;
import java.util.Locale;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@SuppressWarnings("serial")
public class Episode extends HashMap<String, String>
{
	@SuppressWarnings("unused")
	private static final String TAG = "Episode";

	public static final String ID = "id";
	public static final String EPISODENAME = "episodename";
	public static final String EPISODENUMBER = "episodenumber";
	public static final String SEASONNUMBER = "seasonnumber";
	public static final String OVERVIEW = "overview";
	public static final String RATING = "rating";
	public static final String RATINGCOUNT = "ratingcount";
	public static final String WRITER = "writer";
	public static final String GUESTSTARS = "gueststars";

	public Episode(Node episodeNode)
	{
		initializeEpisodeFromXmlNode(episodeNode);
	}

	public void initializeEpisodeFromXmlNode(Node episodeNode)
	{
		if( episodeNode.getNodeType() == Node.ELEMENT_NODE && episodeNode.getNodeName().equals("Episode") )
		{
			NodeList childNodes = episodeNode.getChildNodes();
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
