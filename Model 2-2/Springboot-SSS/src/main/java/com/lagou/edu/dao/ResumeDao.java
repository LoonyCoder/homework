package com.lagou.edu.dao;

import com.lagou.edu.pojo.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


/**
 * @author loonycoder
 * @data 2020年05月18日13:37:46
 * @description
 */

public interface ResumeDao extends JpaRepository<Resume,Long>, JpaSpecificationExecutor<Resume> {
}
