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
        servletRegistrationBean.addInitParameter("async","true");
        servletRegistrationBean.setAsyncSupported(true);
        return servletRegistrationBean;
    }

    public static void main(String[] args) {
//        Counter.getInstance().start();
//        ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.ADVANCED);
//        ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.PARANOID);
//        System.setProperty("io.netty.allocator.maxCachedBufferCapacity", "1");
        SpringApplication.run(GatewayApplication.class, args);
    }

}

