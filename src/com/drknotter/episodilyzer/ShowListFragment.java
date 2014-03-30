package com.drknotter.episodilyzer;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ShowListFragment extends Fragment {
	
	private static final String TAG = "ShowListFragment";
	
	private String[] mShowTitles = 
		{
			"Doctor Who",
			"Star Trek: The Next Generation",
			"Star Trek: The Next Generation",
			"Star Trek: The Next Generation",
			"Star Trek: The Next Generation",
			"Star Trek: The Next Generation",
			"Star Trek: The Next Generation",
			"Star Trek: The Next Generation",
			"Star Trek: The Next Generation",
			"Star Trek: The Next Generation",
			"Star Trek: The Next Generation",
			"Star Trek: The Next Generation",
			"Star Trek: The Next Generation",
			"Star Trek: The Next Generation",
			"Star Trek: The Next Generation",
			"Star Trek: The Next Generation",
			"Star Trek: The Next Generation",
			"Star Trek: The Next Generation",
			"Star Trek: The Next Generation",
			"Star Trek: The Next Generation",
			"Adventure Time"
		};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.show_list_container, container, true);
		ListView listView = (ListView) rootView.findViewById(R.id.showlist);
		listView.setAdapter(new ShowListAdapter());
		
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				((ShowListener) getActivity()).onChangeShow(mShowTitles[position]);
			}
		});
		
		setHasOptionsMenu(true);
		return rootView;
	}
	
	class ShowListAdapter extends BaseAdapter
	{
		private static final String TAG = "ShowListAdapter";
		
		@Override
		public int getCount() {
			return mShowTitles.length;
		}

		@Override
		public Object getItem(int position) {
			return mShowTitles[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Log.i(TAG, "in getView("+position+", ...)");
			
			if( convertView == null )
			{
				LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = (View) inflater.inflate(R.layout.show_list_item_container, null);
			}

			((TextView) convertView.findViewById(R.id.showListItemTitle)).setText(mShowTitles[position]);

			return convertView;
		}
	}
	
	public interface ShowListener
	{
		public void onChangeShow(String show);
	}
}
