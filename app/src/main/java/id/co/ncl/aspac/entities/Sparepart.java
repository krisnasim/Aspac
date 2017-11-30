package id.co.ncl.aspac.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Created by Jonathan Simananda on 29/11/2017.
 */

@Entity(tableName = "sparepart", indices = {@Index("id")}, foreignKeys = @ForeignKey(entity = Machine.class,
                                                           parentColumns = "id",
                                                           childColumns = "machine_id",
                                                           onDelete = CASCADE))
public class Sparepart {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private String description;
    private String number;
    private int stock;

//    @ColumnInfo(name = "in_date")
//    private String inDate;
//
//    @ColumnInfo(name = "out_date")
//    private String outDate;

    @ColumnInfo(name = "machine_id")
    private int machineID;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

//    public String getInDate() {
//        return inDate;
//    }
//
//    public void setInDate(String inDate) {
//        this.inDate = inDate;
//    }
//
//    public String getOutDate() {
//        return outDate;
//    }
//
//    public void setOutDate(String outDate) {
//        this.outDate = outDate;
//    }

    public int getMachineID() {
        return machineID;
    }

    public void setMachineID(int machineID) {
        this.machineID = machineID;
    }
}
