package com.lagou.edu.service;

import com.lagou.edu.pojo.Resume;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author loonycoder
 * @data 2020年05月18日13:38:12
 * @description
 */
@Service
public interface ResumeService {

    public List<Resume> findAll();

    public void deleteById(Long id);

    public Resume save(Resume resume);

    public Resume findById(Long id);
}
