package cn.e3mall.activemq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.junit.Test;

public class TestActiveMq {

	// p2p形式的消息发送者
	@Test
	public void messageProducerQueue() throws Exception {
		// 1、和Activemq服务器建立连接，创建一个ConnectionFactory对象。
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.168:61616");
		// 2、使用ConnectionFactory对象创建Connection对象
		Connection connection = connectionFactory.createConnection();
		// 3、开启连接start方法。
		connection.start();
		// 4、创建一个Session对象
		// 两个参数：1：是否开启事务，一般不开启事务。
		// 2：如果第一个参数为true第二个参数无意义。如果是false，消息的应答模式，手动应答或者是自动应答。通常可以使用自动应答
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		// 5、创建一个消息的Destination，两种形式queue、topic，当前应该使用queue
		Queue queue = session.createQueue("test-queue");
		// 6、创建一个消息的生产者对象Producer对象。
		MessageProducer producer = session.createProducer(queue);
		// 7、创建一个Message对象，可以创建一个TextMessage
		/*
		 * TextMessage textMessage = new ActiveMQTextMessage();
		 * textMessage.setText("hello activemq");
		 */
		TextMessage textMessage = session.createTextMessage("hello activemq");
		// 8、发送消息
		producer.send(textMessage);
		// 9、关闭资源
		producer.close();
		session.close();
		connection.close();
	}

	// 接收队列消息
	@Test
	public void messageConsumerQueue() throws Exception {
		// 创建一个ConnectionFactory连接连接
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.168:61616");
		// 创建Connection对象
		Connection connection = connectionFactory.createConnection();
		// 开启连接
		connection.start();
		// 创建一个Session对象
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		// 创建一个Destination对象
		Queue queue = session.createQueue("test-queue");
		// 使用Session 创建一个Consumer对象
		MessageConsumer consumer = session.createConsumer(queue);
		// 设置监听对象MessageListener对象
		consumer.setMessageListener(new MessageListener() {

			// 接收消息
			@Override
			public void onMessage(Message message) {
				TextMessage textMessage = (TextMessage) message;
				String text = "";
				try {
					text = textMessage.getText();
				} catch (JMSException e) {
					e.printStackTrace();
				}
				// 打印消息
				System.out.println(text);
			}
		});
		// 等待接收消息
		System.out.println("消费者已经启动成功。。。");
		System.in.read();
		// 关闭资源
		consumer.close();
		session.close();
		connection.close();
	}

	@Test
	public void sendTopicMessage() throws Exception {
		// 1、和Activemq服务器建立连接，创建一个ConnectionFactory对象。
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.168:61616");
		// 2、使用ConnectionFactory对象创建Connection对象
		Connection connection = connectionFactory.createConnection();
		// 3、开启连接start方法。
		connection.start();
		// 4、创建一个Session对象
		// 两个参数：1：是否开启事务，一般不开启事务。
		// 2：如果第一个参数为true第二个参数无意义。如果是false，消息的应答模式，手动应答或者是自动应答。通常可以使用自动应答
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		// 5、创建一个消息的Destination，两种形式queue、topic，当前应该使用topic
		Topic topic = session.createTopic("test-topic");
		// 6、创建一个消息的生产者对象Producer对象。
		MessageProducer producer = session.createProducer(topic);
		// 7、创建一个Message对象，可以创建一个TextMessage
		/*
		 * TextMessage textMessage = new ActiveMQTextMessage();
		 * textMessage.setText("hello activemq");
		 */
		TextMessage textMessage = session.createTextMessage("hello topic activemq2");
		// 8、发送消息
		producer.send(textMessage);
		// 9、关闭资源
		producer.close();
		session.close();
		connection.close();
	}
	
	// 接收队列消息
	@Test
	public void messageConsumerTopic() throws Exception {
		// 创建一个ConnectionFactory连接连接
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.168:61616");
		// 创建Connection对象
		Connection connection = connectionFactory.createConnection();
		// 开启连接
		connection.start();
		// 创建一个Session对象
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		// 创建一个Destination对象
		Topic topic = session.createTopic("test-topic");
		// 使用Session 创建一个Consumer对象
		MessageConsumer consumer = session.createConsumer(topic);
		// 设置监听对象MessageListener对象
		consumer.setMessageListener(new MessageListener() {

			// 接收消息
			@Override
			public void onMessage(Message message) {
				TextMessage textMessage = (TextMessage) message;
				String text = "";
				try {
					text = textMessage.getText();
				} catch (JMSException e) {
					e.printStackTrace();
				}
				// 打印消息
				System.out.println(text);
			}
		});
		// 等待接收消息
		System.out.println("消费者3已经启动成功。。。");
		System.in.read();
		// 关闭资源
		consumer.close();
		session.close();
		connection.close();
	}
}
