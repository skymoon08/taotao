package com.mytaotao.web.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mytaotao.common.bean.HttpResult;
import com.mytaotao.common.service.ApiService;
import com.mytaotao.web.bean.Order;
import com.mytaotao.web.bean.User;
import com.mytaotao.web.threadlocal.UserThreadLocal;

@Service
public class OrderService {

    @Autowired
    private PropertieService propertieService;
    
    @Autowired
    private ApiService apiService;
    
    private static final ObjectMapper MAPPER=new ObjectMapper();
    
    
    /**
     * 调用订单系统的接口服务提交订单，提交成功返回订单号，提交失败返回null
     * @param order
     * @return
     */
    public String submitOrder(Order order) {
        String url=this.propertieService.MYTAOTAO_ORDER_URL+"/order/create";
        try {
            //直接通过ThreadLocal来获取User对象
            User user = UserThreadLocal.get();
            order.setUserId(user.getId());
            order.setBuyerNick(user.getUsername());
            
            HttpResult httpResult=this.apiService.doPostJson(url, MAPPER.writeValueAsString(order));
            //这里的200响应状态码
            if(httpResult.getCode()==200){
                //解析响应数据
                JsonNode jsonNode=MAPPER.readTree(httpResult.getData());
                if (jsonNode.get("status").asInt()==200) {
                    //返回订单号
                    return jsonNode.get("data").asText();
                }
            }
        }  catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 根据订单id查询订单
     * @param orderId
     * @return
     */
    public Order queryOrderById(String orderId) {
        String url=this.propertieService.MYTAOTAO_ORDER_URL+"/order/query/"+orderId;
        try {
            String jsonData = this.apiService.doGet(url);
            if (StringUtils.isEmpty(jsonData)) {
                return null;
            }
            return MAPPER.readValue(jsonData,Order.class);
        }  catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
