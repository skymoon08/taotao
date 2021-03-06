package com.mytaotao.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.mytaotao.common.service.RedisService;
import com.mytaotao.web.service.ItemService;

@RequestMapping("item/cache")
@Controller
public class ItemCacheController {
    
    @Autowired
    private RedisService redisService;
    
    @RequestMapping(value="{itemId}",method=RequestMethod.POST)
    public ResponseEntity<Void> deleteCache(@PathVariable("itemId")Long itemId){
        String key=ItemService.REDIS_KEY+itemId;
        this.redisService.del(key);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
