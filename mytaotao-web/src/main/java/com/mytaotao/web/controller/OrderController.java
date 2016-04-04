package com.mytaotao.web.controller;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.mytaotao.web.bean.Item;
import com.mytaotao.web.bean.Order;
import com.mytaotao.web.service.ItemService;
import com.mytaotao.web.service.OrderService;
import com.mytaotao.web.service.UserService;

@RequestMapping("order")
@Controller
public class OrderController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    /**
     * 
     * @param itemId
     * @return
     */
    @RequestMapping(value = "{itemId}", method = RequestMethod.GET)
    public ModelAndView toOrder(@PathVariable("itemId") Long itemId) {
        ModelAndView mv = new ModelAndView("order");
        // 查询商品数据
        Item item = this.itemService.queryItemById(itemId);
        mv.addObject("item", item);
        return mv;
    }

    /**
     * 提交订单
     * 
     * @param order
     * @return
     */
    @RequestMapping(value = "submit", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> submitOrder(Order order) {
        Map<String, Object> result = new HashMap<String, Object>();
        // 查询用户信息
        String orderId = this.orderService.submitOrder(order);
        if (null == orderId) {
            result.put("status", 400);
        } else {
            result.put("status", 200);
            result.put("data", orderId);
        }
        return result;
    }
    
    @RequestMapping(value="success",method=RequestMethod.GET)
    public ModelAndView success(@RequestParam("id")String orderId){
        ModelAndView mv=new ModelAndView("success");
        Order order=this.orderService.queryOrderById(orderId);
        mv.addObject("order", order);
        //当前时间往后推两天
        mv.addObject("date", new DateTime().plusDays(2).toString("MM月dd日"));
        return mv;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
