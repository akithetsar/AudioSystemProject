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
        operationTable.put("15", Operations::operation15);
        operationTable.put("16", Operations::operation16);
        operationTable.put("23", Operations::operation23);
        operationTable.put("24", Operations::operation24);
        operationTable.put("25", Operations::operation25);

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

    public static ObjectMessage operation15(Message msg, JMSContext context) throws JMSException {
        Serializable payload = ((ObjectMessage) msg).getObject();
        System.out.println("Executing Operation 15 with payload: " + payload);
        ObjectMessage message = null;

        if (payload instanceof RatingDTO) {
            RatingDTO dto = (RatingDTO) payload;
            try {
                int userId = Integer.parseInt(msg.getStringProperty("user_id"));
                int trackId = Integer.parseInt(msg.getStringProperty("track_id"));

                em.getTransaction().begin();

                // Find User
                User user = em.find(User.class, userId);
                if (user == null) {
                    em.getTransaction().rollback();
                    message = context.createObjectMessage("User with ID '" + userId + "' not found.");
                    message.setIntProperty("status", 404);
                    return message;
                }

                // Find Audiotrack
                Audiotrack track = em.find(Audiotrack.class, trackId);
                if (track == null) {
                    em.getTransaction().rollback();
                    message = context.createObjectMessage("Audiotrack with ID '" + trackId + "' not found.");
                    message.setIntProperty("status", 404);
                    return message;
                }

                // Find existing Rating
                 List<Rating> ratings = em.createQuery(
                        "SELECT r FROM Rating r WHERE r.userId = :user AND r.audioId = :track", Rating.class)
                        .setParameter("user", user)
                        .setParameter("track", track)
                        .getResultList();

                Rating rating = ratings.isEmpty() ? null : ratings.get(0);

                if (rating == null) {
                    em.getTransaction().rollback();
                    message = context.createObjectMessage("Rating not found for user ID '" + userId + "' and track ID '" + trackId + "'.");
                    message.setIntProperty("status", 404);
                    return message;
                }

                // Update the rating
                rating.setRating(dto.getRating());
                em.merge(rating);

                em.getTransaction().commit();

                message = context.createObjectMessage("Rating updated successfully.");
                message.setIntProperty("status", 200);

            } catch (RollbackException e) {
                em.getTransaction().rollback();
                return rollbackHandler(e, context);
            } catch (NumberFormatException e) {
                message = context.createObjectMessage("Invalid user ID or track ID.");
                message.setIntProperty("status", 400);
            } finally {
                if (em.getTransaction().isActive()) em.getTransaction().rollback();
            }
        } else {
            message = context.createObjectMessage("Operation 15 payload is not of type RatingDTO");
            message.setIntProperty("status", 500);
        }

        return message;
    }

    
    public static ObjectMessage operation16(Message msg, JMSContext context) throws JMSException {
        Serializable payload = ((ObjectMessage) msg).getObject();
        System.out.println("Executing Operation 16 with payload: " + payload);
        ObjectMessage message = null;

       
        try {
            int userId = Integer.parseInt(msg.getStringProperty("user_id"));
            int trackId = Integer.parseInt(msg.getStringProperty("track_id"));

            em.getTransaction().begin();
            
            // Find User
            User user = em.find(User.class, userId);
            if (user == null) {
                em.getTransaction().rollback();
                message = context.createObjectMessage("User with ID '" + userId + "' not found.");
                message.setIntProperty("status", 404);
                return message;
            }

            // Find Audiotrack
            Audiotrack track = em.find(Audiotrack.class, trackId);
            if (track == null) {
                em.getTransaction().rollback();
                message = context.createObjectMessage("Audiotrack with ID '" + trackId + "' not found.");
                message.setIntProperty("status", 404);
                return message;
            }

            // Find existing Rating
            List<Rating> ratings = em.createQuery(
                    "SELECT r FROM Rating r WHERE r.userId = :user AND r.audioId = :track", Rating.class)
                    .setParameter("user", user)
                    .setParameter("track", track)
                    .getResultList();

            Rating rating = ratings.isEmpty() ? null : ratings.get(0);

            if (rating == null) {
                em.getTransaction().rollback();
                message = context.createObjectMessage("Rating not found for user ID '" + userId + "' and track ID '" + trackId + "'.");
                message.setIntProperty("status", 404);
                return message;
            }
            em.createQuery("DELETE FROM Rating WHERE track_id = :trackId AND user_id = :userId")
                .setParameter("trackId", trackId)
                .setParameter("userId", userId)
                .executeUpdate();




            em.getTransaction().commit();

            message = context.createObjectMessage("Successfully deleted.");
            message.setIntProperty("status", 200);

        } catch (RollbackException e) {
            em.getTransaction().rollback();
            return rollbackHandler(e, context);
        } catch (NumberFormatException e) {
            message = context.createObjectMessage("Invalid user ID or track ID.");
            message.setIntProperty("status", 400);
        } finally {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
        }
        

        return message;
    }
    
    public static ObjectMessage operation23(Message msg, JMSContext context) throws JMSException {
        Serializable payload = ((ObjectMessage) msg).getObject();
        System.out.println("Executing Operation 23 with payload: " + payload);
        ObjectMessage message = null;
        List<Package> packages = null;
        
        try{    
            
            em.getTransaction().begin();
            packages = em.createQuery("SELECT t FROM Package t", Package.class).getResultList();
            
            
            
            for(Package pack : packages){
                pack.setSubscriptionList(null);
            }
            message = context.createObjectMessage((Serializable) packages);
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
    
    public static ObjectMessage operation24(Message msg, JMSContext context) throws JMSException {
        Serializable payload = ((ObjectMessage) msg).getObject();
        System.out.println("Executing Operation 24 with payload: " + payload);
        ObjectMessage message = null;
        List<Subscription> subs = null;
        
        try{    
            int userId = Integer.parseInt(msg.getStringProperty("user_id"));
            em.getTransaction().begin();
            User user = em.find(User.class, userId);
            if (user == null) {
                em.getTransaction().rollback();
                message = context.createObjectMessage("User with ID '" + userId + "' not found.");
                message.setIntProperty("status", 404);
                return message;
            }
            
            subs = user.getSubscriptionList();
            
            em.getTransaction().commit();
            List<List<Subscription>> copy = new ArrayList<>();
            List<City> cities = new ArrayList<>();

            for(Subscription sub : subs){
                
                copy.add(sub.getUserId().getSubscriptionList());
                cities.add(sub.getUserId().getCityId());
                
                sub.getPackageId().setSubscriptionList(null);
                
//                sub.setUserId(null);
                sub.getUserId().setSubscriptionList(null);
                sub.getUserId().setAudiotrackList(null);
                sub.getUserId().setFavoritesList(null);
                sub.getUserId().setCityId(null);
                sub.getUserId().setRatingList(null);
                sub.getUserId().setListeningList(null);
                
            }
            message = context.createObjectMessage((Serializable) subs);
            message.setIntProperty("status", 200);
            int i = 0;
            for(Subscription sub : subs){
                sub.getUserId().setCityId(cities.get(i));
                sub.getUserId().setSubscriptionList(copy.get(i));
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
    
    
    public static ObjectMessage operation25(Message msg, JMSContext context) throws JMSException {
        Serializable payload = ((ObjectMessage) msg).getObject();
        System.out.println("Executing Operation 25 with payload: " + payload);
        ObjectMessage message = null;
        List<Listening> listenings = null;
        
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
            
            listenings = track.getListeningList();
            
            em.getTransaction().commit();
            List<List<Listening>> copy = new ArrayList<>();
            List<City> cities = new ArrayList<>();

            for(Listening listen : listenings){
                
                copy.add(listen.getUserId().getListeningList());
                cities.add(listen.getUserId().getCityId());
                
                listen.getAudioId().setListeningList(null);
                listen.getAudioId().setFavoritesList(null);
                listen.getAudioId().setRatingList(null);
                listen.getAudioId().setOwnerId(null);
                listen.getUserId().setSubscriptionList(null);
                listen.getUserId().setAudiotrackList(null);
                listen.getUserId().setFavoritesList(null);
                listen.getUserId().setCityId(null);
                listen.getUserId().setRatingList(null);
                listen.getUserId().setListeningList(null);
                
            }
            message = context.createObjectMessage((Serializable) listenings);
            message.setIntProperty("status", 200);
            int i = 0;
            for(Listening listen : listenings){
                listen.getUserId().setCityId(cities.get(i));
                listen.getUserId().setListeningList(copy.get(i));
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
