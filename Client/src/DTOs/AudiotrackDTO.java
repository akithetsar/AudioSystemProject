package DTOs;

import java.io.Serializable;

public class AudiotrackDTO implements Serializable {
    private String name;
    private int duration;
    private String uploadTime;
    private String ownerName;
    private String ownerEmail;
    

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }
    public String getUploadTime() { return uploadTime; }
    public void setUploadTime(String uploadTime) { this.uploadTime = uploadTime; }
    public String getOwnerName() { return ownerName; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }
    public String getOwnerEmail() { return ownerEmail; }
    public void setOwnerEmail(String email) { this.ownerEmail = email; }

    @Override
    public String toString() {
        return "  Audiotrack {\n" +
               "    Name        = '" + name + "'\n" +
               "    Duration    = " + duration + "s\n" +
               "    Upload Time = '" + uploadTime + "'\n" +
               "    Owner       = '" + ownerName + "'\n" +
               "  }";
    }
}