package com.home.jsquad.knowhunt.instrumentation;

public interface IObservable<T>
{
    void subscribe(IObserver<T> observer);
    void unSubscribe(IObserver<T> observer);
}
