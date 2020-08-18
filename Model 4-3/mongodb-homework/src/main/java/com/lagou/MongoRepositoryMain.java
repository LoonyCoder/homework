package com.lagou;

import com.lagou.bean.ResumeData;
import com.lagou.repository.ResumeRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.List;

@SpringBootApplication
public class MongoRepositoryMain {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(MongoRepositoryMain.class,args);
        ResumeRepository resumeRepository = applicationContext.getBean(ResumeRepository.class);

        /*
        ResumeData resume  = new ResumeData();
        resume.setName("mongodb-test2");
        resume.setSalary(10000);
        resumeRepository.save(resume);
    */

        System.out.println(resumeRepository.findByNameEquals("mongodb-test2"));
    }
}
