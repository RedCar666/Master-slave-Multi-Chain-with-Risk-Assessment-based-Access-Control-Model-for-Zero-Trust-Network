package org.com.fisco.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @Auther ChenFei
 * @Date Create On 2020/12/2 16:50
 */
@Configuration
public class ApplicationContextConfig {

    @Bean
    //使用@LoadBalanced注解赋予RestTemplate负载均衡的能力
    //@LoadBalanced
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
}
