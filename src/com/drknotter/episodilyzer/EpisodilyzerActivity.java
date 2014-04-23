package com.drknotter.episodilyzer;

import java.util.LinkedList;

import com.drknotter.episodilyzer.ShowListFragment.ShowListener;

import android.os.Bundle;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.widget.SearchView;
import android.support.v4.widget.SlidingPaneLayout;

public class EpisodilyzerActivity extends Activity implements ShowListener
{
	public static final String TAG = "EpisodilyzerActivity";
	
	static final int MODE_DEFAULT = 0;
	static final int MODE_SEARCH = 1;
	private int mMode = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		if( Intent.ACTION_SEARCH.equals(getIntent().getAction()) )
		{
			mMode = MODE_SEARCH;
		}
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_episodilyzer);
		SlidingPaneLayout pane = (SlidingPaneLayout) findViewById(R.id.sp);
		
		pane.setPanelSlideListener(new EpisodilyzerPaneListener());
		pane.setCoveredFadeColor(getResources().getColor(R.color.covered_fade_color));
		pane.setSliderFadeColor(getResources().getColor(R.color.slider_fade_color));
		pane.setParallaxDistance(200);
		pane.setShadowResource(R.drawable.right_pane_shadow);
		
		pane.openPane();
		handleIntent(getIntent());
	}

	@Override
	protected void onNewIntent(Intent intent)
	{
		handleIntent(intent);
	}

	private void handleIntent(Intent intent)
	{
		if( mMode == MODE_SEARCH )
		{
			getActionBar().setTitle("Search Results");
			String seriesName = intent.getStringExtra(SearchManager.QUERY);
			new SearchShowsTask(this).execute(seriesName);
		}
	}

	void presentSearchResults(LinkedList<Show> searchResults)
	{
		ShowListFragment leftPane = (ShowListFragment) getFragmentManager().findFragmentById(R.id.leftpane);
		leftPane.populateListFromSearch(searchResults);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		if( mMode != MODE_SEARCH )
		{
			// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.episodilyzer, menu);

			// Associate searchable configuration with the SearchView
			SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
			SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
			searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
		}

		return true;
	}

	public void onChangeShow(Show show)
	{
		ShowDetailFragment rightPane = (ShowDetailFragment) getFragmentManager().findFragmentById(R.id.rightpane);
		rightPane.setShow(show);
	}
	
	public int getMode()
	{
		return mMode;
	}
}
