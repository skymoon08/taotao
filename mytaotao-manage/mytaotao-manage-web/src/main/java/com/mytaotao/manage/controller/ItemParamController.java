package com.mytaotao.manage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
//import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.mytaotao.manage.pojo.ItemParam;
import com.mytaotao.manage.service.ItemParamService;

@RequestMapping("item/param")
@Controller
public class ItemParamController {
    
    @Autowired
    private ItemParamService itemParamService;
    
    /**
     * 根据类目id查询规格参数模板
     * @param itemCatId
     * @return
     */
    @RequestMapping(value="{itemCatId}",method=RequestMethod.GET)
    public ResponseEntity<ItemParam> queryByItemCatId(@PathVariable("itemCatId")Long itemCatId){
        try {
            ItemParam param=new ItemParam();
            param.setItemCatId(itemCatId);
            ItemParam itemParam = this.itemParamService.queryOne(param);
            if (null==itemParam) {
                //404资源不存在
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            //200存在
            return ResponseEntity.ok(itemParam);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //500查询出错
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
    
    /**
     * 新增模板
     * @param itemParam
     * @param itemCatId
     * @return
     */
    @RequestMapping(value="{itemCatId}",method=RequestMethod.POST)
    public ResponseEntity<Void> saveItemParam(ItemParam itemParam,@PathVariable("itemCatId")Long itemCatId){
        try {
            itemParam.setItemCatId(itemCatId);
            itemParam.setId(null);
            this.itemParamService.save(itemParam);
            //202
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
