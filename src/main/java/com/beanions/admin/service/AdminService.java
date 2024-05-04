package com.beanions.admin.service;

import com.beanions.common.dao.admin.AdminMapper;
import com.beanions.common.dto.AdminPostDTO;
import com.beanions.common.dto.PostDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminService {

    private final AdminMapper adminMapper;
    public AdminService(AdminMapper adminMapper) {
        this.adminMapper = adminMapper;
    }

    public List<AdminPostDTO> selectAllPost() {

        return adminMapper.selectAllPost();
    }

    public List<AdminPostDTO> selectPost(int code) {

        return adminMapper.selectPost(code);
    }

    @Transactional
    public void postUpdate(PostDTO post) {

        System.out.println("postUpdate service 매소드 호출...");
        System.out.println("post = " + post);
        adminMapper.postUpdate(post);
    }

    @Transactional
    public void postDelete(Integer id) {

        adminMapper.postDelete(id);
    }
}