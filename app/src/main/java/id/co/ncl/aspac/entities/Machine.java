package id.co.ncl.aspac.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by Jonathan Simananda on 29/11/2017.
 */

@Entity(tableName = "machine", foreignKeys = @ForeignKey(entity = Service.class,
                                                         parentColumns = "id",
                                                         childColumns = "service_id"))
public class Machine {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String brand;
    private String model;

    @ColumnInfo(name = "serial_number")
    private String serialNumber;

    @ColumnInfo(name = "sales_number")
    private String salesNumber;

    //foreign key to service ID
    @ColumnInfo(name = "service_id")
    private int serviceID;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getSalesNumber() {
        return salesNumber;
    }

    public void setSalesNumber(String salesNumber) {
        this.salesNumber = salesNumber;
    }

    public int getServiceID() {
        return serviceID;
    }

    public void setServiceID(int serviceID) {
        this.serviceID = serviceID;
    }
}
