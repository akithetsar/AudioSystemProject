package subsystemone;

import DTOs.CityDTO;
import DTOs.UserDTO;
import entities.City;
import entities.User;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import java.io.Serializable;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import javax.jms.Message;
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
        operationTable.put("3", Operations::operation3);
        operationTable.put("18", Operations::operation18);
        
        // Add other operations here...
    }

    
     public static ObjectMessage operation1(Message msg, JMSContext context) throws JMSException {
        Serializable payload = ((ObjectMessage) msg).getObject();
        System.out.println("Executing Operation 1 with payload: " + payload);
        ObjectMessage message = null;
        if(payload instanceof CityDTO){
            CityDTO dto = (CityDTO) payload;
            try{    
                em.getTransaction().begin();
                
                City newCity = new City();
                newCity.setName(dto.getName());
              
                em.persist(newCity);
                em.getTransaction().commit();
            }
            catch (RollbackException e) {
                em.getTransaction().rollback();
                return rollbackHandler(e, context);
            }
            finally{
                if(em.getTransaction().isActive()) em.getTransaction().rollback();
                message = context.createObjectMessage("New city created");
                message.setIntProperty("status", 200);
            }
        }
        else{
            message = context.createObjectMessage("Operation 1 payload is not of type CityDTO");
            message.setIntProperty("status", 500);
        }
        return message;
    }
    
    public static ObjectMessage operation2(Message msg, JMSContext context) throws JMSException {
        Serializable payload = ((ObjectMessage) msg).getObject();
        System.out.println("Executing Operation 2 with payload: " + payload);
        ObjectMessage message = null;
        if(payload instanceof UserDTO){
            UserDTO dto = (UserDTO) payload;

            try{    
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

                em.persist(newUser);
                em.getTransaction().commit();

            }catch (RollbackException e) {
                em.getTransaction().rollback();
                return rollbackHandler(e, context);

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

    public static ObjectMessage operation3(Message msg, JMSContext context) throws JMSException{
        Serializable payload = ((ObjectMessage) msg).getObject();
        System.out.println("Executing Operation 3 with payload: " + payload);
        ObjectMessage message = null;
        
        try{
            String email = msg.getStringProperty("email");
            String cityName = msg.getStringProperty("city");
            int userId = Integer.parseInt(msg.getStringProperty("user_id"));

            em.getTransaction().begin();

            // Find the city by name
            City city = null;


            // Find the user by ID
            User user = em.find(User.class, userId);
            if (user == null) {
                em.getTransaction().rollback();
                message = context.createObjectMessage("User with ID '" + userId + "' not found.");
                message.setIntProperty("status", 404);
                return message;
            }

            // Update the user entity
            if (email != null) {
                user.setEmail(email);
            }
            if (cityName != null) {
                try {
                    city = em.createNamedQuery("City.findByName", City.class)
                            .setParameter("name", cityName)
                            .getSingleResult();         
                } catch (Exception e) {
                    em.getTransaction().rollback();
                    message = context.createObjectMessage("City with name '" + cityName + "' not found.");
                    message.setIntProperty("status", 404);
                    return message;
            }
                user.setCityId(city);
            }

            em.merge(user);
            em.getTransaction().commit();

        }
        catch (RollbackException e) {
            em.getTransaction().rollback();
            return rollbackHandler(e, context);
        }
        finally{
            if(em.getTransaction().isActive()) em.getTransaction().rollback();
            message = context.createObjectMessage("User data updated");
            message.setIntProperty("status", 200);
        }
        
        
        return message;
    }
    
    public static ObjectMessage operation18(Message msg, JMSContext context) throws JMSException {
        Serializable payload = ((ObjectMessage) msg).getObject();
        System.out.println("Executing Operation 18 with payload: " + payload);
        ObjectMessage message = context.createObjectMessage("Operation 18 executed successfully.");
        message.setIntProperty("status", 200);
        return message;
    }
    
    public static Operation getOperation(String operation) {
        return operationTable.get(operation);
    }
    
    private static ObjectMessage rollbackHandler(RollbackException e, JMSContext context) throws JMSException{
            Throwable cause = e.getCause();
            ObjectMessage message = null;
            while (cause != null) {
                if (cause instanceof SQLIntegrityConstraintViolationException) {
                    message = context.createObjectMessage("Database constraint violeted.");
                    message.setIntProperty("status", 404);
                    
                }
                cause = cause.getCause();
            }
            return message;
    }
}
