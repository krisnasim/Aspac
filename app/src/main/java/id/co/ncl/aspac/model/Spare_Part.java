package id.co.ncl.aspac.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by jonat on 15/11/2017.
 */

public class Spare_Part implements Serializable {
    private String sparepartName;
    private String sparepartDesc;
    private String sparepartNumb;
    private int sparepartStock;
    private Date sparepartInDate;
    private Date sparepartOutDate;

    public Spare_Part(String name, String desc) {
        sparepartName = name;
        sparepartDesc = desc;
    }

    public String getSparepartName() {
        return sparepartName;
    }

    public void setSparepartName(String sparepartName) {
        this.sparepartName = sparepartName;
    }

    public String getSparepartDesc() {
        return sparepartDesc;
    }

    public void setSparepartDesc(String sparepartDesc) {
        this.sparepartDesc = sparepartDesc;
    }

    public String getSparepartNumb() {
        return sparepartNumb;
    }

    public void setSparepartNumb(String sparepartNumb) {
        this.sparepartNumb = sparepartNumb;
    }

    public int getSparepartStock() {
        return sparepartStock;
    }

    public void setSparepartStock(int sparepartStock) {
        this.sparepartStock = sparepartStock;
    }

    public Date getSparepartInDate() {
        return sparepartInDate;
    }

    public void setSparepartInDate(Date sparepartInDate) {
        this.sparepartInDate = sparepartInDate;
    }

    public Date getSparepartOutDate() {
        return sparepartOutDate;
    }

    public void setSparepartOutDate(Date sparepartOutDate) {
        this.sparepartOutDate = sparepartOutDate;
    }

    @Override
    public String toString() { return sparepartName; }

}
