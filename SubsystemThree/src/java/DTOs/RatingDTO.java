package DTOs;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.xml.bind.annotation.XmlElement;

public class RatingDTO implements Serializable {
    private String userName;
    private String audioTitle;
    private Character rating;
    private Date timestamp;
    
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAudioTitle() {
        return audioTitle;
    }

    public void setAudioTitle(String audioTitle) {
        this.audioTitle = audioTitle;
    }

    public Character getRating() {
        return rating;
    }

    public void setRating(Character rating) {
        this.rating = rating;
    }

  
    @XmlElement
    public String getTimestamp() {
        if (timestamp != null) {
            return dateFormat.format(timestamp); // Format as "yyyy-MM-dd"
        }
        return null;
    }
    
    public Date getTimestampDate() {
            return timestamp;
    }

    public void setTimestamp(String timestamp) {
        try {
            this.timestamp = dateFormat.parse(timestamp); // Parse from "yyyy-MM-dd" string
        } catch (Exception e) {
            this.timestamp = null; // Handle parsing errors
        }
    }
    
}
