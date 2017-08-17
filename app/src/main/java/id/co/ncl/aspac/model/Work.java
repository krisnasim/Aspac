package id.co.ncl.aspac.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by Jonathan Simananda on 18/08/2017.
 */

public class Work implements Parcelable {

    //variables belonged to Work Model class
    private String workTitle;
    private String workStatus;
    private String workDescShort;
    private String workDescription;
    private Date workDateTime;

    public String getWorkTitle() {
        return workTitle;
    }

    public void setWorkTitle(String workTitle) {
        this.workTitle = workTitle;
    }

    public String getWorkStatus() {
        return workStatus;
    }

    public void setWorkStatus(String workStatus) {
        this.workStatus = workStatus;
    }

    public String getWorkDescShort() {
        return workDescShort;
    }

    public void setWorkDescShort(String workDescShort) {
        this.workDescShort = workDescShort;
    }

    public String getWorkDescription() {
        return workDescription;
    }

    public void setWorkDescription(String workDescription) {
        this.workDescription = workDescription;
    }

    public Date getWorkDateTime() {
        return workDateTime;
    }

    public void setWorkDateTime(Date workDateTime) {
        this.workDateTime = workDateTime;
    }

    /////////////////////////////////////
    //Parcelable Boilerplate, Approved!//
    /////////////////////////////////////
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.workTitle);
        dest.writeString(this.workStatus);
        dest.writeString(this.workDescShort);
        dest.writeString(this.workDescription);
        dest.writeLong(this.workDateTime != null ? this.workDateTime.getTime() : -1);
    }

    public Work() {
    }

    protected Work(Parcel in) {
        this.workTitle = in.readString();
        this.workStatus = in.readString();
        this.workDescShort = in.readString();
        this.workDescription = in.readString();
        long tmpWorkDateTime = in.readLong();
        this.workDateTime = tmpWorkDateTime == -1 ? null : new Date(tmpWorkDateTime);
    }

    public static final Parcelable.Creator<Work> CREATOR = new Parcelable.Creator<Work>() {
        @Override
        public Work createFromParcel(Parcel source) {
            return new Work(source);
        }

        @Override
        public Work[] newArray(int size) {
            return new Work[size];
        }
    };
}
