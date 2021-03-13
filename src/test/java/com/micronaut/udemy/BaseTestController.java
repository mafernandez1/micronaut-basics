package com.micronaut.udemy;

import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;

import javax.inject.Inject;

public class BaseTestController  {

    @Inject
    @Client("/")
    public RxHttpClient client;

}
