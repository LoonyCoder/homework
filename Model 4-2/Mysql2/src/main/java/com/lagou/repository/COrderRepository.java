package com.lagou.repository;

import com.lagou.entity.COrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface COrderRepository extends JpaRepository<COrder,Long> {

    @Query(nativeQuery = true, value = "select * from c_order where user_id=:user_id")
    public COrder findByUId(@Param("user_id") int id);
}
