package cn.e3mall.freemarker;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class FreeMarkerTest {

	@Test
	public void testFreeMarker() throws Exception {
		// 1、把freemarker的jar包添加到工程中
		// 2、创建一个模板文件。
		// 3、创建一个Configuration对象，直接new
		Configuration configuration = new Configuration(Configuration.getVersion());
		// 4、设置模板所在的目录。
		configuration.setDirectoryForTemplateLoading(new File("D:/workspaces-itcast/term249/git-repo/e3-item-web/src/main/webapp/WEB-INF/ftl"));
		// 5、模板文件使用的编码格式一般就是utf-8
		configuration.setDefaultEncoding("utf-8");
		// 6、加载模板，得到一个模板对象。
		Template template = configuration.getTemplate("student.ftl");
		// 7、创建一个数据集，可以是map也可以是pojo，通常使用map
		Map data = new HashMap<>();
		data.put("hello", "hello freemarker!");
		data.put("student", new Student(1,"小明",10,"北京昌平"));
		List<Student> stuList = new ArrayList<>();
		stuList.add(new Student(1,"小明1",10,"北京昌平"));
		stuList.add(new Student(2,"小明2",11,"北京昌平"));
		stuList.add(new Student(3,"小明3",12,"北京昌平"));
		stuList.add(new Student(4,"小明4",13,"北京昌平"));
		stuList.add(new Student(5,"小明5",14,"北京昌平"));
		stuList.add(new Student(6,"小明6",15,"北京昌平"));
		data.put("stuList", stuList);
		data.put("date", new Date());
		data.put("val", "哈哈哈哈");
		// 8、创建一个Writer对象，指定输出文件的路径及文件名。
		Writer out = new FileWriter("D:/temp/term249/freemarker/student.html");
		// 9、生成文件。
		template.process(data, out);
		// 10、关闭流
		out.close();
	}
}
