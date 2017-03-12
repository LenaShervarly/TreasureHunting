package com.home.croaton.followme.domain;

import com.home.croaton.followme.activities.ExcursionOverviewActivity;
import com.home.croaton.followme.download.ExcursionDownloadManager;

public interface IExcursion
{
    Route getRoute();
    String getKey();
}
