package com.drknotter.episodilyzer.server.model;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name="Data")
public class SearchResult {
    @ElementList(name="Series", inline=true)
    public List<SaveSeriesInfo> resultList;
}
