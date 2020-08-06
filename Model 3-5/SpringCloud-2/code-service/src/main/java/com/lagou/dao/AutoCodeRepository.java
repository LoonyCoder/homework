package com.lagou.dao;

import com.lagou.pojo.AuthCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;


public interface AutoCodeRepository extends JpaRepository<AuthCode,Integer>, JpaSpecificationExecutor<AuthCode> {

    @Query(value = "select * from lagou_auth_code where email = ?1 order by createtime desc limit 1",nativeQuery = true)
    public AuthCode findLatestByEmail(String email);
}
