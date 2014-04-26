package com.drknotter.episodilyzer;

import java.util.LinkedList;

import com.drknotter.episodilyzer.ShowListFragment.ShowListener;

import android.os.Bundle;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.view.Menu;
import android.widget.SearchView;
import android.support.v4.widget.SlidingPaneLayout;

public class EpisodilyzerActivity extends Activity implements ShowListener
{
	public static final String TAG = "EpisodilyzerActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_episodilyzer);
		SlidingPaneLayout pane = (SlidingPaneLayout) findViewById(R.id.sp);

		pane.setPanelSlideListener(new EpisodilyzerPaneListener());
		pane.setCoveredFadeColor(getResources().getColor(R.color.covered_fade_color));
		pane.setSliderFadeColor(getResources().getColor(R.color.slider_fade_color));
		pane.setParallaxDistance(200);
		pane.setShadowResource(R.drawable.right_pane_shadow);

		pane.openPane();
	}
	
	void presentSearchResults(LinkedList<Show> searchResults)
	{
		ShowListFragment leftPane = (ShowListFragment) getFragmentManager().findFragmentById(R.id.leftpane);
		leftPane.populateListFromSearch(searchResults);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.episodilyzer, menu);

		// Associate searchable configuration with the SearchView
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
		searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

		return true;
	}

	public void onChangeShow(Show show)
	{
		ShowDetailFragment rightPane = (ShowDetailFragment) getFragmentManager().findFragmentById(R.id.rightpane);
		rightPane.setShow(show);
	}
}
