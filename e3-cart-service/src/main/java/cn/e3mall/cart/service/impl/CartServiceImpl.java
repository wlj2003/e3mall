package cn.e3mall.cart.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;

/**
 * 购物车Service
 * <p>Title: CartServiceImpl</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p> 
 * @version 1.0
 */
@Service
public class CartServiceImpl implements CartService {

	@Autowired
	private JedisClient jedisClient;
	@Autowired
	private TbItemMapper itemMapper;
	
	@Override
	public E3Result addCartItem(Long userId, Long itemId, Integer num) {
		// 1、判断商品在购物车中是否存在
		if (jedisClient.hexists("cart_info:" + userId, itemId.toString())) {
			String json = jedisClient.hget("cart_info:" + userId, itemId.toString());
			TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
			// 2、如果商品存在商品数量相加
			tbItem.setNum(tbItem.getNum() + num);
			//写入redis
			jedisClient.hset("cart_info:" + userId, itemId.toString(), JsonUtils.objectToJson(tbItem));
			return E3Result.ok();
		}
		// 3、如果不存在根据商品id查询商品信息
		TbItem tbItem = itemMapper.selectByPrimaryKey(itemId);
		// 4、设置商品数量
		tbItem.setNum(num);
		// 5、图片取第一张
		String image = tbItem.getImage();
		if (StringUtils.isNotBlank(image)) {
			tbItem.setImage(tbItem.getImage().split(",")[0]);
		}
		// 6、把商品添加到购物车列表
		jedisClient.hset("cart_info:" + userId, itemId.toString(), JsonUtils.objectToJson(tbItem));
		// 7、返回OK
		return E3Result.ok();
	}

	@Override
	public E3Result mergeCartItem(long userId, List<TbItem> itemList) {
		for (TbItem tbItem : itemList) {
			addCartItem(userId, tbItem.getId(), tbItem.getNum());
		}
		return E3Result.ok();
	}

	@Override
	public List<TbItem> getCartList(long userId) {
		List<String> jsonList = jedisClient.hvals("cart_info:" + userId);
		List<TbItem> cartList = new ArrayList<>();
		for (String string : jsonList) {
			TbItem tbItem = JsonUtils.jsonToPojo(string, TbItem.class);
			cartList.add(tbItem);
		}
		return cartList;
	}

	@Override
	public E3Result updateCartItem(long userId, long itemId, int num) {
		// 1、取商品信息
		String json = jedisClient.hget("cart_info:" + userId, itemId + "");
		// 2、把json转换成java对象
		TbItem item = JsonUtils.jsonToPojo(json, TbItem.class);
		// 3、更新数量
		item.setNum(num);
		// 4、写回redis
		jedisClient.hset("cart_info:" + userId, itemId + "", JsonUtils.objectToJson(item));
		// 5、返回成功
		return E3Result.ok();
	}

	@Override
	public E3Result deleteCartItem(long userId, long itemId) {
		jedisClient.hdel("cart_info:" + userId, itemId + "");
		return E3Result.ok();
	}

}
