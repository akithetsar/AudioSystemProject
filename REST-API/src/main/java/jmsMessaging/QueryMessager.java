/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmsMessaging;

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
import javax.ws.rs.core.Response;

/**
 *
 * @author akith
 */

public class QueryMessager {
    
    
    private ConnectionFactory connFactory;
    private Queue queue;
    
    
    public QueryMessager(ConnectionFactory connFactory, Queue queue){
        this.connFactory = connFactory;
        this.queue = queue;
    }

    private static final int TIMEOUT_TIME = 5000; //5s
    
    public enum Subsystem{
        ONE,
        TWO,
        THREE
    }
    
    public enum Operation{
        CREATE,
        READ,
        UPDATE,
        DELETE
    }
    public static String[] operationStrings = {"create", "read", "update", "delete"};
    public Response sendMessage(Serializable obj, HashMap<String, String> params, Operation operation, Subsystem subsystem) {
        try{
        
            

            JMSContext context = connFactory.createContext();
            JMSProducer producer = context.createProducer();

            Queue tempQueue = context.createTemporaryQueue();
            ObjectMessage objMsg = context.createObjectMessage(obj);
            
            if(params!=null){
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    objMsg.setStringProperty(key, value);
                }
            }
            objMsg.setStringProperty("operation", operationStrings[operation.ordinal()]);
            objMsg.setJMSReplyTo(tempQueue);
            producer.send(queue, objMsg);


            return formResponse(consumeMessage(context, tempQueue));
        } catch (JMSException e) {
                
                 return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Error processing message: " + e.getMessage())
                .build();
    }
    }
   
    
  private ObjectMessage consumeMessage(JMSContext context, Queue tempQueue) throws JMSException {
        JMSConsumer consumer = context.createConsumer(tempQueue); // Use the same context for consuming
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
