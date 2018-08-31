package id.co.ncl.aspac.model;

/**
 * Created by Jonathan Simananda on 01/09/2018.
 */

public class MachineSparepart {
    private int sparepartID;
    private int quantity;

    public MachineSparepart() {

    }

    public int getSparepartID() {
        return sparepartID;
    }

    public void setSparepartID(int sparepartID) {
        this.sparepartID = sparepartID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
