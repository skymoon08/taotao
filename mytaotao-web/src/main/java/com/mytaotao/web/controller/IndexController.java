package com.mytaotao.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.mytaotao.web.service.IndexService;

@Controller
public class IndexController {
    
    @Autowired
    private IndexService indexService;
    
    @RequestMapping(value="index",method=RequestMethod.GET)
    public ModelAndView index(){
        
       ModelAndView mv=new ModelAndView("index");
        //大广告数据
       mv.addObject("indexAd1",this.indexService.queryIndexServiceAd1());
       //右上角广告数据
       mv.addObject("indexAd2",this.indexService.queryIndexServiceAd2());
       return mv;
        
    }
    
}
