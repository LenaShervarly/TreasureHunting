package com.home.croaton.followme.instrumentation;

public interface IObserver<T>
{
    void notify(T args);
}
