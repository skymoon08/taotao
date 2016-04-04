package com.mytaotao.manage.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.mytaotao.common.bean.EasyUIResult;
import com.mytaotao.manage.pojo.Item;
import com.mytaotao.manage.service.ItemService;

@RequestMapping("item")
@Controller
public class ItemController {
    
    private static final Logger LOGGER=LoggerFactory.getLogger(ItemController.class);
    
    @Autowired
    private ItemService itemService;

    /**
     * 新增商品
     * 
     * @param item
     * @param desc
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> saveItem(Item item, @RequestParam("desc") String desc,
            @RequestParam("itemParams")String itemParams ) {
        // 设置初始参数
        try {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("新增商品！item={},desc={}", item,desc);
            }
            // 保存到数据库
            
            this.itemService.saveItem(item, desc,itemParams);
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("新增商品成功！id={}",item.getId());
            }
            // 201
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            LOGGER.error("新增商品失败！item="+item,e);
        }
        // 500
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
    
    /**
     * 更新商品
     * @param item
     * @param desc
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Void> updateItem(Item item, @RequestParam("desc") String desc,
           @RequestParam("itemParams")String itemParams,@RequestParam("itemParamId")Long itemParamId) {
        // 设置初始参数
        try {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("修改商品！item={},desc={}", item,desc);
            }
            // 保存到数据库
            
            this.itemService.updateItem(item, desc,itemParams,itemParamId);
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("修改商品成功！id={}",item.getId());
            }
            // 204
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            LOGGER.error("修改商品失败！item="+item,e);
        }
        // 500
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    
    /**
     * 分页查询商品列表，按照更新时间倒序排序
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<EasyUIResult> queryItemList(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "30") Integer rows) {
        try {
            return ResponseEntity.ok(this.itemService.queryItemlist(page, rows));
        } catch (Exception e) {
            e.printStackTrace();
        }
        //500
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
    
    
    /**
     * 根据商品id查询商品数据
     * @param itemId
     * @return
     */
    @RequestMapping(value="{itemId}",method=RequestMethod.GET)
    public ResponseEntity<Item> queryItemById(@PathVariable("itemId")Long itemId){
        try {
            Item item=this.itemService.queryById(itemId);
            if (null==item) {
                //404
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
                }
            //200
            return ResponseEntity.ok(item);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //500
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);    }
}
