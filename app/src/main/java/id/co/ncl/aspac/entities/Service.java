package id.co.ncl.aspac.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

/**
 * Created by Jonathan Simananda on 29/11/2017.
 */

class CustomerBranch {

    @ColumnInfo(name = "id")
    private int CBID;

    private int code;
    private String initial;
    private String name;
    private String status;

    @ColumnInfo(name = "pic")
    private String PIC;

    @ColumnInfo(name = "pic_phone_number")
    private String PICPhoneNumber;

    @ColumnInfo(name = "pic_email")
    private String PICEmail;

    @ColumnInfo(name = "kw")
    private int KW;

    @ColumnInfo(name = "sljj")
    private int SLJJ;
    private String address;

    @ColumnInfo(name = "regency_id")
    private int regencyID;

    @ColumnInfo(name = "province_id")
    private int provinceID;

    @ColumnInfo(name = "post_code")
    private int postCode;

    @ColumnInfo(name = "office_phone_number")
    private String officePhoneNumber;
    private String fax;

    @ColumnInfo(name = "customer_id")
    private int customerID;

    @ColumnInfo(name = "coordinator_id")
    private int coordinatorID;

    @ColumnInfo(name = "teknisi_id")
    private int teknisiID;

    @ColumnInfo(name = "sales_id")
    private int salesID;

    private String username;
    private String password;

    @ColumnInfo(name = "remember_token")
    private String rememberToken;

    @ColumnInfo(name = "created_at")
    private Date createdAt;

    @ColumnInfo(name = "updated_at")
    private Date updatedAt;

    public int getCBID() {
        return CBID;
    }

    public void setCBID(int CBID) {
        this.CBID = CBID;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getInitial() {
        return initial;
    }

    public void setInitial(String initial) {
        this.initial = initial;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPIC() {
        return PIC;
    }

    public void setPIC(String PIC) {
        this.PIC = PIC;
    }

    public String getPICPhoneNumber() {
        return PICPhoneNumber;
    }

    public void setPICPhoneNumber(String PICPhoneNumber) {
        this.PICPhoneNumber = PICPhoneNumber;
    }

    public String getPICEmail() {
        return PICEmail;
    }

    public void setPICEmail(String PICEmail) {
        this.PICEmail = PICEmail;
    }

    public int getKW() {
        return KW;
    }

    public void setKW(int KW) {
        this.KW = KW;
    }

    public int getSLJJ() {
        return SLJJ;
    }

    public void setSLJJ(int SLJJ) {
        this.SLJJ = SLJJ;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getRegencyID() {
        return regencyID;
    }

    public void setRegencyID(int regencyID) {
        this.regencyID = regencyID;
    }

    public int getProvinceID() {
        return provinceID;
    }

    public void setProvinceID(int provinceID) {
        this.provinceID = provinceID;
    }

    public int getPostCode() {
        return postCode;
    }

    public void setPostCode(int postCode) {
        this.postCode = postCode;
    }

    public String getOfficePhoneNumber() {
        return officePhoneNumber;
    }

    public void setOfficePhoneNumber(String officePhoneNumber) {
        this.officePhoneNumber = officePhoneNumber;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public int getCoordinatorID() {
        return coordinatorID;
    }

    public void setCoordinatorID(int coordinatorID) {
        this.coordinatorID = coordinatorID;
    }

    public int getTeknisiID() {
        return teknisiID;
    }

    public void setTeknisiID(int teknisiID) {
        this.teknisiID = teknisiID;
    }

    public int getSalesID() {
        return salesID;
    }

    public void setSalesID(int salesID) {
        this.salesID = salesID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRememberToken() {
        return rememberToken;
    }

    public void setRememberToken(String rememberToken) {
        this.rememberToken = rememberToken;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}

class Technician {
    @ColumnInfo(name = "id")
    private int TID;

    private String username;
    private String name;
    private Date dob;
    private String email;

    @ColumnInfo(name = "api_token")
    private String apiToken;

    @ColumnInfo(name = "role_id")
    private String roleID;

    @ColumnInfo(name = "branch_id")
    private String branchID;

    @ColumnInfo(name = "superior_id")
    private String superiorID;

    @ColumnInfo(name = "created_at")
    private String createdAt;

    @ColumnInfo(name = "updated_at")
    private String updatedAt;

    public int getTID() {
        return TID;
    }

    public void setTID(int TID) {
        this.TID = TID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getApiToken() {
        return apiToken;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }

    public String getRoleID() {
        return roleID;
    }

    public void setRoleID(String roleID) {
        this.roleID = roleID;
    }

    public String getBranchID() {
        return branchID;
    }

    public void setBranchID(String branchID) {
        this.branchID = branchID;
    }

    public String getSuperiorID() {
        return superiorID;
    }

    public void setSuperiorID(String superiorID) {
        this.superiorID = superiorID;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}

@Entity(tableName = "service")
public class Service {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "date_service")
    private Date dateService;

    @ColumnInfo(name = "type_service")
    private int typeService;

    @Embedded(prefix = "cb_")
    private CustomerBranch custBranch;

    @Embedded(prefix = "t_")
    private Technician technician;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDateService() {
        return dateService;
    }

    public void setDateService(Date dateService) {
        this.dateService = dateService;
    }

    public CustomerBranch getCustBranch() {
        return custBranch;
    }

    public void setCustBranch(CustomerBranch custBranch) {
        this.custBranch = custBranch;
    }

    public Technician getTechnician() {
        return technician;
    }

    public void setTechnician(Technician technician) {
        this.technician = technician;
    }
}
