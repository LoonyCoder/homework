package com.lagou.dao;

import com.lagou.pojo.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author loonycoder
 * @data 2020年04月17日10:32:09
 * @description
 */

public interface ResumeDao extends JpaRepository<Resume,Long>, JpaSpecificationExecutor<Resume> {
}
