package id.co.ncl.aspac.model;

/**
 * Created by Jonathan Simananda on 30/11/2017.
 */

public class Sparepart {

    private int id;
    private String sparepartID;
    private String code;
    private String name;
    //foreign key to machine ID
    private int machineID;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSparepartID() {
        return sparepartID;
    }

    public void setSparepartID(String sparepartID) {
        this.sparepartID = sparepartID;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMachineID() {
        return machineID;
    }

    public void setMachineID(int machineID) {
        this.machineID = machineID;
    }
}
