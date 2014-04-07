package com.drknotter.episodilyzer;

import com.drknotter.episodilyzer.R;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SlidingPaneLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ShowDetailFragment extends Fragment
{
	private View mRootView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		mRootView = inflater.inflate(R.layout.show_detail_container, container, false);
		mRootView.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				SlidingPaneLayout pane = (SlidingPaneLayout) getActivity().findViewById(R.id.sp);
				pane.openPane();
			}
		});

		return mRootView;
	}

	public void setShow(Show show)
	{
		((TextView) mRootView.findViewById(R.id.title_text)).setText(show.mSeriesName);
		((TextView) mRootView.findViewById(R.id.overview_text)).setText(show.mOverview);
		((TextView) mRootView.findViewById(R.id.first_aired_text)).setText("First air date: " + show.mFirstAired);
	}
}
