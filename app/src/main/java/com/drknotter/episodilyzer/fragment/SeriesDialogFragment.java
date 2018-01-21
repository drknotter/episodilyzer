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
import com.drknotter.episodilyzer.utils.PicassoUtils;
import com.drknotter.episodilyzer.utils.SeriesUtils;
import com.drknotter.episodilyzer.view.AspectRatioImageView;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SeriesDialogFragment extends AppCompatDialogFragment {
    public static final String TAG = SeriesDialogFragment.class.getSimpleName();
    public static final String ARG_SERIES_INFO = "ARG_SERIES_INFO";

    @BindView(R.id.series_banner)
    AspectRatioImageView seriesBanner;
    @BindView(R.id.series_name)
    TextView seriesName;
    @BindView(R.id.firstAired)
    TextView firstAired;

    @BindView(R.id.download_button)
    View downloadButton;

    @BindView(R.id.overview)
    TextView overview;

    private Unbinder unbinder;

    public static SeriesDialogFragment newInstance(SaveSeriesInfo seriesInfo) {
        SeriesDialogFragment fragment = new SeriesDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SERIES_INFO, new Gson().toJson(seriesInfo));
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
        unbinder = ButterKnife.bind(this, root);

        SaveSeriesInfo seriesInfo = null;
        if (getArguments() != null) {
            try {
                seriesInfo = new Gson().fromJson(getArguments().getString(ARG_SERIES_INFO), SaveSeriesInfo.class);
            } catch (Throwable t) {
            }
        }

        if (!TextUtils.isEmpty(seriesInfo.seriesName)) {
            bindSeries(seriesInfo);
        } else {
            dismissAllowingStateLoss();
        }

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void bindSeries(final SaveSeriesInfo seriesInfo) {
        // Bind the episode image, if present.
        Uri bannerUri = null;
        if (seriesInfo.banner != null) {
            bannerUri = Uri.parse(TheTVDBService.BASE_URL)
                    .buildUpon()
                    .appendPath("banners")
                    .appendPath(seriesInfo.banner)
                    .build();
        }
        PicassoUtils.getPicasso(getContext())
                .load(bannerUri)
                .into(seriesBanner);
        seriesBanner.setVisibility(bannerUri != null ? View.VISIBLE : View.GONE);

        // Bind the episode name.
        seriesName.setText(seriesInfo.seriesName);

        // Bind the first aired date.
        setFirstAired(seriesInfo.firstAired);

        // Bind the overview, if present.
        this.overview.setVisibility(TextUtils.isEmpty(seriesInfo.overview) ? View.GONE : View.VISIBLE);
        this.overview.setText(seriesInfo.overview);

        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SeriesUtils.saveSeries(seriesInfo);
                dismissAllowingStateLoss();
            }
        });
    }

    private void setFirstAired(String firstAiredText) {
        try {
            Date firstAiredDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(firstAiredText);
            firstAired.setText(
                    getString(R.string.first_aired,
                            new SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(firstAiredDate)));
            firstAired.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            firstAired.setText(null);
            firstAired.setVisibility(View.GONE);
        }
    }
}
