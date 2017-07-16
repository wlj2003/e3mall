package cn.e3mall.item.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * freemarker整合springmvc测试
 * <p>Title: HtmlGenController</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p> 
 * @version 1.0
 */
@Controller
public class HtmlGenController {

	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;
	
	@RequestMapping("/gen")
	@ResponseBody
	public String genHtml() throws Exception {
		//使用FreeMarkerConfigurer对象获得Configuration对象
		Configuration configuration = freeMarkerConfigurer.getConfiguration();
		//加载模板文件
		Template template = configuration.getTemplate("hello.ftl");
		//创建数据集
		Map data = new HashMap<>();
		data.put("hello", "freemarker springmvc test");
		//指定文件输出的路径及文件名
		Writer out = new FileWriter(new File("D:/temp/term249/freemarker/hello.html"));
		//生成文件
		template.process(data, out);
		//关闭流
		out.close();
		//返回ok
		return "OK";
	}
}
