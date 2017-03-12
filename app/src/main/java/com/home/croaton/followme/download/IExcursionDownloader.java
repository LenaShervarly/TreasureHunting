package com.home.croaton.followme.download;

import com.home.croaton.followme.domain.Game;
import com.home.croaton.followme.instrumentation.IObservable;

public interface IExcursionDownloader {
    Game downloadExcursion(String language);
    IObservable<Integer> getProgressObservable();
}
