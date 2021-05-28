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
        runWithTx();
    }

    /**
     * 不使用持久化机制的topic
     */
    private static void runWithNonPersistent(){
        Connection connection = null;
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

    /**
     * 使用持久化机制的topic
     */
    private static void runWithPersistent(){
        Connection connection = null;
        Session session = null;
        MessageProducer producer = null;
        try {
            connection = ActiveMqUtil.getConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Topic topic = session.createTopic(TOPIC_NAME);
            producer = session.createProducer(topic);
            producer.setDeliveryMode(DeliveryMode.PERSISTENT);
            connection.start();
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

    /**
     * 使用事务的topic
     */
    private static void runWithTx(){
        Connection connection = null;
        Session session = null;
        MessageProducer producer = null;
        try {
            connection = ActiveMqUtil.getConnection();
            session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
            Topic topic = session.createTopic(TOPIC_NAME);
            producer = session.createProducer(topic);
            connection.start();
            for (int i = 1; i <= MESSAGE_NUM; i++) {
                TextMessage textMessage = session.createTextMessage("消息为" + i);
                producer.send(textMessage);
            }
            System.out.println("消息生产完毕");
            session.commit();
        } catch (JMSException e) {
            e.printStackTrace();
            try {
                session.rollback();
            } catch (JMSException jmsException) {
                jmsException.printStackTrace();
            }
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
