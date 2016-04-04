package com.mytaotao.search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.mytaotao.search.bean.SearchResult;
import com.mytaotao.search.service.ItemSearchService;

@Controller
public class ItemSearchController {
    
    @Autowired
    private ItemSearchService itemSearchService;

    @RequestMapping(value = "search", method = RequestMethod.GET)
    public ModelAndView search(@RequestParam("q") String keyWords,
            @RequestParam(value = "page", defaultValue = "1") Integer page) {
        ModelAndView mv = new ModelAndView("search");
        try {
            //解决中文乱码问题GET，请求解决乱码的过滤器不会生效
            keyWords=new String(keyWords.getBytes("ISO-8859-1"), "UTF-8");
            SearchResult searchResult = this.itemSearchService.search(keyWords, page);
            mv.addObject("query", keyWords);// 搜索关键字
            mv.addObject("itemList", searchResult.getList());// 商品集合数据
            mv.addObject("page", page);
            // 计算总页数
            int total = searchResult.getTotal().intValue();
            int pages = total % ItemSearchService.ROWS == 0 ? total / ItemSearchService.ROWS : total
                    / ItemSearchService.ROWS + 1;
            mv.addObject("pages", pages);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mv;
    }
}
