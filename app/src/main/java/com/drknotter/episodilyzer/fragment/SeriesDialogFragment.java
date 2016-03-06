package com.drknotter.episodilyzer.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.drknotter.episodilyzer.R;
import com.drknotter.episodilyzer.server.TheTVDBService;
import com.drknotter.episodilyzer.server.model.SaveSeriesInfo;
import com.drknotter.episodilyzer.view.AspectRatioImageView;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SeriesDialogFragment extends AppCompatDialogFragment {
    public static final String TAG = SeriesDialogFragment.class.getSimpleName();
    public static final String ARG_SERIES_BANNER = "ARG_SERIES_BANNER";
    public static final String ARG_SERIES_NAME = "ARG_SERIES_NAME";
    public static final String ARG_SERIES_OVERVIEW = "ARG_SERIES_OVERVIEW";

    @Bind(R.id.series_banner)
    AspectRatioImageView seriesBanner;
    @Bind(R.id.series_name)
    TextView seriesName;
    @Bind(R.id.overview)
    TextView overview;

    public static SeriesDialogFragment newInstance(SaveSeriesInfo seriesInfo) {
        SeriesDialogFragment fragment = new SeriesDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SERIES_BANNER, seriesInfo.banner);
        args.putString(ARG_SERIES_NAME, seriesInfo.seriesName);
        args.putString(ARG_SERIES_OVERVIEW, seriesInfo.overview);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, R.style.AppTheme_Dialog);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View root = inflater.inflate(R.layout.dialog_series, container, false);
        ButterKnife.bind(this, root);

        String banner = null, name = null, overview = null;
        if (getArguments() != null) {
            banner = getArguments().getString(ARG_SERIES_BANNER);
            name = getArguments().getString(ARG_SERIES_NAME);
            overview = getArguments().getString(ARG_SERIES_OVERVIEW);
        }

        if (!TextUtils.isEmpty(name)) {
            bindSeries(banner, name, overview);
        } else {
            dismissAllowingStateLoss();
        }

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void bindSeries(String banner, String name, String overview) {
        // Bind the episode image, if present.
        Uri bannerUri = null;
        if (banner != null) {
            bannerUri = Uri.parse(TheTVDBService.BASE_URL)
                    .buildUpon()
                    .appendPath("banners")
                    .appendPath(banner)
                    .build();
        }
        Picasso.with(getContext())
                .load(bannerUri)
                .into(seriesBanner);
        seriesBanner.setVisibility(bannerUri != null ? View.VISIBLE : View.GONE);

        // Bind the episode name.
        seriesName.setText(name);

        // Bind the overview, if present.
        this.overview.setVisibility(TextUtils.isEmpty(overview) ? View.GONE : View.VISIBLE);
        this.overview.setText(overview);
    }

}
