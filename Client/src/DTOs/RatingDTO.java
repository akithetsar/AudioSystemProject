package DTOs;

import java.io.Serializable;

public class RatingDTO implements Serializable {
    private String userName;
    private String audioTitle;
    private Character rating;
    private String timestamp;

    // Getters and Setters
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public String getAudioTitle() { return audioTitle; }
    public void setAudioTitle(String audioTitle) { this.audioTitle = audioTitle; }
    public Character getRating() { return rating; }
    public void setRating(Character rating) { this.rating = rating; }
    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

    @Override
    public String toString() {
        return "  Rating {\n" +
               "    User      = '" + userName + "'\n" +
               "    Track     = '" + audioTitle + "'\n" +
               "    Rating    = " + rating + "\n" +
               "    Timestamp = '" + timestamp + "'\n" +
               "  }";
    }
}