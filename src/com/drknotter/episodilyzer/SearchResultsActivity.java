package com.drknotter.episodilyzer;

import java.util.LinkedList;

import com.drknotter.episodilyzer.ShowListFragment.ShowListener;

import android.app.Activity;
import android.app.SearchManager;
import android.os.Bundle;
import android.support.v4.widget.SlidingPaneLayout;

public class SearchResultsActivity extends Activity implements ShowListener
{
	public static final String TAG = "SearchResultsActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_results);
		
		SlidingPaneLayout pane = (SlidingPaneLayout) findViewById(R.id.sp);

		pane.setPanelSlideListener(new EpisodilyzerPaneListener());
		pane.setCoveredFadeColor(getResources().getColor(R.color.covered_fade_color));
		pane.setSliderFadeColor(getResources().getColor(R.color.slider_fade_color));
		pane.setParallaxDistance(200);
		pane.setShadowResource(R.drawable.right_pane_shadow);

		pane.openPane();
		
		getActionBar().setTitle("Search Results");
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		String seriesName = getIntent().getStringExtra(SearchManager.QUERY);
		new SearchShowsTask(this).execute(seriesName);
	}

	void presentSearchResults(LinkedList<Show> searchResults)
	{
		ShowListFragment leftPane = (ShowListFragment) getFragmentManager().findFragmentById(R.id.leftpane);
		leftPane.populateListFromSearch(searchResults);
	}

	public void onChangeShow(Show show)
	{
		ShowDetailFragment rightPane = (ShowSearchDetailFragment) getFragmentManager().findFragmentById(R.id.rightpane);
		rightPane.setShow(show);
	}
}
