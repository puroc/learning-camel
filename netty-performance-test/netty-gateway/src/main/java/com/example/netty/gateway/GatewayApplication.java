package com.example.netty.gateway;

import org.apache.camel.component.servlet.CamelHttpTransportServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;

import javax.servlet.ServletException;

@SpringBootApplication
@ImportResource(locations = {"classpath*:spring-context.xml"})
public class GatewayApplication {

    @Bean
    public ServletRegistrationBean servletRegistrationBean() throws ServletException {
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean();
        CamelHttpTransportServlet camelHttpTransportServlet = new CamelHttpTransportServlet();
        servletRegistrationBean.setServlet(camelHttpTransportServlet);
        servletRegistrationBean.setName("camelServlet");
        servletRegistrationBean.addUrlMappings("/camel/*");
        servletRegistrationBean.addInitParameter("async", "true");
        servletRegistrationBean.setAsyncSupported(true);
        return servletRegistrationBean;
    }

    public static void main(String[] args) {
//        Counter.getInstance().start();
//        ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.ADVANCED);
//        ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.PARANOID);
//        System.setProperty("io.netty.allocator.maxCachedBufferCapacity", "1");


//        System.setProperty("hawtio.authenticationEnabled", "false");
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Main main = new Main();
//                main.setPort(5555);
//                main.setWar("/Users/puroc/git/learning-camel/netty-performance-test/netty-gateway/lib/hawtio-default-2.5.0.war");
//                try {
//                    main.run();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//

        SpringApplication.run(GatewayApplication.class, args);
    }

}

