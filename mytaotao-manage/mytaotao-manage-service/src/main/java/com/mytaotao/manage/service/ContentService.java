package com.mytaotao.manage.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mytaotao.common.bean.EasyUIResult;
import com.mytaotao.manage.mapper.ContentMapper;
import com.mytaotao.manage.pojo.Content;

@Service
public class ContentService extends BaseService<Content> {

    @Autowired
    private ContentMapper contentMapper;
    
    public EasyUIResult queryContentList(Long categoryId, Integer page, Integer rows) {
        PageHelper.startPage(page,rows);
        List<Content> contents=this.contentMapper.queryContentList(categoryId);
        PageInfo<Content> pageInfo=new PageInfo<Content>(contents);
        return new EasyUIResult(pageInfo.getTotal(),pageInfo.getList());
    }
}
