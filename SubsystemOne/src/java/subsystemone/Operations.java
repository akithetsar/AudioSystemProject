package subsystemone;

import DTOs.UserDTO;
import entities.City;
import entities.User;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import java.io.Serializable;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.RollbackException;

public class Operations {
    private static final HashMap<String, Operation> operationTable = new HashMap<>();
    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("SubsystemOnePU");
    private static EntityManager em = emf.createEntityManager();
    
    static {
        operationTable.put("1", Operations::operation1);
        operationTable.put("2", Operations::operation2);
        operationTable.put("18", Operations::operation18);
        // Add other operations here...
    }

    public static ObjectMessage operation2(Serializable payload, JMSContext context) throws JMSException {
        System.out.println("Executing Operation 2 with payload: " + payload);
        ObjectMessage message = null;
        if(payload instanceof UserDTO){
            UserDTO dto = (UserDTO) payload;

        try{    
            // Begin transaction
            em.getTransaction().begin();

            // Fetch city by name
            City city = null;
            try {
                city = em.createNamedQuery("City.findByName", City.class)
                            .setParameter("name", dto.getCityName())
                            .getSingleResult();
            } catch (Exception e) {
                em.getTransaction().rollback();
                message = context.createObjectMessage("City with name '" + dto.getCityName() + "' not found.");
                message.setIntProperty("status", 404);
                return message;
            }

            // Create new user and set properties
            User newUser = new User();
            newUser.setName(dto.getName());
            newUser.setEmail(dto.getEmail());
            newUser.setBirthYear(dto.getBirthYear());
            newUser.setGender(dto.getGender());
            newUser.setCityId(city); // Set the City reference

            // Persist the new user
            em.persist(newUser);

            // Commit transaction
            em.getTransaction().commit();
            
        }catch (RollbackException e) {
            Throwable cause = e.getCause();
            while (cause != null) {
                if (cause instanceof SQLIntegrityConstraintViolationException) {
                    message = context.createObjectMessage("Database constraint violated.");
                    message.setIntProperty("status", 404);
                    return message;
                }
                cause = cause.getCause();
            }
            em.getTransaction().rollback();
        }
        finally{
            if(em.getTransaction().isActive()) em.getTransaction().rollback();
            message = context.createObjectMessage("New user created");
            message.setIntProperty("status", 200);
        }
        
        }else{
            message = context.createObjectMessage("Operation 2 payload is not of type UserDTO");
            message.setIntProperty("status", 500);
        }
        return message;
    }

    public static ObjectMessage operation1(Serializable payload, JMSContext context) throws JMSException {
        System.out.println("Executing Operation 1 with payload: " + payload);
        ObjectMessage message = context.createObjectMessage("Operation 1 executed successfully.");
        message.setIntProperty("status", 200);
        return message;
    }

    public static ObjectMessage operation18(Serializable payload, JMSContext context) throws JMSException {
        System.out.println("Executing Operation 18 with payload: " + payload);
        ObjectMessage message = context.createObjectMessage("Operation 18 executed successfully.");
        message.setIntProperty("status", 200);
        return message;
    }
    public static Operation getOperation(String operation) {
        return operationTable.get(operation);
    }
}
