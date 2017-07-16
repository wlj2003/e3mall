package cn.e3mall.item.listener;

import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import cn.e3mall.item.pojo.Item;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.service.ItemService;
import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 监听商品添加消息，生成静态页面
 * <p>Title: HtmlGenMessageListener</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p> 
 * @version 1.0
 */
public class HtmlGenMessageListener implements MessageListener {
	
	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;
	@Autowired
	private ItemService itemService;
	@Value("${item.html.path}")
	private String htmlGenPath;

	@Override
	public void onMessage(Message message) {
		try {
			// 1、接收到消息，从消息中取商品id
			TextMessage textMessage = (TextMessage) message;
			String text = textMessage.getText();
			Long itemId = new Long(text);
			// 2、需要一个商品详情页面的模板文件
			Configuration configuration = freeMarkerConfigurer.getConfiguration();
			Template template = configuration.getTemplate("item.ftl");
			// 3、需要生成静态页面需要的数据，根据商品id查询数据。
			Thread.sleep(1000);
			TbItem tbItem = itemService.getItemById(itemId);
			Item item = new Item(tbItem);
			TbItemDesc itemDesc = itemService.getItemDescById(itemId);
			// 4、创建一个数据集，把模板需要的数据封装。
			Map data = new HashMap<>();
			data.put("item", item);
			data.put("itemDesc", itemDesc);
			// 5、指定文件的输出目录及文件名。
			// 文件名可以使用：商品id+.html
			Writer out = new FileWriter(htmlGenPath + itemId + ".html");
			// 6、输出文件
			template.process(data, out);
			// 7、关闭流
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
