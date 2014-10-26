package org.slf4j.impl;

import org.slf4j.ILoggerFactory;
import org.slf4j.spi.LoggerFactoryBinder;

import pl.brightinventions.slf4android.AndroidLoggerFactory;

public class StaticLoggerBinder implements LoggerFactoryBinder {
    public static final String REQUESTED_API_VERSION = "1.7";
    private static final ILoggerFactory factory = new AndroidLoggerFactory();
    private static final String factoryClassName = AndroidLoggerFactory.class.getName();
    private static final StaticLoggerBinder instance = new StaticLoggerBinder();

    //This method is required by slf4j
    //@Override
    public static StaticLoggerBinder getSingleton() {
        return instance;
    }

    @Override
    public ILoggerFactory getLoggerFactory() {
        return factory;
    }

    @Override
    public String getLoggerFactoryClassStr() {
        return factoryClassName;
    }
}
