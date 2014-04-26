package com.drknotter.episodilyzer;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ExpandableLayoutBuilder
{
	private String mTitle = null;
	private String mContent = null;
	private Context mContext = null;

	public ExpandableLayoutBuilder()
	{
	}

	public ExpandableLayoutBuilder setTitle(String title)
	{
		mTitle = title;
		return this;
	}

	public ExpandableLayoutBuilder setContent(String content)
	{
		mContent = content;
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
		layout.init(mTitle, mContent);
		return layout;
	}
}

class ExpandableLayout extends LinearLayout
{
	TextView mTitleText;
	TextView mContentText;

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

	public void init(String title, String content)
	{
		mTitleText = (TextView) this.findViewById(R.id.item_title);
		mContentText = (TextView) this.findViewById(R.id.item_content);

		mTitleText.setText(title);
		mContentText.setText(content);
		
		Drawable arrow = getContext().getResources().getDrawable(R.drawable.expand);
		int size = getContext().getResources().getDimensionPixelSize(R.dimen.expandable_item_height);
		arrow.setBounds(0, 0, size/2, size/2);
		mTitleText.setCompoundDrawables(null, null, arrow, null);

		mTitleText.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Drawable arrow;
				mContentText.clearAnimation();
				if( mContentText.getVisibility() == View.GONE )
				{
					mContentText.setVisibility(View.VISIBLE);
					arrow = getContext().getResources().getDrawable(R.drawable.collapse);
					mContentText.startAnimation(AnimationUtils.loadAnimation(ExpandableLayout.this.getContext(), R.anim.expand_vertical));
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
							mContentText.setVisibility(View.GONE);
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
					mContentText.startAnimation(animation);
				}
				int size = getContext().getResources().getDimensionPixelSize(R.dimen.expandable_item_height);
				arrow.setBounds(0, 0, size/2, size/2);
				mTitleText.setCompoundDrawables(null, null, arrow, null);
			}
		});
	}
}
