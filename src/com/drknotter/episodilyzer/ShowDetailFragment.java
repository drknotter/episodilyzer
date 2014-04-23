package com.drknotter.episodilyzer;

import java.io.File;

import com.drknotter.episodilyzer.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.SlidingPaneLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class ShowDetailFragment extends Fragment
{
	private static final String TAG = "ShowDetailFragment";

	private View mRootView;
	private EpisodilyzerActivity mActivity;
	private Show mShow;

	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		mActivity = (EpisodilyzerActivity) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		Log.v(TAG, "onCreateView");

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

		Button addRemoveShowButton = (Button) mRootView.findViewById(R.id.add_show_button);
		if( mActivity.getMode() == EpisodilyzerActivity.MODE_SEARCH )
		{
			addRemoveShowButton.setOnClickListener(new View.OnClickListener()
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
		}
		else
		{
			mRootView.findViewById(R.id.anti_minus_sign).setVisibility(View.GONE);
			addRemoveShowButton.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					String message = mActivity.getString(R.string.remove_show_dialog_message);
					message = message.replace("@%", mShow.get(Show.SERIESNAME));

					AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
					builder.setPositiveButton(mActivity.getString(android.R.string.yes), new DialogInterface.OnClickListener()
					{
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							Log.v(TAG, mShow.toString());
							String directoryString = mActivity.getFilesDir().getAbsolutePath() + "/" + mShow.get(Show.ID);
							Log.v(TAG, "directoryString: " + directoryString);
							File directory = new File(directoryString);
							if( directory.isDirectory() )
							{
								Log.v(TAG, "deleting directory " + directory);
								deleteRecursive(directory);
							}
						}
					})
							.setNegativeButton(mActivity.getString(android.R.string.no), null)
							.setMessage(message)
							.show();
				}
			});
		}

		return mRootView;
	}
	
	private void deleteRecursive(File fileOrDirectory) {
	    if (fileOrDirectory.isDirectory())
	        for (File child : fileOrDirectory.listFiles())
	            deleteRecursive(child);

	    fileOrDirectory.delete();
	}

	public void setShow(Show show)
	{
		mShow = show;
		updateLayout();
	}

	private void updateLayout()
	{
		((TextView) mRootView.findViewById(R.id.title_text)).setText(mShow.get(Show.SERIESNAME));
		((TextView) mRootView.findViewById(R.id.overview_text)).setText(mShow.get(Show.OVERVIEW));
		((TextView) mRootView.findViewById(R.id.first_aired_text)).setText("First air date: " + mShow.get(Show.FIRSTAIRED));
	}
}
