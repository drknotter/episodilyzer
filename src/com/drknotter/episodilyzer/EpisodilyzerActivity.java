package com.drknotter.episodilyzer;

import java.util.ArrayList;
import com.drknotter.episodilyzer.ShowListFragment.ShowListener;

import android.os.Bundle;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.widget.SearchView;
import android.support.v4.widget.SlidingPaneLayout;

public class EpisodilyzerActivity extends Activity implements ShowListener
{
	public String mMode = "";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_episodilyzer);
		SlidingPaneLayout pane = (SlidingPaneLayout) findViewById(R.id.sp);
		pane.setPanelSlideListener(new EpisodilyzerPaneListener());
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
		if( Intent.ACTION_SEARCH.equals(intent.getAction()) )
		{
			getActionBar().setTitle("Search Results");
			String seriesName = intent.getStringExtra(SearchManager.QUERY);
			new SearchShowsTask(this).execute(seriesName);
			mMode = "search";
		}
		else
		{
			mMode = "default";
		}
	}

	void presentSearchResults(ArrayList<Show> searchResults)
	{
		ShowListFragment leftPane = (ShowListFragment) getFragmentManager().findFragmentById(R.id.leftpane);
		leftPane.populateListFromSearch(searchResults);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		if( !Intent.ACTION_SEARCH.equals(getIntent().getAction()) )
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

	@Override
	public void onChangeShow(Show show)
	{
		ShowDetailFragment rightPane = (ShowDetailFragment) getFragmentManager().findFragmentById(R.id.rightpane);
		rightPane.setShow(show);
	}
}
