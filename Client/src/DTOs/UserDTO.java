package DTOs;

import java.io.Serializable;

public class UserDTO implements Serializable {
    private String name;
    private String email;
    private int birthYear;
    private String gender;
    private String cityName;

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public int getBirthYear() { return birthYear; }
    public void setBirthYear(int birthYear) { this.birthYear = birthYear; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getCityName() { return cityName; }
    public void setCityName(String cityName) { this.cityName = cityName; }

    @Override
    public String toString() {
        return "  User {\n" +
               "    Name       = '" + name + "'\n" +
               "    Email      = '" + email + "'\n" +
               "    Birth Year = " + birthYear + "\n" +
               "    Gender     = '" + gender + "'\n" +
               "    City       = '" + cityName + "'\n" +
               "  }";
    }
}