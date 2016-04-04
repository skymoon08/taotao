package com.mytaotao.manage.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.abel533.entity.Example;
import com.mytaotao.manage.mapper.ContentCategoryMapper;
import com.mytaotao.manage.pojo.ContentCategory;

@Service
public class ContentCategoryService extends BaseService<ContentCategory>{

    @Autowired
    private ContentCategoryMapper contentCategoryMapper;
    
    /**
     * 根据父节点id倒序排序查询分类列表数据
     * @param parentId
     * @return
     */
    public List<ContentCategory> queryListByParentId(Long parentId) {
        Example example=new Example(ContentCategory.class);
        example.createCriteria().andEqualTo("parentId", parentId);
        //设置排序条件
        example.setOrderByClause("updated DESC");
        return this.contentCategoryMapper.selectByExample(example);
    }
    
    /**
     * 新增子节点
     * @param contentCategory
     * @return
     */
    public ContentCategory saveContentCategroy(ContentCategory contentCategory){
        try {
            //设置初始数据值
            contentCategory.setId(null);
            contentCategory.setIsParent(false);
            contentCategory.setSortOrder(1);
            contentCategory.setStatus(1);
            this.save(contentCategory);
            //需要判断当前节点的isParent是否为true，不是，需要设置为true
            ContentCategory parent=this.queryById(contentCategory.getParentId());
            if (!parent.getIsParent()) {
                parent.setIsParent(true);
                this.update(parent);
            }
            return contentCategory;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * 重命名
     * @param contentCategory
     */
    public void rename(ContentCategory contentCategory){
        try {
           this.updateSelective(contentCategory);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 删除节点以及子节点
     * @param contentCategory
     */
    public void delete(ContentCategory contentCategory){
        //删除当前节点以及该节点下的所有节点
        try {
            List<Object> ids =new ArrayList<Object>();
            ids.add(contentCategory.getId());
            findAllSubNode(ids,contentCategory.getId());
            //批量删除
            this.deleteByIds(ContentCategory.class,"id",ids);
            //判断当前节点的父节点是否还有其他子节点，若没有，设置isParent为false
            ContentCategory param=new ContentCategory();
            param.setParentId(contentCategory.getParentId());
            List<ContentCategory> list=this.queryListByWhere(param);
            if (null==list || list.isEmpty()) {
                ContentCategory parent =new ContentCategory();
                parent.setId(contentCategory.getParentId());
                parent.setIsParent(false);
                this.updateSelective(parent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 查找到所有节点
     * @param ids
     * @param parentId
     */
    private void findAllSubNode(List<Object> ids, Long parentId) {
        ContentCategory param=new ContentCategory();
        param.setParentId(parentId);
        List<ContentCategory> list=this.queryListByWhere(param);
        for (ContentCategory contentCategory : list) {
            ids.add(contentCategory.getId());
            if (contentCategory.getIsParent()) {
                findAllSubNode(ids, contentCategory.getId());
            }
        }
    }
}
