package id.co.ncl.aspac.model;

import java.util.Date;

/**
 * Created by Jonathan Simananda on 08/11/2017.
 */

public class Mesin {

    //variables belonged to Work Model class
    private String mesinBrand;
    private String mesinModel;
    private String mesinNomorSeri;
    private String mesinStatus;

    public String getMesinBrand() {
        return mesinBrand;
    }

    public void setMesinBrand(String mesinBrand) {
        this.mesinBrand = mesinBrand;
    }

    public String getMesinModel() {
        return mesinModel;
    }

    public void setMesinModel(String mesinModel) {
        this.mesinModel = mesinModel;
    }

    public String getMesinNomorSeri() {
        return mesinNomorSeri;
    }

    public void setMesinNomorSeri(String mesinNomorSeri) {
        this.mesinNomorSeri = mesinNomorSeri;
    }

    public String getMesinStatus() {
        return mesinStatus;
    }

    public void setMesinStatus(String mesinStatus) {
        this.mesinStatus = mesinStatus;
    }
}
