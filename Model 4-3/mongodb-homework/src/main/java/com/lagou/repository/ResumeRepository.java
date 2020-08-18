package com.lagou.repository;

import com.lagou.bean.ResumeData;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ResumeRepository extends MongoRepository<ResumeData,String> {

    List<ResumeData> findByNameEquals(String name);
}
