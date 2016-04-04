package com.mytaotao.manage.pojo;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name="tb_item_desc")
public class ItemDesc extends BasePojo{
    
    @Override
    public String toString() {
        return "ItemDesc [itemId=" + itemId + ", itemDesc=" + itemDesc + "]";
    }

    @Id//对应tb_item中的id,将itemId认为是描述的id
    private Long itemId;
    
    private String itemDesc;

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }
}
