package com.lagou.dao;


import com.lagou.pojo.LagouToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;


public interface TokenRepository extends JpaRepository<LagouToken,Integer>, JpaSpecificationExecutor<LagouToken> {

    @Query(value = "select * from lagou_token where email = ?1 limit 1",nativeQuery = true)
    LagouToken findByEmail(String email);


    @Query(value = "select * from lagou_token where token = ?1 limit 1",nativeQuery = true)
    LagouToken findByToken(String token);
}
