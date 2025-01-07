/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author akith
 */
@Entity
@Table(name = "audiotrack")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Audiotrack.findAll", query = "SELECT a FROM Audiotrack a"),
    @NamedQuery(name = "Audiotrack.findByAudioId", query = "SELECT a FROM Audiotrack a WHERE a.audioId = :audioId"),
    @NamedQuery(name = "Audiotrack.findByName", query = "SELECT a FROM Audiotrack a WHERE a.name = :name"),
    @NamedQuery(name = "Audiotrack.findByDuration", query = "SELECT a FROM Audiotrack a WHERE a.duration = :duration"),
    @NamedQuery(name = "Audiotrack.findByUploadTime", query = "SELECT a FROM Audiotrack a WHERE a.uploadTime = :uploadTime")})
public class Audiotrack implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "audio_id")
    private Integer audioId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Column(name = "duration")
    private int duration;
    @Basic(optional = false)
    @NotNull
    @Column(name = "upload_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date uploadTime;
    @JoinTable(name = "audiocategory", joinColumns = {
        @JoinColumn(name = "audio_id", referencedColumnName = "audio_id")}, inverseJoinColumns = {
        @JoinColumn(name = "category_id", referencedColumnName = "category_id")})
    @ManyToMany
    private List<Category> categoryList;
    @JoinColumn(name = "owner_id", referencedColumnName = "user_id")
    @ManyToOne
    private User ownerId;

    public Audiotrack() {
    }

    public Audiotrack(Integer audioId) {
        this.audioId = audioId;
    }

    public Audiotrack(Integer audioId, String name, int duration, Date uploadTime) {
        this.audioId = audioId;
        this.name = name;
        this.duration = duration;
        this.uploadTime = uploadTime;
    }

    public Integer getAudioId() {
        return audioId;
    }

    public void setAudioId(Integer audioId) {
        this.audioId = audioId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }

    @XmlTransient
    public List<Category> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    public User getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(User ownerId) {
        this.ownerId = ownerId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (audioId != null ? audioId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Audiotrack)) {
            return false;
        }
        Audiotrack other = (Audiotrack) object;
        if ((this.audioId == null && other.audioId != null) || (this.audioId != null && !this.audioId.equals(other.audioId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Audiotrack[ audioId=" + audioId + " ]";
    }
    
}
