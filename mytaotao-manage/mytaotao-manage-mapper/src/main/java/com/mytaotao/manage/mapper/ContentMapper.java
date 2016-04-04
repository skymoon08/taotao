package com.mytaotao.manage.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.github.abel533.mapper.Mapper;
import com.mytaotao.manage.pojo.Content;

public interface ContentMapper extends Mapper<Content>{

    List<Content> queryContentList(@Param("categoryId")Long categoryId);
    
}
