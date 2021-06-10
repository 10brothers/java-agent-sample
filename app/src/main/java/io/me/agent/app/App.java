package io.me.agent.app;

import io.me.agent.app.pkg.Service;
import org.springframework.util.StringUtils;
import sun.misc.Launcher;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        /*System.out.println( "Hello World!" );
        Service service = new Service();
        List<Void> li = new ArrayList<>();
        System.out.print(service.cook());
        LocalDateTime now = LocalDateTime.now();

        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();


        Unsafe unsafe;*/
        Launcher launcher = new Launcher();

        ClassLoader.getSystemClassLoader();
        Service service = new Service();
        service.cook();

        String filename = StringUtils.getFilename("D:\\work\\maven\\repository\\org\\springframework\\spring-core\\5.3.7\\spring-core-5.3.7.jar");
        System.out.println(filename);
        System.out.println( Service.staticMethod());

    }
}
