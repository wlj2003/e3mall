package cn.e3mall.jedis;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.e3mall.common.jedis.JedisClient;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

public class TestJedis {

	@Test
	public void testJedis() throws Exception {
		//创建一个Jedis对象，需要服务器的ip及端口号。
		Jedis jedis = new Jedis("192.168.25.153", 6379);
		//直接使用jedis对象操作redis
		jedis.set("mytest", "abcd");
		String string = jedis.get("mytest");
		System.out.println(string);
		//关闭jedis
		jedis.close();
	}
	
	@Test
	public void testJedisPool() throws Exception {
		//创建一个JedisPool对象,需要构造参数host、port
		JedisPool jedisPool = new JedisPool("192.168.25.153", 6379);
		//从连接池中获得连接jedis对象
		Jedis jedis = jedisPool.getResource();
		//对redis进行操作
		String string = jedis.get("mytest");
		System.out.println(string);
		//每次使用完毕后关闭连接，连接池回收资源。
		jedis.close();
		//系统结束之前关闭连接池。
		jedisPool.close();
	}
	
	@Test
	public void testJedisCluster() throws Exception {
		// 1）创建一个JedisCluster对象，需要参数Set类型。Set中的元素是HostAndPort对象。
		Set<HostAndPort> nodes = new HashSet<>();
		nodes.add(new HostAndPort("192.168.25.153", 7001));
		nodes.add(new HostAndPort("192.168.25.153", 7002));
		nodes.add(new HostAndPort("192.168.25.153", 7003));
		nodes.add(new HostAndPort("192.168.25.153", 7004));
		nodes.add(new HostAndPort("192.168.25.153", 7005));
		nodes.add(new HostAndPort("192.168.25.153", 7006));
		JedisCluster jedisCluster = new JedisCluster(nodes);
		// 2）使用JedisCluster对象操作redis集群。
		jedisCluster.set("mytest", "1000");
		String string = jedisCluster.get("mytest");
		System.out.println(string);
		// 3）系统结束之前关闭JedisCluster，在系统中单例的。
		jedisCluster.close();
	}
	
	@Test
	public void testJedisClient() throws Exception {
		//初始化spring容器
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-redis.xml");
		//从容器中获得JedisClient对象。
		JedisClient jedisClient = applicationContext.getBean(JedisClient.class);
		//使用JedisClient操作redis
		jedisClient.set("a00", "100");
		String string = jedisClient.get("a00");
		System.out.println(string);
	}
}
