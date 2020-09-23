package com.driver.pojo.loginws;

import com.driver.pojo.CartypeList;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Chirag Solanki on 06/08/2016.
 */
public class LoginResponse {

    @Expose
    private String id;
    @Expose
    private String company;
    @Expose
    private String prefixname;
    @Expose
    private String fname;
    @Expose
    private String mname;
    @Expose
    private String lname;
    @Expose
    private String email;
    @Expose
    private String password;
    @SerializedName("country_code")
    @Expose
    private String countryCode;
    @Expose
    private String phone;
    @Expose
    private String gender;
    @Expose
    private String dob;
    @Expose
    private String latitude;
    @Expose
    private String longitude;
    @SerializedName("device_id")
    @Expose
    private String deviceId;
    @SerializedName("device_type")
    @Expose
    private String deviceType;
    @SerializedName("profile_image")
    @Expose
    private String profileImage;
    @SerializedName("car_type")
    @Expose
    private String carType;
    @SerializedName("car_type_image")
    @Expose
    private String carTypeImage;
    @SerializedName("street_address")
    @Expose
    private String streetAddress;
    @SerializedName("street_address_line2")
    @Expose
    private String streetAddressLine2;
    @Expose
    private String city;
    @Expose
    private String state;
    @Expose
    private String country;
    @Expose
    private String postalcode;
    @SerializedName("country_of_birth")
    @Expose
    private String countryOfBirth;
    @SerializedName("ew_in_australia")
    @Expose
    private String ewInAustralia;
    @SerializedName("english_proficiency")
    @Expose
    private String englishProficiency;
    @SerializedName("up_votes")
    @Expose
    private String upVotes;
    @SerializedName("down_votes")
    @Expose
    private String downVotes;
    @Expose
    private String token;
    @SerializedName("settlement_date")
    @Expose
    private String settlementDate;
    @SerializedName("doc_status")
    @Expose
    private String docStatus;
    @SerializedName("is_service")
    @Expose
    private String isService;
    @SerializedName("is_free")
    @Expose
    private String isFree;
    @SerializedName("is_login")
    @Expose
    private String isLogin;
    @SerializedName("is_active")
    @Expose
    private String isActive;
    @SerializedName("last_login")
    @Expose
    private String lastLogin;
    @Expose
    private String insertdate;
    @SerializedName("driver_id")
    @Expose
    private String driverId;
    @SerializedName("uploads_item")
    @Expose
    private String uploadsItem;
    @SerializedName("have_aud_licence")
    @Expose
    private String haveAudLicence;
    @SerializedName("licence_number")
    @Expose
    private String licenceNumber;
    @SerializedName("licence_expiry")
    @Expose
    private String licenceExpiry;
    @SerializedName("upload_licence")
    @Expose
    private String uploadLicence;
    @SerializedName("upload_cld_extension")
    @Expose
    private String uploadCldExtension;
    @SerializedName("account_name")
    @Expose
    private String accountName;
    @SerializedName("bank_name")
    @Expose
    private String bankName;
    @Expose
    private String bsb;
    @SerializedName("bank_account")
    @Expose
    private String bankAccount;
    @SerializedName("vehicle_reg_number")
    @Expose
    private String vehicleRegNumber;
    @SerializedName("vehicle_make")
    @Expose
    private String vehicleMake;
    @SerializedName("vehicle_model")
    @Expose
    private String vehicleModel;
    @SerializedName("vehicle_year")
    @Expose
    private String vehicleYear;
    @SerializedName("upload_ins_certificate")
    @Expose
    private String uploadInsCertificate;
    @SerializedName("upload_vehicle_certification")
    @Expose
    private String uploadVehicleCertification;
    @SerializedName("abn_number")
    @Expose
    private String abnNumber;
    @SerializedName("car_type_data")
    @Expose
    private CartypeList cartypedata;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPrefixname() {
        return prefixname;
    }

    public void setPrefixname(String prefixname) {
        this.prefixname = prefixname;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getMname() {
        return mname;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getCarTypeImage() {
        return carTypeImage;
    }

    public void setCarTypeImage(String carTypeImage) {
        this.carTypeImage = carTypeImage;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getStreetAddressLine2() {
        return streetAddressLine2;
    }

    public void setStreetAddressLine2(String streetAddressLine2) {
        this.streetAddressLine2 = streetAddressLine2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalcode() {
        return postalcode;
    }

    public void setPostalcode(String postalcode) {
        this.postalcode = postalcode;
    }

    public String getCountryOfBirth() {
        return countryOfBirth;
    }

    public void setCountryOfBirth(String countryOfBirth) {
        this.countryOfBirth = countryOfBirth;
    }

    public String getEwInAustralia() {
        return ewInAustralia;
    }

    public void setEwInAustralia(String ewInAustralia) {
        this.ewInAustralia = ewInAustralia;
    }

    public String getEnglishProficiency() {
        return englishProficiency;
    }

    public void setEnglishProficiency(String englishProficiency) {
        this.englishProficiency = englishProficiency;
    }

    public String getUpVotes() {
        return upVotes;
    }

    public void setUpVotes(String upVotes) {
        this.upVotes = upVotes;
    }

    public String getDownVotes() {
        return downVotes;
    }

    public void setDownVotes(String downVotes) {
        this.downVotes = downVotes;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSettlementDate() {
        return settlementDate;
    }

    public void setSettlementDate(String settlementDate) {
        this.settlementDate = settlementDate;
    }

    public String getDocStatus() {
        return docStatus;
    }

    public void setDocStatus(String docStatus) {
        this.docStatus = docStatus;
    }

    public String getIsService() {
        return isService;
    }

    public void setIsService(String isService) {
        this.isService = isService;
    }

    public String getIsFree() {
        return isFree;
    }

    public void setIsFree(String isFree) {
        this.isFree = isFree;
    }

    public String getIsLogin() {
        return isLogin;
    }

    public void setIsLogin(String isLogin) {
        this.isLogin = isLogin;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getInsertdate() {
        return insertdate;
    }

    public void setInsertdate(String insertdate) {
        this.insertdate = insertdate;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getUploadsItem() {
        return uploadsItem;
    }

    public void setUploadsItem(String uploadsItem) {
        this.uploadsItem = uploadsItem;
    }

    public String getHaveAudLicence() {
        return haveAudLicence;
    }

    public void setHaveAudLicence(String haveAudLicence) {
        this.haveAudLicence = haveAudLicence;
    }

    public String getLicenceNumber() {
        return licenceNumber;
    }

    public void setLicenceNumber(String licenceNumber) {
        this.licenceNumber = licenceNumber;
    }

    public String getLicenceExpiry() {
        return licenceExpiry;
    }

    public void setLicenceExpiry(String licenceExpiry) {
        this.licenceExpiry = licenceExpiry;
    }

    public String getUploadLicence() {
        return uploadLicence;
    }

    public void setUploadLicence(String uploadLicence) {
        this.uploadLicence = uploadLicence;
    }

    public String getUploadCldExtension() {
        return uploadCldExtension;
    }

    public void setUploadCldExtension(String uploadCldExtension) {
        this.uploadCldExtension = uploadCldExtension;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBsb() {
        return bsb;
    }

    public void setBsb(String bsb) {
        this.bsb = bsb;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getVehicleRegNumber() {
        return vehicleRegNumber;
    }

    public void setVehicleRegNumber(String vehicleRegNumber) {
        this.vehicleRegNumber = vehicleRegNumber;
    }

    public String getVehicleMake() {
        return vehicleMake;
    }

    public void setVehicleMake(String vehicleMake) {
        this.vehicleMake = vehicleMake;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public String getVehicleYear() {
        return vehicleYear;
    }

    public void setVehicleYear(String vehicleYear) {
        this.vehicleYear = vehicleYear;
    }

    public String getUploadInsCertificate() {
        return uploadInsCertificate;
    }

    public void setUploadInsCertificate(String uploadInsCertificate) {
        this.uploadInsCertificate = uploadInsCertificate;
    }

    public String getUploadVehicleCertification() {
        return uploadVehicleCertification;
    }

    public void setUploadVehicleCertification(String uploadVehicleCertification) {
        this.uploadVehicleCertification = uploadVehicleCertification;
    }

    public String getAbnNumber() {
        return abnNumber;
    }

    public void setAbnNumber(String abnNumber) {
        this.abnNumber = abnNumber;
    }

    public CartypeList getCartypedata() {
        return cartypedata;
    }

    public void setCartypedata(CartypeList cartypedata) {
        this.cartypedata = cartypedata;
    }
}
