package com.mytaotao.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.mytaotao.manage.pojo.ItemDesc;
import com.mytaotao.web.bean.Item;
import com.mytaotao.web.service.ItemService;

@RequestMapping("item")
@Controller
public class ItemController {
    
    
    @Autowired
    private ItemService itemService;
    
    @RequestMapping(value="{itemId}",method=RequestMethod.GET)
    public ModelAndView detail(@PathVariable("itemId")Long itemId){
        
        ModelAndView mv=new ModelAndView("item");
        //商品基本数据
        Item item =this.itemService.queryItemById(itemId);
        mv.addObject("item", item);
        
        //商品描述数据
        ItemDesc itemDesc=this.itemService.queryItemDescByItemId(itemId);
        mv.addObject("itemDesc", itemDesc);
        
        //查询商品规格参数
        String html=this.itemService.queryItemParamItemByItemId(itemId);
        mv.addObject("itemParam", html);
        return mv;
    }
}
