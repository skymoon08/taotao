package com.mytaotao.search.mq.handler;


import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mytaotao.search.bean.Item;
import com.mytaotao.search.service.ItemService;

public class ItemHandler {
    
    @Autowired
    private HttpSolrServer httpSolrServer;
    
    @Autowired
    private ItemService itemService;
    
    private static final ObjectMapper MAPPER=new ObjectMapper();
    
    
    public void executeMessage(String msg){
        
        try {
            JsonNode jsonNode=MAPPER.readTree(msg);
            String type=jsonNode.get("type").asText();
            String itemId=jsonNode.get("itemId").asText();
            if (StringUtils.equals(type, "insert")||StringUtils.equals(type, "update")) {
                //通过后台系统的接口查询商品数据
                Item item=this.itemService.queryItemById(Long.valueOf(itemId));
                this.httpSolrServer.addBean(item);
            } else if(StringUtils.equals(type, "delete")) {
                //删除索引库中的数据
                this.httpSolrServer.deleteById(itemId);
                this.httpSolrServer.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
