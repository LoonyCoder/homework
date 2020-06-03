package com.lagou.edu.service.impl;

import com.lagou.edu.dao.ResumeDao;
import com.lagou.edu.pojo.Resume;
import com.lagou.edu.service.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author loonycoder
 * @data 2020年05月18日13:42:07
 * @description
 */
@Service
public class ResumeServiceImpl implements ResumeService {

    @Autowired
    private ResumeDao resumeDao;

    @Override
    public List<Resume> findAll() {
        return resumeDao.findAll();
    }

    @Override
    public void deleteById(Long id) {
        resumeDao.deleteById(id);
    }

    @Override
    public Resume save(Resume resume) {
        return resumeDao.save(resume);
    }

    @Override
    public Resume findById(Long id) {
        return resumeDao.findById(id).get();
    }
}
