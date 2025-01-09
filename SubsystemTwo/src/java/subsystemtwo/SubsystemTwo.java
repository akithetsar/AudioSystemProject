/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package subsystemtwo;

import entities.City;
import java.io.Serializable;
import java.util.function.BiConsumer;
import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
/**
 *
 * @author akith
 */
public class SubsystemTwo {

    @Resource(lookup = "jms/__defaultConnectionFactory")
    private static ConnectionFactory connFactory;

    @Resource(lookup = "audioSystemQueue")
    private static Queue queue;

    @Resource(lookup = "audioSystemTopic")
    private static Topic topic;
    
    private static final int SUBSYSTEM_ID = 2;

    
    public static void main(String[] args) {
        
         try (JMSContext context = connFactory.createContext()) {
            // Filter messages for this subsystem using a MessageSelector
            String messageSelector = "subsystem = " + SUBSYSTEM_ID;
            JMSConsumer consumer = context.createConsumer(topic, messageSelector);

            System.out.println("Subsystem " + SUBSYSTEM_ID + " is listening...");
            while (true) {
                Message message = consumer.receive();

                if (message != null) {
                    processMessage(message, context);
                }
            }
        }    
    
    }
    
    private static void processMessage(Message message, JMSContext context){
        System.out.println("Subsystem "  + SUBSYSTEM_ID + " received message");
        try {
            // Extract data from the incoming message
            Serializable payload = ((ObjectMessage) message).getObject();
            String operation = message.getStringProperty("operation");

            System.out.println("Subsystem " + SUBSYSTEM_ID + " received operation: " + operation);
            System.out.println("Payload: " + payload);

                    
            // Send the response back to the ReplyTo queue
            Destination replyTo = message.getJMSReplyTo();
            if (replyTo != null) {
                JMSProducer producer = context.createProducer();
                Message responseMessage = performTask(message, operation, context);

                producer.send(replyTo, responseMessage);

                System.out.println("Subsystem " + SUBSYSTEM_ID + " sent response.");
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
    
    private static Message performTask(Message msg, String operation, JMSContext context) throws JMSException {
        Operation operationHandler = Operations.getOperation(operation);
       
        if (operationHandler != null) {
            return operationHandler.execute(msg, context);
        } else {
            System.out.println("no op");
            ObjectMessage responseMsg = context.createObjectMessage("The operation " + operation + " is not implemented on subsystem " + SUBSYSTEM_ID);
            responseMsg.setIntProperty("status", 400);
            return responseMsg;
    }
}

    
    
}
