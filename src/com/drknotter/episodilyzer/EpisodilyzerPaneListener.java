package com.drknotter.episodilyzer;

import android.support.v4.widget.SlidingPaneLayout;
import android.util.Log;
import android.view.View;

public class EpisodilyzerPaneListener implements SlidingPaneLayout.PanelSlideListener
{
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
