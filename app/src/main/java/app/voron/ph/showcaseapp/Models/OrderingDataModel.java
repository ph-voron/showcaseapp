package app.voron.ph.showcaseapp.Models;

/**
 * Created by TJack on 17.11.2017.
 */

public class OrderingDataModel {
    public String name = null;
    public String phoneNumber = null;
    public String street = null;
    public String house = null;
    public String building = null;
    public String apartments = null;
    public String comments = null;
    //
    public  OrderingDataModel(){

    }
    //
    public OrderingDataModel(String name,
                             String phoneNumber,
                             String street,
                             String house,
                             String building,
                             String apartments,
                             String comments){
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.street = street;
        this.house = house;
        this.building = building;
        this.apartments = apartments;
        this.comments = comments;
    }
}
