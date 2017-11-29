package id.co.ncl.aspac.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;

import java.util.Date;

/**
 * Created by Jonathan Simananda on 29/11/2017.
 */

@Entity(tableName = "sparepart", foreignKeys = @ForeignKey(entity = Machine.class,
                                                           parentColumns = "id",
                                                           childColumns = "machine_id"))
public class Sparepart {

    private String name;
    private String description;
    private String number;
    private int stock;

    @ColumnInfo(name = "in_date")
    private Date inDate;

    @ColumnInfo(name = "out_date")
    private Date outDate;

    @ColumnInfo(name = "machine_id")
    private int machineID;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public Date getInDate() {
        return inDate;
    }

    public void setInDate(Date inDate) {
        this.inDate = inDate;
    }

    public Date getOutDate() {
        return outDate;
    }

    public void setOutDate(Date outDate) {
        this.outDate = outDate;
    }

    public int getMachineID() {
        return machineID;
    }

    public void setMachineID(int machineID) {
        this.machineID = machineID;
    }
}
