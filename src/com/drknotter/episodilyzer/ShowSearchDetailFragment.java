package com.drknotter.episodilyzer;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class ShowSearchDetailFragment extends ShowDetailFragment
{
	private static final String TAG = "ShowSearchDetailFragment";

	private SearchResultsActivity mActivity;

	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		mActivity = (SearchResultsActivity) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		Log.v(TAG, "onCreateView");
		super.onCreateView(inflater, container, savedInstanceState);

//		Button addShowButton = (Button) mRootView.findViewById(R.id.add_show_button);
//		addShowButton.setOnClickListener(new View.OnClickListener()
//		{
//			@Override
//			public void onClick(View v)
//			{
//				String message = mActivity.getString(R.string.add_show_dialog_message);
//				message = message.replace("@%", mShow.get("seriesname"));
//
//				AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
//				builder.setPositiveButton(mActivity.getString(android.R.string.yes), new DialogInterface.OnClickListener()
//				{
//					@Override
//					public void onClick(DialogInterface dialog, int which)
//					{
//						new ShowDetailDownloadTask(mActivity).execute(mShow);
//
//					}
//				})
//						.setNegativeButton(mActivity.getString(android.R.string.no), null)
//						.setMessage(message)
//						.show();
//			}
//		});

		return mRootView;
	}

	@Override
	protected void updateLayout()
	{
		super.updateLayout();
		LinearLayout rootDetailContainer = (LinearLayout) mRootView.findViewById(R.id.root_detail_container);
		ExpandableLayout layout = new ExpandableLayoutBuilder()
				.setContext(mActivity)
				.setTitle("Overview")
				.setContent(mShow.get(Show.OVERVIEW))
				.build();
		if( rootDetailContainer.getChildCount() > 2 )
		{
			rootDetailContainer.removeViewAt(2);
		}
		rootDetailContainer.addView(layout, 2);
	}
}
