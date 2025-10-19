package DTOs;

import java.io.Serializable;
import java.math.BigDecimal;

public class PackageDTO implements Serializable {
    private String name;
    private BigDecimal monthlyPrice;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public BigDecimal getMonthlyPrice() { return monthlyPrice; }
    public void setMonthlyPrice(BigDecimal monthlyPrice) { this.monthlyPrice = monthlyPrice; }

    @Override
    public String toString() {
        return "  Package {\n" +
               "    Name  = '" + name + "'\n" +
               "    Price = " + monthlyPrice + "\n" +
               "  }";
    }
}