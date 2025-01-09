package DTOs;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AudiotrackDTO implements Serializable {
    private String name;
    private int duration;
    private Date uploadTime;
    private String ownerName;
    
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @XmlElement
    public String getUploadTime() {
        if (uploadTime != null) {
            return dateFormat.format(uploadTime); // Format as "yyyy-MM-dd"
        }
        return null;
    }

    public Date getUploadTimeDate(){
        return uploadTime;
    }
    public void setUploadTime(String uploadTime) {
        try {
            this.uploadTime = dateFormat.parse(uploadTime); // Parse from "yyyy-MM-dd" string
        } catch (Exception e) {
            this.uploadTime = null; // Handle parsing errors
        }
    }

    // Getter and setter for other fields
    @XmlElement
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement
    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @XmlElement
    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
}
