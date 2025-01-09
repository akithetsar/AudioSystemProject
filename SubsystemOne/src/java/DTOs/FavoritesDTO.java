package DTOs;

import java.io.Serializable;

public class FavoritesDTO implements Serializable {
    private String userName;
    private String audioTitle;

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
}
