package com.home.croaton.followme.domain;

import java.util.List;

public interface IExcursionBrief {

    String getKey();
    String getThumbnailFilePath();
    ExcursionBriefContent getContentByLanguage(String language);
    boolean getUseDirections();
    List<SerializableGeoPoint> getArea();
}
