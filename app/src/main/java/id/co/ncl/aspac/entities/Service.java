package id.co.ncl.aspac.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;


/**
 * Created by Jonathan Simananda on 29/11/2017.
 */

@Entity(tableName = "service", indices = {@Index("id")})
public class Service {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "date_service")
    private String dateService;

    @ColumnInfo(name = "type_service")
    private int typeService;

    //Customer Branch

    @ColumnInfo(name = "cb_id")
    private int CBID;

    @ColumnInfo(name = "cb_code")
    private String code;

    @ColumnInfo(name = "cb_initial")
    private String initial;

    @ColumnInfo(name = "cb_name")
    private String name;

    @ColumnInfo(name = "cb_status")
    private String status;

    @ColumnInfo(name = "cb_pic")
    private String PIC;

    @ColumnInfo(name = "cb_pic_phone_number")
    private String PICPhoneNumber;

    @ColumnInfo(name = "cb_pic_email")
    private String PICEmail;

    @ColumnInfo(name = "cb_kw")
    private int KW;

    @ColumnInfo(name = "cb_sljj")
    private int SLJJ;
    private String address;

    @ColumnInfo(name = "cb_regency_id")
    private String regencyID;

    @ColumnInfo(name = "cb_province_id")
    private int provinceID;

    @ColumnInfo(name = "cb_post_code")
    private int postCode;

    @ColumnInfo(name = "cb_office_phone_number")
    private String officePhoneNumber;
    private String fax;

    @ColumnInfo(name = "cb_customer_id")
    private int customerID;

    @ColumnInfo(name = "cb_coordinator_id")
    private int coordinatorID;

    @ColumnInfo(name = "cb_teknisi_id")
    private int teknisiID;

    @ColumnInfo(name = "cb_sales_id")
    private int salesID;

    @ColumnInfo(name = "cb_username")
    private String username;

    @ColumnInfo(name = "cb_password")
    private String password;

    @ColumnInfo(name = "cb_remember_token")
    private String rememberToken;

    @ColumnInfo(name = "cb_created_at")
    private String createdAt;

    @ColumnInfo(name = "cb_updated_at")
    private String updatedAt;

    //Technician

    @ColumnInfo(name = "t_id")
    private int TID;

    @ColumnInfo(name = "t_username")
    private String Tusername;

    @ColumnInfo(name = "t_name")
    private String Tname;

    @ColumnInfo(name = "t_dob")
    private String dob;

    @ColumnInfo(name = "t_email")
    private String email;

    @ColumnInfo(name = "t_api_token")
    private String apiToken;

    @ColumnInfo(name = "t_role_id")
    private String roleID;

    @ColumnInfo(name = "t_branch_id")
    private String branchID;

    @ColumnInfo(name = "t_superior_id")
    private String superiorID;

    @ColumnInfo(name = "t_created_at")
    private String TcreatedAt;

    @ColumnInfo(name = "t_updated_at")
    private String TupdatedAt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDateService() {
        return dateService;
    }

    public void setDateService(String dateService) {
        this.dateService = dateService;
    }

    public int getTypeService() {
        return typeService;
    }

    public void setTypeService(int typeService) {
        this.typeService = typeService;
    }

    //Customer Branch

    public int getCBID() {
        return CBID;
    }

    public void setCBID(int CBID) {
        this.CBID = CBID;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
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

    public String getRegencyID() {
        return regencyID;
    }

    public void setRegencyID(String regencyID) {
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

    //Technician

    public int getTID() {
        return TID;
    }

    public void setTID(int TID) {
        this.TID = TID;
    }

    public String getTusername() {
        return Tusername;
    }

    public void setTusername(String tusername) {
        Tusername = tusername;
    }

    public String getTname() {
        return Tname;
    }

    public void setTname(String name) {
        this.Tname = name;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
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

    public String getTcreatedAt() {
        return TcreatedAt;
    }

    public void setTcreatedAt(String createdAt) {
        this.TcreatedAt = createdAt;
    }

    public String getTupdatedAt() {
        return TupdatedAt;
    }

    public void setTupdatedAt(String updatedAt) {
        this.TupdatedAt = updatedAt;
    }

}
