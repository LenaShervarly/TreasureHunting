package com.home.jsquad.knowhunt.instrumentation;

public interface IObserver<T>
{
    void notify(T args);
}
