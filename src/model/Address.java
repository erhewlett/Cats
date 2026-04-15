package model;

/**
 * Address.java
 * Team: Cats | CSc3350 SP2026
 *
 * Represents one row from the addresses table.
 * Stores a physical address using foreign keys to cities and states
 * instead of raw strings — that's the 3NF design.
 *
 * Example: addressID=1, street="123 Doghouse Lane",
 *          cityID=1 (Atlanta), stateID=10 (GA), zip="30301"
 *
 * We also store cityName and stateAbbrev as convenience fields
 * so the UI can display "Atlanta, GA" without a second DB lookup.
 */
public class Address {

    private int    addressID;
    private String street;
    private int    cityID;
    private int    stateID;
    private String zip;

    // Convenience display fields — populated by JOIN queries in the DAO
    // Not stored in the addresses table itself
    private String cityName;
    private String stateAbbrev;

    // ----------------------------------------------------------------
    // CONSTRUCTORS
    // ----------------------------------------------------------------

    public Address() {}

    public Address(int addressID, String street, int cityID, int stateID, String zip) {
        this.addressID = addressID;
        this.street    = street;
        this.cityID    = cityID;
        this.stateID   = stateID;
        this.zip       = zip;
    }

    // ----------------------------------------------------------------
    // GETTERS
    // ----------------------------------------------------------------
    public int    getAddressID()    { return addressID; }
    public String getStreet()       { return street; }
    public int    getCityID()       { return cityID; }
    public int    getStateID()      { return stateID; }
    public String getZip()          { return zip; }
    public String getCityName()     { return cityName; }
    public String getStateAbbrev()  { return stateAbbrev; }

    // ----------------------------------------------------------------
    // SETTERS
    // ----------------------------------------------------------------
    public void setAddressID(int addressID)       { this.addressID = addressID; }
    public void setStreet(String street)          { this.street = street; }
    public void setCityID(int cityID)             { this.cityID = cityID; }
    public void setStateID(int stateID)           { this.stateID = stateID; }
    public void setZip(String zip)                { this.zip = zip; }
    public void setCityName(String cityName)      { this.cityName = cityName; }
    public void setStateAbbrev(String abbrev)     { this.stateAbbrev = abbrev; }

    // ----------------------------------------------------------------
    // toString
    // ----------------------------------------------------------------
    @Override
    public String toString() {
        String city  = (cityName    != null) ? cityName    : "cityID=" + cityID;
        String state = (stateAbbrev != null) ? stateAbbrev : "stateID=" + stateID;
        return String.format("%s, %s, %s %s", street, city, state, zip);
    }
}
