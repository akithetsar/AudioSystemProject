package DTOs;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.xml.bind.annotation.XmlElement;

public class ListeningDTO implements Serializable {
    private String userName;
    private String audioTitle;
    private Date startTime;
    private int startSecond;
    private int duration;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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
    
    @XmlElement
    public String getStartTime() {
        if (startTime != null) {
            return dateFormat.format(startTime);
        }
        return null;
    }
        public Date getStartTimeDate() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        try {
            this.startTime = dateFormat.parse(startTime);
        } catch (Exception e) {
            this.startTime = null; 
        }
    }

    public int getStartSecond() {
        return startSecond;
    }

    public void setStartSecond(int startSecond) {
        this.startSecond = startSecond;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
