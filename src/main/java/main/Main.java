package main;

import util.ActiveMqUtil;

import javax.jms.*;

/**
 * @author ：Qiao Yansong
 * @date ：Created in 2021/5/26 9:39
 * description：
 */
public class Main {

    private static final String TOPIC_NAME = "topic1";
    private static final int MESSAGE_NUM = 6;
    public static void main(String[] args) {
        XAConnection connection = null;
        Session session = null;
        MessageProducer producer = null;
        try {
            connection = ActiveMqUtil.getConnection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Topic topic = session.createTopic(TOPIC_NAME);
            producer = session.createProducer(topic);

            for (int i = 1; i <= MESSAGE_NUM; i++) {
                TextMessage textMessage = session.createTextMessage("消息为" + i);
                producer.send(textMessage);
            }

            System.out.println("消息生产完毕");
        } catch (JMSException e) {
            e.printStackTrace();
        }finally {
            if(producer != null){
                try {
                    producer.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
            if(session != null){
                try {
                    session.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
            if(connection != null){
                try {
                    connection.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
