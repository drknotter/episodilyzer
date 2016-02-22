package com.drknotter.episodilyzer;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import com.drknotter.episodilyzer.adapter.EpisodilyzerAdapter;
import com.drknotter.episodilyzer.fragment.AboutDialogFragment;
import com.drknotter.episodilyzer.model.Series;
import com.drknotter.episodilyzer.utils.SeriesUtils;

import java.util.ArrayList;
import java.util.List;

public class EpisodilyzerActivity extends RecyclerViewActivity {
    private List<Series> myShows = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EpisodilyzerAdapter adapter = new EpisodilyzerAdapter(myShows);
        adapter.setHasStableIds(true);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        myShows.clear();
        myShows.addAll(SeriesUtils.allSeries());
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_episodilyzer, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
                FragmentManager fragmentMananager = getSupportFragmentManager();
                AboutDialogFragment fragment = (AboutDialogFragment) fragmentMananager.findFragmentByTag(AboutDialogFragment.TAG);
                if (fragment != null) {
                    fragment.dismissAllowingStateLoss();
                }
                fragment = AboutDialogFragment.newInstance();
                fragment.show(fragmentMananager, AboutDialogFragment.TAG);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
