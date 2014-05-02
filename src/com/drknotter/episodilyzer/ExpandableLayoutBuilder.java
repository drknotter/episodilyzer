package com.drknotter.episodilyzer;

import java.util.ArrayList;
import java.util.TreeMap;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ExpandableLayoutBuilder
{
	private static enum LayoutType
	{
		TEXT,
		NESTED
	};

	private LayoutType mLayoutType = null;
	private String mTitle = null;
	private String mContentString = null;
	private Context mContext = null;
	private ArrayList<ExpandableLayout> mLayoutList = null;

	public ExpandableLayoutBuilder()
	{
	}

	public ExpandableLayoutBuilder setTitle(String title)
	{
		mTitle = title;
		return this;
	}

	public ExpandableLayoutBuilder setContentString(String content)
	{
		if( mLayoutType != null )
		{
			return this;
		}
		mLayoutType = LayoutType.TEXT;
		mContentString = content;
		return this;
	}
	
	public ExpandableLayoutBuilder setSeries(TreeMap<String, Season> series)
	{
		if( mLayoutType != null )
		{
			return this;
		}
		
		mLayoutType = LayoutType.NESTED;
		mLayoutList = new ArrayList<ExpandableLayout>();
		
		for( String seasonNumber : series.keySet() )
		{
			ExpandableLayout episodeLayout = new ExpandableLayoutBuilder()
					.setContext(mContext)
					.setTitle("Season " + seasonNumber)
					.setSeason(series.get(seasonNumber))
					.build();
			mLayoutList.add(episodeLayout);
		}

		return this;
	}

	public ExpandableLayoutBuilder setSeason(Season season)
	{
		if( mLayoutType != null )
		{
			return this;
		}

		mLayoutType = LayoutType.NESTED;
		mLayoutList = new ArrayList<ExpandableLayout>();

		for( String episodeNumber : season.keySet() )
		{
			ExpandableLayout episodeLayout = new ExpandableLayoutBuilder()
					.setContext(mContext)
					.setTitle("Episode " + episodeNumber)
					.setContentString(season.get(episodeNumber).get(Episode.OVERVIEW))
					.build();
			mLayoutList.add(episodeLayout);
		}

		return this;
	}

	public ExpandableLayoutBuilder setContext(Context context)
	{
		mContext = context;
		return this;
	}

	public ExpandableLayout build()
	{
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ExpandableLayout layout = (ExpandableLayout) inflater.inflate(R.layout.show_detail_expandable_item_container, null);
		layout.init(mTitle);

		// Insert the contents of the content layout.
		if( mLayoutType == LayoutType.TEXT )
		{
			TextView textView = new TextView(mContext);
			textView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			textView.setText(mContentString);
			layout.addViewToContent(textView);
		}
		else if( mLayoutType == LayoutType.NESTED )
		{
			for( ExpandableLayout child : mLayoutList )
			{
				layout.addViewToContent(child);
			}
		}

		return layout;
	}
}

class ExpandableLayout extends LinearLayout
{
	TextView mTitleText;
	LinearLayout mContent;

	public ExpandableLayout(Context context)
	{
		super(context);
	}

	public ExpandableLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public ExpandableLayout(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	public void init(String title)
	{
		mTitleText = (TextView) this.findViewById(R.id.item_title);
		mContent = (LinearLayout) this.findViewById(R.id.item_content);

		mTitleText.setText(title);

		Drawable arrow = getContext().getResources().getDrawable(R.drawable.expand);
		int size = getContext().getResources().getDimensionPixelSize(R.dimen.expandable_item_height);
		arrow.setBounds(0, 0, size / 2, size / 2);
		mTitleText.setCompoundDrawables(null, null, arrow, null);

		mTitleText.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Drawable arrow;
				mContent.clearAnimation();

				if( mContent.getVisibility() == View.GONE )
				{
					mContent.setVisibility(View.VISIBLE);
					arrow = getContext().getResources().getDrawable(R.drawable.collapse);
					mContent.startAnimation(AnimationUtils.loadAnimation(ExpandableLayout.this.getContext(), R.anim.expand_vertical));
				}
				else
				{
					arrow = getContext().getResources().getDrawable(R.drawable.expand);
					Animation animation = AnimationUtils.loadAnimation(ExpandableLayout.this.getContext(), R.anim.collapse_vertical);
					animation.setAnimationListener(new AnimationListener()
					{
						@Override
						public void onAnimationEnd(Animation arg0)
						{
							mContent.setVisibility(View.GONE);
						}

						@Override
						public void onAnimationRepeat(Animation arg0)
						{
						}

						@Override
						public void onAnimationStart(Animation arg0)
						{
						}
					});
					mContent.startAnimation(animation);
				}
				int size = getContext().getResources().getDimensionPixelSize(R.dimen.expandable_item_height);
				arrow.setBounds(0, 0, size / 2, size / 2);
				mTitleText.setCompoundDrawables(null, null, arrow, null);
			}
		});
	}

	public void addViewToContent(View v)
	{
		mContent.addView(v);
	}
}
