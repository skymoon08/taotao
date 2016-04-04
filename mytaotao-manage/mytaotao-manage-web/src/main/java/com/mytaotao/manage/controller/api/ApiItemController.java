package com.mytaotao.manage.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mytaotao.common.bean.ItemCatResult;
import com.mytaotao.manage.service.ItemCatService;

@RequestMapping("api/item/cat")
@Controller
public class ApiItemController {
    
    @Autowired
    private ItemCatService itemCatService;
    
//    private static final ObjectMapper MAPPER=new ObjectMapper();
    @RequestMapping(method=RequestMethod.GET)
    @ResponseBody
    public ItemCatResult queryItemCatAll(){
        return this.itemCatService.queryAllToTree();
    }
  /*  @RequestMapping(method=RequestMethod.GET)
    @ResponseBody
    public String queryItemCatAll(@RequestParam(value="callback",required=false)String callback){
        try {
            ItemCatResult itemCatResult=this.itemCatService.queryAllToTree();
            String jsonData =MAPPER.writeValueAsString(itemCatResult);
            if (StringUtils.isNotEmpty(callback)) {
                return callback +"("+jsonData+")";
            }
            else {
                return jsonData;
            }
        } catch (Exception e) {
            
          【 
            e.printStackTrace();
            
        }
        return null;
    }*/
}
