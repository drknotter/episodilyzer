package com.drknotter.episodilyzer;

import com.drknotter.episodilyzer.ShowListFragment.ShowListener;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.support.v4.widget.SlidingPaneLayout;

public class EpisodilyzerActivity extends Activity implements ShowListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_episodilyzer);
		SlidingPaneLayout pane = (SlidingPaneLayout) findViewById(R.id.sp);
		pane.setPanelSlideListener(new PaneListener());
		pane.openPane();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.episodilyzer, menu);
		return true;
	}

	private class PaneListener implements SlidingPaneLayout.PanelSlideListener {

		private static final String TAG = "PaneListener";
		
		@Override
		public void onPanelClosed(View view) {
			Log.i(TAG, "Panel closed.");
		}

		@Override
		public void onPanelOpened(View view) {
			Log.i(TAG, "Panel opened.");
		}

		@Override
		public void onPanelSlide(View view, float arg1) {
		}

	}

	@Override
	public void onChangeShow(String show)
	{
		ShowDetailFragment rightPane = (ShowDetailFragment) getFragmentManager().findFragmentById(R.id.rightpane);
		rightPane.setShow(show);
	}
}
