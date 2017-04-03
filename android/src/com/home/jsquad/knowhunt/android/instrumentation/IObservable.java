package com.home.jsquad.knowhunt.android.instrumentation;

public interface IObservable<T>
{
    void subscribe(IObserver<T> observer);
    void unSubscribe(IObserver<T> observer);
}
