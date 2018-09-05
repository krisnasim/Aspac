package id.co.ncl.aspac.model;

/**
 * Created by Jonathan Simananda on 30/11/2017.
 */

public class Machine {

    private int id;
    private int tempServiceID;
    private String machineID;
    private String brand;
    private String model;
    private String name;
    private String serialNumber;
    private String salesNumber;
    private int repairID;
    //foreign key to service ID
    private String noLPS;
    private String serviceID;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTempServiceID() {
        return tempServiceID;
    }

    public void setTempServiceID(int tempServiceID) {
        this.tempServiceID = tempServiceID;
    }

    public String getMachineID() {
        return machineID;
    }

    public void setMachineID(String machineID) {
        this.machineID = machineID;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getNoLPS() {
        return noLPS;
    }

    public void setNoLPS(String noLPS) {
        this.noLPS = noLPS;
    }

    public int getRepairID() {
        return repairID;
    }

    public void setRepairID(int repairID) {
        this.repairID = repairID;
    }

    public String getServiceID() {
        return serviceID;
    }

    public void setServiceID(String serviceID) {
        this.serviceID = serviceID;
    }
}
