package provider2.lagou;


import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.annotation.Service;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

public class ServiceTwoMain {
    public static void main(String[] args) throws  Exception{
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ProviderConfiguration.class);
        context.start();
        System.in.read();
    }
    @Configuration
    @EnableDubbo(scanBasePackages = "provider2.lagou.service.impl")
    @PropertySource("classpath:/dubbo-provider2.properties")
    static  class  ProviderConfiguration{
        @Bean
        public RegistryConfig   registryConfig(){
            RegistryConfig  registryConfig  = new RegistryConfig();
            registryConfig.setAddress("zookeeper://127.0.0.1:2181?timeout=10000");
            //registryConfig.setTimeout(10000);
            return   registryConfig;
        }
    }

}
