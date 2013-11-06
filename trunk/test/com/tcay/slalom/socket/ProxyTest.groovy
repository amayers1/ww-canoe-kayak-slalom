package com.tcay.slalom.socket

import org.mockito.Mockito
import org.mockito.invocation.InvocationOnMock
import org.mockito.stubbing.Answer

//import org.mockito.Mockito

import static org.mockito.Mockito.*;

//import groovy.mock.interceptor.MockFor
import org.testng.Assert
import org.testng.annotations.*;

/**
 * Created with IntelliJ IDEA.
 * User: allen
 * Date: 11/2/13
 * Time: 5:00 PM
 * To change this template use File | Settings | File Templates.
 */
class ProxyTest {

    Proxy proxy;

    @BeforeClass
    void setUp() {

    }

    @Test
    public void isStandAloneDefault() {
        Client mockedClient = mock(Client.class);

        Mockito.doAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                return "called with arguments: " + args;
            }
        }).when(mockedClient).connectToServer();

        proxy = new Proxy(mockedClient);

        verify(mockedClient).connectToServer();
        verifyNoMoreInteractions(mockedClient);

        Assert.assertEquals(proxy.isStandAlone(), true);
    }


    @Test
    public void isNotStandAlone() {
        proxy = new Proxy(null);
        Assert.assertEquals(proxy.isStandAlone(), false);
    }



    //def service = [retrieveRate:{ new ExchangeRate(1.45, 0.57) }] as ExchangeRateService
    //def sterlingConverter = new SterlingCurrencyConverter(service)
    //def sterlingConverter = new SterlingCurrencyConverter(service)


    void tearDown() {

    }
}

