package com.lagou.dao;



import com.lagou.pojo.LagouUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;


public interface UserRepository extends JpaRepository<LagouUser,Integer>, JpaSpecificationExecutor<LagouUser> {


    @Query(value = "select * from lagou_user where email = ?1 and password = ?2 limit 1",nativeQuery = true)
    LagouUser findByEmailAndPwd(String email, String password);


}
