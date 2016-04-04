package com.mytaotao.web.mq.handler;


import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mytaotao.common.service.RedisService;
import com.mytaotao.web.service.ItemService;

public class ItemHandler {
    
    @Autowired
    private  RedisService redisService;
    
    private static final ObjectMapper MAPPER=new ObjectMapper();
    
    
    public void executeMessage(String msg){
        
        try {
            JsonNode jsonNode=MAPPER.readTree(msg);
            String key=ItemService.REDIS_KEY+jsonNode.get("itemId").asLong();
            //删除缓存的数据
            this.redisService.del(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
