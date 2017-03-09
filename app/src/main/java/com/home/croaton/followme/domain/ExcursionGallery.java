package com.home.croaton.followme.domain;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;

@Root(name = "excursionGallery")
public class ExcursionGallery {
    @ElementList(required = true, inline = true)
    private ArrayList<ExcursionBrief> availableExcursions;

    public ExcursionGallery() {
        this(new ArrayList<ExcursionBrief>());
    }

    public ExcursionGallery(ArrayList<ExcursionBrief> availableExcursions) {
        this.availableExcursions = availableExcursions;
    }

    public ArrayList<ExcursionBrief> getAvailableExcursions() {
        return availableExcursions;
    }
}
