package cn.e3mall.pagehelper;

import java.util.List;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemExample;
import cn.e3mall.pojo.TbItemExample.Criteria;

public class TestPageHelper {

	@Test
	public void testPageHelper() throws Exception {
		// 3）设置分页信息
		PageHelper.startPage(1, 10);
		// 4）执行查询
		//初始化spring容器
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-dao.xml");
		//从spring容器中获得mapepr代理对象
		TbItemMapper itemMapper = applicationContext.getBean(TbItemMapper.class);
		//执行查询
		TbItemExample example = new TbItemExample();
		List<TbItem> list = itemMapper.selectByExample(example);
		// 5）取分页结果
		PageInfo<TbItem> pageInfo = new PageInfo<>(list);
		System.out.println("查询结果总记录数：" + pageInfo.getTotal());
		System.out.println("查询结果总页数：" + pageInfo.getPages());
		System.out.println(pageInfo.getPageNum());
		System.out.println("list size:" + list.size());
	}
}