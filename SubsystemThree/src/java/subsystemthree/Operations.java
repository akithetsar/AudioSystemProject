package subsystemthree;

import DTOs.CityDTO;
import DTOs.FavoritesDTO;
import DTOs.ListeningDTO;
import DTOs.PackageDTO;
import DTOs.RatingDTO;
import DTOs.SubscriptionDTO;
import DTOs.UserDTO;
import entities.Audiotrack;
import entities.City;
import entities.Favorites;
import entities.Listening;
import entities.User;
import entities.Package;
import entities.Rating;
import entities.Subscription;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.jms.Message;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.RollbackException;

public class Operations {
    private static final HashMap<String, Operation> operationTable = new HashMap<>();
    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("SubsystemThreePU");
    private static EntityManager em = emf.createEntityManager();
    
    static {
        operationTable.put("9", Operations::operation9);
        operationTable.put("10", Operations::operation10);
        operationTable.put("11", Operations::operation11);
        operationTable.put("12", Operations::operation12);
        operationTable.put("13", Operations::operation13);
        operationTable.put("14", Operations::operation14);


        // Add other operations here...
    }

    
    public static ObjectMessage operation9(Message msg, JMSContext context) throws JMSException {
        Serializable payload = ((ObjectMessage) msg).getObject();
        System.out.println("Executing Operation 9 with payload: " + payload);
        ObjectMessage message = null;
        if(payload instanceof PackageDTO){
            PackageDTO dto = (PackageDTO) payload;
            try{    
                em.getTransaction().begin();
                
                Package newPackage = new Package();
                newPackage.setMonthlyPrice(dto.getMonthlyPrice());
                newPackage.setName(dto.getName());
                
                em.persist(newPackage);
                em.getTransaction().commit();
            }
            catch (RollbackException e) {
                em.getTransaction().rollback();
                return rollbackHandler(e, context);
            }
            finally{
                if(em.getTransaction().isActive()) em.getTransaction().rollback();
                message = context.createObjectMessage("New package created");
                message.setIntProperty("status", 200);
            }
        }
        else{
            message = context.createObjectMessage("Operation 9 payload is not of type PackageDTO");
            message.setIntProperty("status", 500);
        }
        return message;
    }
    
    
    public static ObjectMessage operation10(Message msg, JMSContext context) throws JMSException{
        Serializable payload = ((ObjectMessage) msg).getObject();
        System.out.println("Executing Operation 10 with payload: " + payload);
        ObjectMessage message = null;
        
        try{
            String price = msg.getStringProperty("price");
            int packageId = Integer.parseInt(msg.getStringProperty("package_id"));

            em.getTransaction().begin();

            // Find the city by name
            


            // Find the user by ID
            Package pack = em.find(Package.class, packageId);
            if (pack == null) {
                em.getTransaction().rollback();
                message = context.createObjectMessage("Package with ID '" + packageId + "' not found.");
                message.setIntProperty("status", 404);
                return message;
            }

            // Update the user entity
            if (price != null) {
                pack.setMonthlyPrice(new BigDecimal(price));
            }
            

            em.merge(pack);
            em.getTransaction().commit();

        }
        catch (RollbackException e) {
            em.getTransaction().rollback();
            return rollbackHandler(e, context);
        }
        finally{
            if(em.getTransaction().isActive()) em.getTransaction().rollback();
            message = context.createObjectMessage("Package data updated");
            message.setIntProperty("status", 200);
        }
        
        
        return message;
    }
    
    public static ObjectMessage operation11(Message msg, JMSContext context) throws JMSException{
        Serializable payload = ((ObjectMessage) msg).getObject();
        System.out.println("Executing Operation 11 with payload: " + payload);
        ObjectMessage message = null;
        if(payload instanceof SubscriptionDTO){
            SubscriptionDTO dto = (SubscriptionDTO) payload;
            try{
                int userId = Integer.parseInt(msg.getStringProperty("user_id"));
                int packageId = Integer.parseInt(msg.getStringProperty("package_id"));

                em.getTransaction().begin();
                User user = em.find(User.class, userId);
                if (user == null) {
                    em.getTransaction().rollback();
                    message = context.createObjectMessage("User with ID '" + userId + "' not found.");
                    message.setIntProperty("status", 404);
                    return message;
                }
                Package pack = em.find(Package.class, packageId);
                if (pack == null) {
                    em.getTransaction().rollback();
                    message = context.createObjectMessage("Package with ID '" + packageId + "' not found.");
                    message.setIntProperty("status", 404);
                    return message;
                }
                Subscription sub = new Subscription();
                sub.setPricePaid(dto.getPricePaid());
                sub.setStartDate(dto.getStartDateAsDate());
                sub.setPackageId(pack);
                sub.setUserId(user);
                
                em.persist(sub);
                em.getTransaction().commit();

            }
            catch (RollbackException e) {
                em.getTransaction().rollback();
                return rollbackHandler(e, context);
            }
            finally{
                if(em.getTransaction().isActive()) em.getTransaction().rollback();
                message = context.createObjectMessage("New subscription added");
                message.setIntProperty("status", 200);
            }
        }
         else{
            message = context.createObjectMessage("Operation 11 payload is not of type SubcriptionDTO");
            message.setIntProperty("status", 500);
        }
        return message;
    }
    
    public static ObjectMessage operation12(Message msg, JMSContext context) throws JMSException{
        Serializable payload = ((ObjectMessage) msg).getObject();
        System.out.println("Executing Operation 11 with payload: " + payload);
        ObjectMessage message = null;
        if(payload instanceof ListeningDTO){
            ListeningDTO dto = (ListeningDTO) payload;
            try{
                int userId = Integer.parseInt(msg.getStringProperty("user_id"));
                int trackId = Integer.parseInt(msg.getStringProperty("track_id"));

                em.getTransaction().begin();
                User user = em.find(User.class, userId);
                if (user == null) {
                    em.getTransaction().rollback();
                    message = context.createObjectMessage("User with ID '" + userId + "' not found.");
                    message.setIntProperty("status", 404);
                    return message;
                }
                Audiotrack track = em.find(Audiotrack.class, trackId);
                if (track == null) {
                    em.getTransaction().rollback();
                    message = context.createObjectMessage("Audiotrack with ID '" + trackId + "' not found.");
                    message.setIntProperty("status", 404);
                    return message;
                }
                Listening listening = new Listening();
                listening.setDuration(dto.getDuration());
                listening.setStartSecond(listening.getStartSecond());
                listening.setStartTime(dto.getStartTimeDate());
                listening.setAudioId(track);
                listening.setUserId(user);
                
                em.persist(listening);
                em.getTransaction().commit();

            }
            catch (RollbackException e) {
                em.getTransaction().rollback();
                return rollbackHandler(e, context);
            }
            finally{
                if(em.getTransaction().isActive()) em.getTransaction().rollback();
                message = context.createObjectMessage("New listening added");
                message.setIntProperty("status", 200);
            }
        }
         else{
            message = context.createObjectMessage("Operation 12 payload is not of type ListeningDTO");
            message.setIntProperty("status", 500);
        }
        return message;
    }
    
    
    public static ObjectMessage operation13(Message msg, JMSContext context) throws JMSException{
        Serializable payload = ((ObjectMessage) msg).getObject();
        System.out.println("Executing Operation 13 with payload: " + payload);
        ObjectMessage message = null;
        
        try{
            int userId = Integer.parseInt(msg.getStringProperty("user_id"));
            int trackId = Integer.parseInt(msg.getStringProperty("track_id"));

            em.getTransaction().begin();
            User user = em.find(User.class, userId);
            if (user == null) {
                em.getTransaction().rollback();
                message = context.createObjectMessage("User with ID '" + userId + "' not found.");
                message.setIntProperty("status", 404);
                return message;
            }
            Audiotrack track = em.find(Audiotrack.class, trackId);
            if (track == null) {
                em.getTransaction().rollback();
                message = context.createObjectMessage("Audiotrack with ID '" + trackId + "' not found.");
                message.setIntProperty("status", 404);
                return message;
            }
            Favorites favorite = new Favorites();
            favorite.setAudioId(track);
            favorite.setUserId(user);

            em.persist(favorite);
            em.getTransaction().commit();

        }
        catch (RollbackException e) {
            em.getTransaction().rollback();
            return rollbackHandler(e, context);
        }
        finally{
            if(em.getTransaction().isActive()) em.getTransaction().rollback();
            message = context.createObjectMessage("New favorite added");
            message.setIntProperty("status", 200);
        }
       
        return message;
    }
    
    
    public static ObjectMessage operation14(Message msg, JMSContext context) throws JMSException{
        Serializable payload = ((ObjectMessage) msg).getObject();
        System.out.println("Executing Operation 14 with payload: " + payload);
        ObjectMessage message = null;
        if(payload instanceof RatingDTO){
            RatingDTO dto = (RatingDTO) payload;
            try{
                int userId = Integer.parseInt(msg.getStringProperty("user_id"));
                int trackId = Integer.parseInt(msg.getStringProperty("track_id"));

                em.getTransaction().begin();
                User user = em.find(User.class, userId);
                if (user == null) {
                    em.getTransaction().rollback();
                    message = context.createObjectMessage("User with ID '" + userId + "' not found.");
                    message.setIntProperty("status", 404);
                    return message;
                }
                Audiotrack track = em.find(Audiotrack.class, trackId);
                if (track == null) {
                    em.getTransaction().rollback();
                    message = context.createObjectMessage("Audiotrack with ID '" + trackId + "' not found.");
                    message.setIntProperty("status", 404);
                    return message;
                }
                Rating rating = new Rating();
                rating.setAudioId(track);
                rating.setRating(dto.getRating());
                rating.setUserId(user);
                rating.setRatingTime(dto.getTimestampDate());
                
                em.persist(rating);
                em.getTransaction().commit();

            }
            catch (RollbackException e) {
                em.getTransaction().rollback();
                return rollbackHandler(e, context);
            }
            finally{
                if(em.getTransaction().isActive()) em.getTransaction().rollback();
                message = context.createObjectMessage("New rating added");
                message.setIntProperty("status", 200);
            }
        }
         else{
            message = context.createObjectMessage("Operation 14 payload is not of type RatingDTO");
            message.setIntProperty("status", 500);
        }
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
