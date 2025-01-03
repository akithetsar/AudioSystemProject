/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author akith
 */
@Entity
@Table(name = "listening")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Listening.findAll", query = "SELECT l FROM Listening l"),
    @NamedQuery(name = "Listening.findByListeningId", query = "SELECT l FROM Listening l WHERE l.listeningId = :listeningId"),
    @NamedQuery(name = "Listening.findByStartTime", query = "SELECT l FROM Listening l WHERE l.startTime = :startTime"),
    @NamedQuery(name = "Listening.findByStartSecond", query = "SELECT l FROM Listening l WHERE l.startSecond = :startSecond"),
    @NamedQuery(name = "Listening.findByDuration", query = "SELECT l FROM Listening l WHERE l.duration = :duration")})
public class Listening implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "listening_id")
    private Integer listeningId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "start_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;
    @Basic(optional = false)
    @NotNull
    @Column(name = "start_second")
    private int startSecond;
    @Basic(optional = false)
    @NotNull
    @Column(name = "duration")
    private int duration;
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    @ManyToOne
    private User userId;
    @JoinColumn(name = "audio_id", referencedColumnName = "audio_id")
    @ManyToOne
    private Audiotrack audioId;

    public Listening() {
    }

    public Listening(Integer listeningId) {
        this.listeningId = listeningId;
    }

    public Listening(Integer listeningId, Date startTime, int startSecond, int duration) {
        this.listeningId = listeningId;
        this.startTime = startTime;
        this.startSecond = startSecond;
        this.duration = duration;
    }

    public Integer getListeningId() {
        return listeningId;
    }

    public void setListeningId(Integer listeningId) {
        this.listeningId = listeningId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
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

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public Audiotrack getAudioId() {
        return audioId;
    }

    public void setAudioId(Audiotrack audioId) {
        this.audioId = audioId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (listeningId != null ? listeningId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Listening)) {
            return false;
        }
        Listening other = (Listening) object;
        if ((this.listeningId == null && other.listeningId != null) || (this.listeningId != null && !this.listeningId.equals(other.listeningId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Listening[ listeningId=" + listeningId + " ]";
    }
    
}
