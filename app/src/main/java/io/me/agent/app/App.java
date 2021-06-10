package io.me.agent.app;

import io.me.agent.app.pkg.Service;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        Service service = new Service();
        service.cook("a");
        System.out.println( Service.staticMethod());
    }
}
