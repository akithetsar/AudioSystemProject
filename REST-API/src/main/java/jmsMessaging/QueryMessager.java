/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmsMessaging;

import entities.City;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Topic;
import javax.ws.rs.core.Response;

/**
 *
 * @author akith
 */

public class QueryMessager {
    
    
    private ConnectionFactory connFactory;
    private Topic topic;
    private Queue queue;
    
    public QueryMessager(ConnectionFactory connFactory, Topic topic, Queue queue){
        this.connFactory = connFactory;
        this.topic = topic;
        this.queue = queue;
    }

    private static final int TIMEOUT_TIME = 5000; //5s
    
    public enum Subsystem{
        ONE,
        TWO,
        THREE
    }
    

    public Response sendMessage(Serializable obj, HashMap<String, String> params, Subsystem subsystem) {
        try{
        
            

            JMSContext context = connFactory.createContext();
            JMSProducer producer = context.createProducer();

            ObjectMessage objMsg = context.createObjectMessage(obj);
            
            if(params!=null){
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    objMsg.setStringProperty(key, value);
                }
            }
            objMsg.setJMSReplyTo(queue);
            objMsg.setIntProperty("subsystem", subsystem.ordinal());
            producer.send(topic, objMsg);


            return formResponse(consumeMessage(context, queue));
        } catch (JMSException e) {
                
                 return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Error processing message: " + e.getMessage())
                .build();
    }
    }
   
//    public Serializable test()  {
//        
//        try{
//            
//        JMSContext context = connFactory.createContext();
//            JMSConsumer consumer = context.createConsumer(queue);
//            Message msg = consumer.receive(TIMEOUT_TIME);
//            if(msg != null){
//            System.out.println(msg.getStringProperty("operation"));
//            System.out.println(msg.getStringProperty("columns"));
//            return (((ObjectMessage)(msg)).getObject());
//            }
//            } catch (JMSException e) {   }
//        return null;
//    }
  private ObjectMessage consumeMessage(JMSContext context, Queue tempQueue) throws JMSException {
        JMSConsumer consumer = context.createConsumer(tempQueue); 
        Message response = consumer.receive(TIMEOUT_TIME);

        if (response == null) {
            throw new JMSException("No response received within the timeout period");
        }

        if (response instanceof ObjectMessage) {
            return (ObjectMessage) response;
        } else {
            throw new JMSException("Unexpected message type received");
        }
    }

   
    private Response formResponse(ObjectMessage msg) throws JMSException {
        int status = msg.getIntProperty("status");
        return Response.status(status).entity(msg.getObject()).build();
    }
}
