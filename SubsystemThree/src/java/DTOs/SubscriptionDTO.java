/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTOs;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author akith
 */
public class SubscriptionDTO implements Serializable {
    private Integer subscriptionId;
    private Date startDate;
    private BigDecimal pricePaid;
    private String userName; 
    private String packageName;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    
    public Integer getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(Integer subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    
    @XmlElement
    public String getStartDate() {
        if (startDate != null) {
            return dateFormat.format(startDate); // Format as "yyyy-MM-dd"
        }
        return null;
    }
        public Date getStartDateAsDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        try {
            this.startDate = dateFormat.parse(startDate); // Parse from "yyyy-MM-dd" string
        } catch (Exception e) {
            this.startDate = null; // Handle parsing errors
        }
    }

    
    
    public BigDecimal getPricePaid() {
        return pricePaid;
    }

    public void setPricePaid(BigDecimal pricePaid) {
        this.pricePaid = pricePaid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
