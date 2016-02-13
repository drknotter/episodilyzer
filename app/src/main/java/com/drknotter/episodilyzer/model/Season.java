package com.drknotter.episodilyzer.model;

import com.activeandroid.query.Select;

public class Season {
    public Season(int id, int number) {
        this.id = id;
        this.number = number;
    }
    public int id;
    public int number;

    public boolean areAllEpisodesSelected() {
        return !(new Select().from(Episode.class)
                .where("seasonId = ?", id)
                .and("selected = ?", false)
                .exists());
    }
}
