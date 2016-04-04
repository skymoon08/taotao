package com.mytaotao.search.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mytaotao.common.service.ApiService;
import com.mytaotao.search.bean.Item;

@Service
public class ItemService {

    @Autowired
    private ApiService apiService;

    @Value("${MYTAOTAO_MANAGE_URL}")
    private String MYTAOTAO_MANAGE_URL;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * 通过商品id查询商品详情
     * 
     * @param itemId
     * @return
     */
    public Item queryItemById(Long itemId) {
        try {
            String url = MYTAOTAO_MANAGE_URL + "/rest/item/" + itemId;
            String jsonData = this.apiService.doGet(url);
            if (StringUtils.isEmpty(jsonData)) {
                return null;
            }
            return MAPPER.readValue(jsonData, Item.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
}