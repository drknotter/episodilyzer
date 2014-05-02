package com.drknotter.episodilyzer;

import com.drknotter.episodilyzer.R;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SlidingPaneLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ShowDetailFragment extends Fragment
{
	private static final String TAG = "ShowDetailFragment";

	protected ViewGroup mRootView;
	protected Show mShow;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		Log.v(TAG, "onCreateView");

		if( mRootView == null )
		{
			mRootView = (ViewGroup) inflater.inflate(R.layout.show_detail_container, container, false);
		}
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
		mShow = show;
		updateLayout();
	}

	protected void updateLayout()
	{
		TextView titleText = (TextView) mRootView.findViewById(R.id.title_text);
		ImageView bannerImage = (ImageView) mRootView.findViewById(R.id.title_image);
		if( mShow.mBannerBitmap != null )
		{
			titleText.setVisibility(View.GONE);
			bannerImage.setVisibility(View.VISIBLE);
			bannerImage.setImageBitmap(mShow.mBannerBitmap);
		}
		else
		{
			bannerImage.setVisibility(View.GONE);
			titleText.setVisibility(View.VISIBLE);
			titleText.setText(mShow.get(Show.SERIESNAME));
		}
		
		int SEASON_LIST_INDEX = 2;
		LinearLayout rootDetailContainer = (LinearLayout) mRootView.findViewById(R.id.root_detail_container);
		ExpandableLayout layout = new ExpandableLayoutBuilder()
				.setContext(getActivity())
				.setTitle("Seasons")
				.setSeries(mShow.mSeasons)
				.build();
		
		if( rootDetailContainer.getChildCount() > SEASON_LIST_INDEX )
		{
			rootDetailContainer.removeViewAt(SEASON_LIST_INDEX);
		}
		rootDetailContainer.addView(layout, SEASON_LIST_INDEX);

	}
}
