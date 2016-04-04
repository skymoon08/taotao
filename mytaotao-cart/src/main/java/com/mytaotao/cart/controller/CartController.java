package com.mytaotao.cart.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@RequestMapping("cart")
@Controller
public class CartController {
    
    @RequestMapping(value="{itemId}",method=RequestMethod.GET)
    public ModelAndView addItemCart(@PathVariable("itemId")Long itemId){
        ModelAndView mv=new ModelAndView("cart");
        //加入购物车后去购物车列表页面
        
        return mv;
    }
    
}
