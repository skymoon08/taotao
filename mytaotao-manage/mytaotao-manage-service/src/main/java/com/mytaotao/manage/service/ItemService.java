package com.mytaotao.manage.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.abel533.entity.Example;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mytaotao.common.bean.EasyUIResult;
import com.mytaotao.common.service.ApiService;
import com.mytaotao.manage.mapper.ItemMapper;
import com.mytaotao.manage.pojo.Item;
import com.mytaotao.manage.pojo.ItemDesc;
import com.mytaotao.manage.pojo.ItemParamItem;
@Service
public class ItemService extends BaseService<Item> {
    
    @Autowired
    private ItemMapper itemMapper;
    
    @Autowired
    private ItemDescService itemDescService;
    
    @Autowired
    private ItemParamItemService itemParamItemService;
    
    @Autowired
    private ApiService apiService;
    
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    private  static final ObjectMapper MAPPER=new ObjectMapper();
    
    public void saveItem(Item item,String desc,String itemParams){
        item.setStatus(1);
        item.setId(null);
        super.save(item);
        //保存商品描述数据
        ItemDesc itemDesc=new ItemDesc();
        itemDesc.setItemDesc(desc);
        itemDesc.setItemId(item.getId());
        this.itemDescService.save(itemDesc);
        
        //保存规格参数数据
        ItemParamItem itemParamItem=new ItemParamItem();
        itemParamItem.setItemId(item.getId());
        itemParamItem.setParamData(itemParams);//设置具体的json字符串
        this.itemParamItemService.save(itemParamItem);
        
        //发送消息通知其他系统
        sendMsg(item.getId(), "insert");
    }
    
    /**
     * 对查询功能的按更新时间排序（最近更新排在最上）
     * @param page
     * @param rows
     * @return
     */
    public EasyUIResult queryItemlist(Integer page, Integer rows) {
        PageHelper.startPage(page,rows);
        Example example =new Example(Item.class);
        example.setOrderByClause("updated DESC");
        List<Item> items=this.itemMapper.selectByExample(example);
        PageInfo<Item> pageInfo=new PageInfo<Item>(items);
        return new EasyUIResult(pageInfo.getTotal(),pageInfo.getList());
    }

    /**
     * 修改商品数据
     * @param item
     * @param desc
     */
    public void updateItem(Item item, String desc,String itemParams,Long itemParamId) {
        //强制设置不能修改的字段为空
        item.setStatus(null);
        //修改数据
        super.updateSelective(item);
        
        ItemDesc itemDesc=new ItemDesc();
        itemDesc.setItemId(item.getId());
        itemDesc.setItemDesc(desc);
        this.itemDescService.updateSelective(itemDesc);
        
        //更新规格参数数据
        ItemParamItem itemParamItem=new ItemParamItem();
        itemParamItem.setId(itemParamId);
        itemParamItem.setParamData(itemParams);
        this.itemParamItemService.updateSelective(itemParamItem);
        
    /*    
        try {
            //通知其他系统商品已经更新
            String url="http://www.mytaotao.com/item/cache/"+item.getId()+"html";
            this.apiService.doPost(url);
        }  catch (Exception e) {
            e.printStackTrace();
        }*/
        //发送消息通知其他系统
        sendMsg(item.getId(),"update");
       
    }
    
    private void sendMsg(Long itemId,String type){
        try {
            //消息的内容：itemId , type , date
            Map<String, Object> msg=new HashMap<String, Object>();
            msg.put("type", type);
            msg.put("itemId",itemId);
            msg.put("date", System.currentTimeMillis());
            this.rabbitTemplate.convertAndSend("item."+type, MAPPER.writeValueAsString(msg));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
        
}
