package com.drknotter.episodilyzer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

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
		mRootView = (ViewGroup) inflater.inflate(R.layout.show_search_detail_container, container, false);
		super.onCreateView(inflater, container, savedInstanceState);

		Button addShowButton = (Button) mRootView.findViewById(R.id.add_show_button);
		addShowButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				String message = mActivity.getString(R.string.add_show_dialog_message);
				message = message.replace("@%", mShow.get("seriesname"));

				AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
				builder.setPositiveButton(mActivity.getString(android.R.string.yes), new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						new ShowDetailDownloadTask(mActivity).execute(mShow);

					}
				})
						.setNegativeButton(mActivity.getString(android.R.string.no), null)
						.setMessage(message)
						.show();
			}
		});

		return mRootView;
	}

	private static final int OVERVIEW_INDEX = 3;
	@Override
	protected void updateLayout()
	{
		super.updateLayout();
		
		TextView firstAiredText = (TextView) mRootView.findViewById(R.id.first_aired);
		try
		{
			Date firstAirDate = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(mShow.get(Show.FIRSTAIRED));
			firstAiredText.setText("First air date: " + new SimpleDateFormat("MMMM d, yyyy", Locale.US).format(firstAirDate));
		}
		catch( ParseException e )
		{
			firstAiredText.setVisibility(View.GONE);
			Log.w(TAG, "Error parsing first air date: " + mShow.get(Show.FIRSTAIRED), e);
		}
		catch( NullPointerException e )
		{
			firstAiredText.setVisibility(View.GONE);
			Log.w(TAG, "No first air date found.", e);
		}
		
		LinearLayout rootDetailContainer = (LinearLayout) mRootView.findViewById(R.id.root_detail_container);
		ExpandableLayout layout = new ExpandableLayoutBuilder()
				.setContext(mActivity)
				.setTitle("Overview")
				.setContentString(mShow.get(Show.OVERVIEW))
				.build();
		
		if( rootDetailContainer.getChildCount() > OVERVIEW_INDEX )
		{
			rootDetailContainer.removeViewAt(OVERVIEW_INDEX);
		}
		rootDetailContainer.addView(layout, OVERVIEW_INDEX);
	}
}
