package com.lagou.service;

import com.lagou.pojo.Resume;

import java.util.List;

/**
 * @author loonycoder
 * @data 2020年04月17日10:32:56
 * @description
 */

public interface ResumeService {

    public List<Resume> findAll();

    public void deleteById(Long id);

    public Resume save(Resume resume);

    public Resume findById(Long id);
}
