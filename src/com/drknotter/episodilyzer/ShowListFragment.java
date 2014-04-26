package com.drknotter.episodilyzer;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.widget.SlidingPaneLayout;

public class ShowListFragment extends Fragment
{
	@SuppressWarnings("unused")
	private static final String TAG = "ShowListFragment";
	ArrayList<Show> mShowList = new ArrayList<Show>();

	private ShowListAdapter mAdapter;
	private boolean mDetailShown = false;

	@Override
	public void onResume()
	{
		super.onResume();
		if( !Intent.ACTION_SEARCH.equals(getActivity().getIntent().getAction()) )
		{
			mAdapter.clear();
			populateListFromMyShows();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.show_list_container, container, true);

		ListView listView = (ListView) rootView.findViewById(R.id.showlist);
		mAdapter = new ShowListAdapter(getActivity(), R.layout.show_list_item_container, mShowList);
		listView.setAdapter(mAdapter);
		listView.setDivider(null);

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				((ShowListener) getActivity()).onChangeShow(mAdapter.getItem(position));
				SlidingPaneLayout pane = (SlidingPaneLayout) getActivity().findViewById(R.id.sp);
				pane.closePane();
			}
		});

		((TextView) rootView.findViewById(R.id.thetvdbInfo)).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				try
				{
					Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.thetvdb.com"));
					startActivity(myIntent);
				}
				catch( ActivityNotFoundException e )
				{
					Toast.makeText(ShowListFragment.this.getActivity(),
							"No application can handle this request, Please install a web browser",
							Toast.LENGTH_LONG).show();
					e.printStackTrace();
				}
			}
		});

		return rootView;
	}

	private void populateListFromMyShows()
	{
		File filesDirectory = getActivity().getFilesDir();

		File[] files = filesDirectory.listFiles();
		Log.v(TAG, "files.length: " + files.length);
		for( File dir : files )
		{
			Log.v(TAG, "dir.getName(): " + dir.getName());
			new InitializeShowTask().execute(dir);
		}

		if( mAdapter.getCount() > 0 )
		{
			((ShowListener) getActivity()).onChangeShow(mAdapter.getItem(0));
		}
	}

	public void notifyAdapterDataSetChanged()
	{
		mAdapter.notifyDataSetChanged();
	}

	public void populateListFromSearch(LinkedList<Show> searchResults)
	{
		for( Show show : searchResults )
		{
			mAdapter.add(show);
		}
		mAdapter.notifyDataSetChanged();
	}

	class ShowListAdapter extends ArrayAdapter<Show>
	{
		@SuppressWarnings("unused")
		private static final String TAG = "ShowListAdapter";

		public ShowListAdapter(Context context, int resource)
		{
			super(context, resource);
		}

		public ShowListAdapter(Context context, int resource, List<Show> list)
		{
			super(context, resource, list);
		}

		@Override
		public int getCount()
		{
			return mShowList.size();
		}

		@Override
		public Show getItem(int position)
		{
			return mShowList.get(position);
		}

		@Override
		public long getItemId(int position)
		{
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			if( convertView == null )
			{
				LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = (View) inflater.inflate(R.layout.show_list_item_container, null);
			}

			Show show = mShowList.get(position);
			TextView titleText = (TextView) convertView.findViewById(R.id.showListItemTitle);
			ImageView bannerImage = (ImageView) convertView.findViewById(R.id.showListItemBanner);
			if( show.mBannerBitmap != null )
			{
				titleText.setVisibility(View.GONE);
				bannerImage.setVisibility(View.VISIBLE);
				bannerImage.setImageBitmap(show.mBannerBitmap);
			}
			else
			{
				bannerImage.setVisibility(View.GONE);
				titleText.setVisibility(View.VISIBLE);
				titleText.setText(show.get(Show.SERIESNAME));
			}

			return convertView;
		}
	}
	
	public synchronized Show loadShow(File dir)
	{
		Show theShow = null;
		HashSet<String> seriesIds = new HashSet<String>();
		
		for( Show show : mShowList )
		{
			seriesIds.add(show.get(Show.SERIESID));
		}
		
		if( dir.isDirectory() )
		{
			if( !seriesIds.contains(dir.getName()) )
			{
				Log.v(TAG, "series id: " + dir.getName());
				theShow = new Show(dir);
			}
		}

		return theShow;
	}

	public interface ShowListener
	{
		public void onChangeShow(Show show);
	}

	class InitializeShowTask extends AsyncTask<File, Void, Show>
	{
		@Override
		protected Show doInBackground(File... dirs)
		{
			return loadShow(dirs[0]);	
		}

		@Override
		protected void onPostExecute(Show show)
		{
			if( show != null )
			{
				mAdapter.add(show);
				if( !mDetailShown )
				{
					((ShowListener) getActivity()).onChangeShow(mAdapter.getItem(0));
					mDetailShown = true;
				}
			}
		}
	}
}

class ImageHelper
{
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels)
	{
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
				.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}
}
