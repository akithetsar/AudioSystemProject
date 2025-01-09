package subsystemtwo;

import DTOs.AudiotrackDTO;
import DTOs.CategoryDTO;
import DTOs.CityDTO;
import DTOs.UserDTO;
import entities.Audiotrack;
import entities.Category;
import entities.City;
import entities.User;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import java.io.Serializable;
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
    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("SubsystemTwoPU");
    private static EntityManager em = emf.createEntityManager();
    
    static {
        operationTable.put("5", Operations::operation5);
        operationTable.put("6", Operations::operation6);
        operationTable.put("7", Operations::operation7);
        operationTable.put("8", Operations::operation8);
        operationTable.put("17", Operations::operation17);
        operationTable.put("20", Operations::operation20);
        operationTable.put("21", Operations::operation21);
        operationTable.put("22", Operations::operation22);
        // Add other operations here...
    }

    
     public static ObjectMessage operation5(Message msg, JMSContext context) throws JMSException {
        Serializable payload = ((ObjectMessage) msg).getObject();
        System.out.println("Executing Operation 5 with payload: " + payload);
        ObjectMessage message = null;
        if(payload instanceof CategoryDTO){
            CategoryDTO dto = (CategoryDTO) payload;
            try{    
                em.getTransaction().begin();
                
                Category newCategory = new Category();
                newCategory.setName(dto.getName());
                
                
                em.persist(newCategory);
                em.getTransaction().commit();
            }
            catch (RollbackException e) {
                em.getTransaction().rollback();
                return rollbackHandler(e, context);
            }
            finally{
                if(em.getTransaction().isActive()) em.getTransaction().rollback();
                message = context.createObjectMessage("New cateogry created");
                message.setIntProperty("status", 200);
            }
        }
        else{
            message = context.createObjectMessage("Operation 5 payload is not of type CategoryDTO");
            message.setIntProperty("status", 500);
        }
        return message;
    }
    public static ObjectMessage operation6(Message msg, JMSContext context) throws JMSException {
        Serializable payload = ((ObjectMessage) msg).getObject();
        System.out.println("Executing Operation 6 with payload: " + payload);
        ObjectMessage message = null;
        if(payload instanceof AudiotrackDTO){
            AudiotrackDTO dto = (AudiotrackDTO) payload;
            try{    
                em.getTransaction().begin();
                
                Audiotrack newTrack = new Audiotrack();
                newTrack.setName(dto.getName());
                newTrack.setDuration(dto.getDuration());
                newTrack.setUploadTime(dto.getUploadTimeDate());
                
                User user = null;
                try {
                    user = em.createNamedQuery("User.findByName", User.class)
                                .setParameter("name", dto.getOwnerName())
                                .getSingleResult();
                } catch (Exception e) {
                    em.getTransaction().rollback();
                    message = context.createObjectMessage("User with name '" + dto.getOwnerName()+ "' not found.");
                    message.setIntProperty("status", 404);
                    return message;
                }
                newTrack.setOwnerId(user);
                em.persist(newTrack);
                em.getTransaction().commit();
            }
            catch (RollbackException e) {
                em.getTransaction().rollback();
                return rollbackHandler(e, context);
            }
            finally{
                if(em.getTransaction().isActive()) em.getTransaction().rollback();
                message = context.createObjectMessage("New audiotrack created");
                message.setIntProperty("status", 200);
            }
        }
        else{
            message = context.createObjectMessage("Operation 6 payload is not of type AudiotrackDTO");
            message.setIntProperty("status", 500);
        }
        return message;
    }


    public static ObjectMessage operation7(Message msg, JMSContext context) throws JMSException{
        Serializable payload = ((ObjectMessage) msg).getObject();
        System.out.println("Executing Operation 7 with payload: " + payload);
        ObjectMessage message = null;
        
        try{
            String name = msg.getStringProperty("name");
            int trackId = Integer.parseInt(msg.getStringProperty("track_id"));

            em.getTransaction().begin();

            


            // Find the user by ID
            Audiotrack track = em.find(Audiotrack.class, trackId);
            if (track == null) {
                em.getTransaction().rollback();
                message = context.createObjectMessage("Track with ID '" + trackId + "' not found.");
                message.setIntProperty("status", 404);
                return message;
            }

            // Update the user entity
            if (name != null) {
                track.setName(name);
            }


            em.merge(track);
            em.getTransaction().commit();

        }
        catch (RollbackException e) {
            em.getTransaction().rollback();
            return rollbackHandler(e, context);
        }
        finally{
            if(em.getTransaction().isActive()) em.getTransaction().rollback();
            message = context.createObjectMessage("Audiotrack data updated");
            message.setIntProperty("status", 200);
        }
        
        
        return message;
    }
    
    
    public static ObjectMessage operation8(Message msg, JMSContext context) throws JMSException{
        Serializable payload = ((ObjectMessage) msg).getObject();
        System.out.println("Executing Operation 8 with payload: " + payload);
        ObjectMessage message = null;
        if(payload instanceof CategoryDTO){
            CategoryDTO dto = (CategoryDTO) payload;
            try{
                int trackId = Integer.parseInt(msg.getStringProperty("track_id"));

                em.getTransaction().begin();


                // Find the user by ID
                Audiotrack track = em.find(Audiotrack.class, trackId);
                if (track == null) {
                    em.getTransaction().rollback();
                    message = context.createObjectMessage("Track with ID '" + trackId + "' not found.");
                    message.setIntProperty("status", 404);
                    return message;
                }
                Category category = null;
                try {
                    category = em.createNamedQuery("Category.findByName", Category.class)
                                .setParameter("name", dto.getName())
                                .getSingleResult();
                } catch (Exception e) {
                    em.getTransaction().rollback();
                    message = context.createObjectMessage("Cateogry with name '" + dto.getName()+ "' not found.");
                    message.setIntProperty("status", 404);
                    return message;
                }

                if(!track.getCategoryList().contains(category)){
                    track.getCategoryList().add(category);
                }
                
                em.merge(track);
                em.getTransaction().commit();

            }
            catch (RollbackException e) {
                em.getTransaction().rollback();
                return rollbackHandler(e, context);
            }
            finally{
                if(em.getTransaction().isActive()) em.getTransaction().rollback();
                message = context.createObjectMessage("Added new category to the audiotrack");
                message.setIntProperty("status", 200);
            }
        }
        else{
            message = context.createObjectMessage("Operation 8 payload is not of type CategoryDTO");
            message.setIntProperty("status", 500);
        }
        return message;
    } 
    
    public static ObjectMessage operation17(Message msg, JMSContext context) throws JMSException {
        Serializable payload = ((ObjectMessage) msg).getObject();
        System.out.println("Executing Operation 17 with payload: " + payload);
        ObjectMessage message = null;
        
        try{    
            em.getTransaction().begin();
            int trackId = Integer.parseInt(msg.getStringProperty("track_id"));
        
//        em.createQuery("DELETE FROM favorites WHERE audio_id = :trackId")
//            .setParameter("trackId", trackId)
//             .executeUpdate();
           em.createQuery("DELETE FROM Audiotrack WHERE audio_id = :trackId")
            .setParameter("trackId", trackId)
            .executeUpdate();

            em.getTransaction().commit();
            
            
            message = context.createObjectMessage("Sucesfully deleted");
            message.setIntProperty("status", 200);
            em.getTransaction().begin();
            
            }catch (RollbackException e) {
                em.getTransaction().rollback();
                return rollbackHandler(e, context);

            }
            finally{
                if(em.getTransaction().isActive()) em.getTransaction().rollback();
               
            }
        return message;
    }
    
    public static ObjectMessage operation20(Message msg, JMSContext context) throws JMSException {
        Serializable payload = ((ObjectMessage) msg).getObject();
        System.out.println("Executing Operation 20 with payload: " + payload);
        ObjectMessage message = null;
        List<Category> categories = null;
        
        try{    
            em.getTransaction().begin();

            categories = em.createQuery("SELECT c FROM Category c", Category.class).getResultList();
 
            em.getTransaction().commit();
           
            List<List<Audiotrack>> copy = new ArrayList<>();

            
            for(Category category : categories){
                
                copy.add(category.getAudiotrackList());
                category.setAudiotrackList(null);
            }
            message = context.createObjectMessage((Serializable) categories);
            message.setIntProperty("status", 200);
            }catch (RollbackException e) {
                em.getTransaction().rollback();
                return rollbackHandler(e, context);

            }
            finally{
                if(em.getTransaction().isActive()) em.getTransaction().rollback();
               
            }
        return message;
    }
    
    public static ObjectMessage operation21(Message msg, JMSContext context) throws JMSException {
        Serializable payload = ((ObjectMessage) msg).getObject();
        System.out.println("Executing Operation 21 with payload: " + payload);
        ObjectMessage message = null;
        List<Audiotrack> tracks = null;
        
        try{    
            em.getTransaction().begin();

            tracks = em.createQuery("SELECT t FROM Audiotrack t", Audiotrack.class).getResultList();
            
            em.getTransaction().commit();
            
            List<List<Category>> copy = new ArrayList<>();
          
            for(Audiotrack track : tracks){
                List<Category> categoryList = track.getCategoryList();
                copy.add(categoryList = track.getCategoryList());
                track.setCategoryList(null);
                track.getOwnerId().setAudiotrackList(null);
                track.getOwnerId().getCityId().setUserList(null);
                
            }
            message = context.createObjectMessage((Serializable) tracks);
            message.setIntProperty("status", 200);
            em.getTransaction().begin();
            int i = 0;
            for(Audiotrack track : tracks){
                track.setCategoryList(copy.get(i));
                i++;
            }
            }catch (RollbackException e) {
                em.getTransaction().rollback();
                return rollbackHandler(e, context);

            }
            finally{
                if(em.getTransaction().isActive()) em.getTransaction().rollback();
               
            }
        return message;
    }
    
    public static ObjectMessage operation22(Message msg, JMSContext context) throws JMSException {
        Serializable payload = ((ObjectMessage) msg).getObject();
        System.out.println("Executing Operation 22 with payload: " + payload);
        ObjectMessage message = null;
        List<Category> categories = null;
        
        try{    
            int trackId = Integer.parseInt(msg.getStringProperty("track_id"));
            em.getTransaction().begin();
            Audiotrack track = em.find(Audiotrack.class, trackId);
            if (track == null) {
                em.getTransaction().rollback();
                message = context.createObjectMessage("Track with ID '" + trackId + "' not found.");
                message.setIntProperty("status", 404);
                return message;
            }
            
            categories = track.getCategoryList();
            
            em.getTransaction().commit();
            List<List<Audiotrack>> copy = new ArrayList<>();

            
            for(Category category : categories){
                
                copy.add(category.getAudiotrackList());
                category.setAudiotrackList(null);
            }
            message = context.createObjectMessage((Serializable) categories);
            message.setIntProperty("status", 200);
            }catch (RollbackException e) {
                em.getTransaction().rollback();
                return rollbackHandler(e, context);

            }
            finally{
                if(em.getTransaction().isActive()) em.getTransaction().rollback();
               
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
