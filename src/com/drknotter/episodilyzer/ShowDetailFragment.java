package com.drknotter.episodilyzer;
import com.drknotter.episodilyzer.R;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class ShowDetailFragment extends Fragment {
	
	private View mRootView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		mRootView = inflater.inflate(R.layout.show_detail_container, container, false);
		
		((TextView) mRootView.findViewById(R.id.title_text)).setText("Title text here.");
		
		return mRootView;
	}

	public void setShow(String text)
	{
		((TextView) mRootView.findViewById(R.id.title_text)).setText(text);
	}
}
