package model;

/**
 * Division.java
 * Team: Cats | CSc3350 SP2026
 *
 * Represents one row from the division table.
 * A division is a company office location.
 *
 * Example: ID=1, Name="Technology Engineering",
 *          city="Atlanta", state="GA"
 */
public class Division {

    private int    id;
    private String name;
    private String city;
    private String addressLine1;
    private String addressLine2;
    private String state;
    private String country;
    private String postalCode;

    // ----------------------------------------------------------------
    // CONSTRUCTORS
    // ----------------------------------------------------------------

    public Division() {}

    public Division(int id, String name, String city, String addressLine1,
                    String addressLine2, String state, String country,
                    String postalCode) {
        this.id           = id;
        this.name         = name;
        this.city         = city;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.state        = state;
        this.country      = country;
        this.postalCode   = postalCode;
    }

    // ----------------------------------------------------------------
    // GETTERS
    // ----------------------------------------------------------------
    public int    getId()           { return id; }
    public String getName()         { return name; }
    public String getCity()         { return city; }
    public String getAddressLine1() { return addressLine1; }
    public String getAddressLine2() { return addressLine2; }
    public String getState()        { return state; }
    public String getCountry()      { return country; }
    public String getPostalCode()   { return postalCode; }

    // ----------------------------------------------------------------
    // SETTERS
    // ----------------------------------------------------------------
    public void setId(int id)                     { this.id = id; }
    public void setName(String name)              { this.name = name; }
    public void setCity(String city)              { this.city = city; }
    public void setAddressLine1(String a)         { this.addressLine1 = a; }
    public void setAddressLine2(String a)         { this.addressLine2 = a; }
    public void setState(String state)            { this.state = state; }
    public void setCountry(String country)        { this.country = country; }
    public void setPostalCode(String postalCode)  { this.postalCode = postalCode; }

    // ----------------------------------------------------------------
    // toString
    // ----------------------------------------------------------------
    @Override
    public String toString() {
        return String.format("Division %-4d | %-25s | %s, %s", id, name, city, state);
    }
}
