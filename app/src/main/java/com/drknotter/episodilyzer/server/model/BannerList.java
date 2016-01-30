package com.drknotter.episodilyzer.server.model;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by plunkett on 1/27/16.
 */
@Root(name="Banners", strict = false)
public class BannerList {
    @ElementList(inline = true, name = "Banner")
    public List<Banner> banners;
}
