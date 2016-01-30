package com.drknotter.episodilyzer.server.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Transient;

import java.util.List;

/**
 * Created by plunkett on 1/25/16.
 */
@Root(name = "Data")
public class FullSeries {
    @Element(name = "Series")
    public BaseSeries series;

    @ElementList(name = "Episode", inline = true)
    public List<BaseEpisode> episodes;

    @Transient
    public List<Banner> banners;
}
