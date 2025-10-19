package DTOs;

import java.io.Serializable;
import java.math.BigDecimal;

public class SubscriptionDTO implements Serializable {
    private Integer subscriptionId;
    private String startDate;
    private BigDecimal pricePaid;
    private String userName;
    private String packageName;

    // Getters and Setters
    public Integer getSubscriptionId() { return subscriptionId; }
    public void setSubscriptionId(Integer subscriptionId) { this.subscriptionId = subscriptionId; }
    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }
    public BigDecimal getPricePaid() { return pricePaid; }
    public void setPricePaid(BigDecimal pricePaid) { this.pricePaid = pricePaid; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public String getPackageName() { return packageName; }
    public void setPackageName(String packageName) { this.packageName = packageName; }

    @Override
    public String toString() {
        return "  Subscription {\n" +
               "    ID       = " + subscriptionId + "\n" +
               "    User     = '" + userName + "'\n" +
               "    Package  = '" + packageName + "'\n" +
               "    Date     = '" + startDate + "'\n" +
               "    Price    = " + pricePaid + "\n" +
               "  }";
    }
}