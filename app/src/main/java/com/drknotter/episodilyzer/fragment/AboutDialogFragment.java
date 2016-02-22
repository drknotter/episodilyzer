package com.drknotter.episodilyzer.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.drknotter.episodilyzer.BuildConfig;
import com.drknotter.episodilyzer.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AboutDialogFragment extends DialogFragment {
    public static final String TAG = AboutDialogFragment.class.getSimpleName();

    @Bind(R.id.version)
    TextView version;

    public static AboutDialogFragment newInstance() {
        return new AboutDialogFragment();
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

        View root = inflater.inflate(R.layout.dialog_about, container, false);
        ButterKnife.bind(this, root);

        version.setText(getString(R.string.version, BuildConfig.VERSION_NAME));

        return root;
    }
}