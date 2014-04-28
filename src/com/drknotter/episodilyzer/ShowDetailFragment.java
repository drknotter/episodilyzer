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

//		Button addRemoveShowButton = (Button) mRootView.findViewById(R.id.add_show_button);
//		mRootView.findViewById(R.id.anti_minus_sign).setVisibility(View.GONE);
//		addRemoveShowButton.setOnClickListener(new View.OnClickListener()
//		{
//			@Override
//			public void onClick(View v)
//			{
//				String message = mActivity.getString(R.string.remove_show_dialog_message);
//				message = message.replace("@%", mShow.get(Show.SERIESNAME));
//
//				AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
//				builder.setPositiveButton(mActivity.getString(android.R.string.yes), new DialogInterface.OnClickListener()
//				{
//					@Override
//					public void onClick(DialogInterface dialog, int which)
//					{
//						Log.v(TAG, mShow.toString());
//						String directoryString = mActivity.getFilesDir().getAbsolutePath() + "/" + mShow.get(Show.ID);
//						Log.v(TAG, "directoryString: " + directoryString);
//						File directory = new File(directoryString);
//						if( directory.isDirectory() )
//						{
//							Log.v(TAG, "deleting directory " + directory);
//							deleteRecursive(directory);
//						}
//					}
//				})
//						.setNegativeButton(mActivity.getString(android.R.string.no), null)
//						.setMessage(message)
//						.show();
//			}
//		});

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
	}
}
