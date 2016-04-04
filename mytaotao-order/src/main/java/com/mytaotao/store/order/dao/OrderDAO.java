package com.mytaotao.store.order.dao;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.mytaotao.store.order.bean.Where;
import com.mytaotao.store.order.mapper.OrderMapper;
import com.mytaotao.store.order.pojo.Order;
import com.mytaotao.store.order.pojo.PageResult;
import com.mytaotao.store.order.pojo.ResultMsg;

/**
 * mysql版本的实现
 * 
 */
public class OrderDAO implements IOrder{
	
	@Autowired
	private OrderMapper orderMapper;

	@Override
	public void createOrder(Order order) {
		this.orderMapper.save(order);
	}

	@Override
	public Order queryOrderById(String orderId) {
		return this.orderMapper.queryByID(orderId);
	}

	@Override
	public PageResult<Order> queryOrderByUserNameAndPage(String buyerNick, Integer page, Integer count) {
	        //PageBounds分页插件
		PageBounds bounds = new PageBounds();
		bounds.setContainsTotalCount(true);//是否查询数据总条数
		bounds.setLimit(count);//每页查询数据总条数
		bounds.setPage(page);//页数
		
		//排序条件
		bounds.setOrders(com.github.miemiedev.mybatis.paginator.domain.Order.formString("create_time.desc"));
		//将分页对象传递到Mapper中
		PageList<Order> list = this.orderMapper.queryListByWhere(bounds, Where.build("buyer_nick", buyerNick));
		return new PageResult<Order>(list.getPaginator().getTotalCount(), list);
	}

	/*
	 * 设置更新时间
	 * 
	 * (non-Javadoc)
	 * @see com.mytaotao.store.order.dao.IOrder#changeOrderStatus(com.mytaotao.store.order.pojo.Order)
	 */
	@Override
	public ResultMsg changeOrderStatus(Order order) {
		try {
			order.setUpdateTime(new Date());
			this.orderMapper.update(order);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultMsg("500", "更新订单出错!");
		}
		return new ResultMsg("200", "更新成功!");
	}

}
