package com.drknotter.episodilyzer.fragment;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.drknotter.episodilyzer.BuildConfig;
import com.drknotter.episodilyzer.R;

public class AboutDialogFragment extends DialogFragment {
    public static final String TAG = AboutDialogFragment.class.getSimpleName();

    TextView version;
    TextView aboutTheTVDB;

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

        version = root.findViewById(R.id.version);
        aboutTheTVDB = root.findViewById(R.id.about_thetvdb);

        version.setText(getString(R.string.version, BuildConfig.VERSION_NAME));
        aboutTheTVDB.setMovementMethod(LinkMovementMethod.getInstance());

        return root;
    }
}
