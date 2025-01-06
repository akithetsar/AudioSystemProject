/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package subsystemone;

import entities.City;
import java.io.Serializable;
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
public class SubsystemOne {

    @Resource(lookup = "jms/__defaultConnectionFactory")
    private static ConnectionFactory connFactory;

    @Resource(lookup = "audioSystemQueue")
    private static Queue queue;

    @Resource(lookup = "audioSystemTopic")
    private static Topic topic;
    
    private static final int SUBSYSTEM_ID = 1;

    
    public static void main(String[] args) {
        
//        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SubsystemOnePU");
//        EntityManager em = emf.createEntityManager();
//         
//        try{
//            EntityTransaction transaction = em.getTransaction();
//            City city = new City();
//            city.setName("Subotica");
//            
//            transaction.begin();
//            em.persist(city);
//            transaction.commit();
//         }
//         finally{
//            if(em.getTransaction().isActive()) em.getTransaction().rollback();
//            emf.close();
//        }
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

            // Process the message (add your own logic here)
            Serializable responsePayload = performTask(payload, operation);

            // Send the response back to the ReplyTo queue
            Destination replyTo = message.getJMSReplyTo();
            if (replyTo != null) {
                JMSProducer producer = context.createProducer();

                ObjectMessage responseMessage = context.createObjectMessage(responsePayload);
                responseMessage.setIntProperty("status", 200); // Set response status
                producer.send(replyTo, responseMessage);

                System.out.println("Subsystem " + SUBSYSTEM_ID + " sent response.");
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
    
    private static Serializable performTask(Serializable payload, String operation){
        
        return "Processed by Subsystem " + SUBSYSTEM_ID + ": " + payload;
    }
    
}
