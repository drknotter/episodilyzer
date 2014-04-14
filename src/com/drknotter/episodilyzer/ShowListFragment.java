package com.drknotter.episodilyzer;

import java.io.File;
import java.util.ArrayList;
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

		if( !Intent.ACTION_SEARCH.equals(getActivity().getIntent().getAction()) )
		{
			populateListFromMyShows();
		}

		return rootView;
	}

	private void populateListFromMyShows()
	{
		File filesDirectory = getActivity().getFilesDir();
		
		File[] files = filesDirectory.listFiles();
		for( File dir : files )
		{
			new InitializeShowTask().execute(dir);
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

		if( mAdapter.getCount() > 0 )
		{
			((ShowListener) getActivity()).onChangeShow(mAdapter.getItem(0));
		}
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
				bannerImage.setImageBitmap(ImageHelper.getRoundedCornerBitmap(show.mBannerBitmap,20));
			}
			else
			{
				bannerImage.setVisibility(View.GONE);
				titleText.setVisibility(View.VISIBLE);
				titleText.setText(show.get("seriesname"));
			}

			return convertView;
		}
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
			File dir = dirs[0];
			if( dir.isDirectory() )
			{
				Log.v(TAG, "series id: " + dir.getName());
				return new Show(dir);
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Show show)
		{
			if( show != null )
			{
				mAdapter.add(show);
			}
		}
	}
}

class ImageHelper {
   public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
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
