package com.drknotter.episodilyzer.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.drknotter.episodilyzer.R;
import com.drknotter.episodilyzer.server.TheTVDBService;
import com.drknotter.episodilyzer.server.model.SaveSeriesInfo;
import com.drknotter.episodilyzer.utils.PicassoUtils;
import com.drknotter.episodilyzer.utils.SeriesUtils;
import com.drknotter.episodilyzer.view.AspectRatioImageView;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SeriesDialogFragment extends AppCompatDialogFragment {
    public static final String TAG = SeriesDialogFragment.class.getSimpleName();
    public static final String ARG_SERIES_INFO = "ARG_SERIES_INFO";

    AspectRatioImageView seriesBanner;
    TextView seriesName;
    TextView firstAired;
    View downloadButton;
    TextView overview;

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
        seriesBanner = root.findViewById(R.id.series_banner);
        seriesName = root.findViewById(R.id.series_name);
        firstAired = root.findViewById(R.id.first_aired);
        downloadButton = root.findViewById(R.id.download_button);
        overview = root.findViewById(R.id.overview);

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
        seriesBanner = null;
        seriesName = null;
        firstAired = null;
        downloadButton = null;
        overview = null;
    }

    private void bindSeries(final SaveSeriesInfo seriesInfo) {
        // Bind the episode image, if present.
        Uri bannerUri = null;
        if (seriesInfo.banner != null) {
            Log.d("FindMe", "banner: " + seriesInfo.banner);
            bannerUri = Uri.parse(TheTVDBService.WEB_URL + seriesInfo.banner);
            Log.d("FindMe", "uri: " + bannerUri.toString());
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
