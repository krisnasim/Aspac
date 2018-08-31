package id.co.ncl.aspac.model;

import java.util.ArrayList;

/**
 * Created by Jonathan Simananda on 01/09/2018.
 */

public class MachineDetail {

    //all the variables
    private String tempServiceID;
    private int isRTAS;
    private int isRTBS;
    private int isJobFinished;
    private ArrayList<MachineSparepart> spareparts;

    public MachineDetail() {

    }

    public String getTempServiceID() {
        return tempServiceID;
    }

    public void setTempServiceID(String tempServiceID) {
        this.tempServiceID = tempServiceID;
    }

    public int isRTAS() {
        return isRTAS;
    }

    public void setRTAS(int RTAS) {
        isRTAS = RTAS;
    }

    public int isRTBS() {
        return isRTBS;
    }

    public void setRTBS(int RTBS) {
        isRTBS = RTBS;
    }

    public int isJobFinished() {
        return isJobFinished;
    }

    public void setJobFinished(int jobFinished) {
        isJobFinished = jobFinished;
    }

    public ArrayList<MachineSparepart> getSpareparts() {
        return spareparts;
    }

    public void setSpareparts(ArrayList<MachineSparepart> spareparts) {
        this.spareparts = spareparts;
    }
}
