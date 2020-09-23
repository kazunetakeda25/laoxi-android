package com.rider.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DriverDetails implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("company")
    @Expose
    private String company;
    @SerializedName("prefixname")
    @Expose
    private String prefixname;
    @SerializedName("fname")
    @Expose
    private String fname;
    @SerializedName("mname")
    @Expose
    private String mname;
    @SerializedName("lname")
    @Expose
    private String lname;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("country_code")
    @Expose
    private String countryCode;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
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
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("postalcode")
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
    @SerializedName("token")
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
    @SerializedName("insertdate")
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
    @SerializedName("bsb")
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
    @SerializedName("is_favourite_driver")
    @Expose
    private boolean isFavouriteDriver;
    @SerializedName("eta")
    @Expose
    private String eta;

    /**
     *
     * @return
     * The id
     */
    public String getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The company
     */
    public String getCompany() {
        return company;
    }

    /**
     *
     * @param company
     * The company
     */
    public void setCompany(String company) {
        this.company = company;
    }

    /**
     *
     * @return
     * The prefixname
     */
    public String getPrefixname() {
        return prefixname;
    }

    /**
     *
     * @param prefixname
     * The prefixname
     */
    public void setPrefixname(String prefixname) {
        this.prefixname = prefixname;
    }

    /**
     *
     * @return
     * The fname
     */
    public String getFname() {
        return fname;
    }

    /**
     *
     * @param fname
     * The fname
     */
    public void setFname(String fname) {
        this.fname = fname;
    }

    /**
     *
     * @return
     * The mname
     */
    public String getMname() {
        return mname;
    }

    /**
     *
     * @param mname
     * The mname
     */
    public void setMname(String mname) {
        this.mname = mname;
    }

    /**
     *
     * @return
     * The lname
     */
    public String getLname() {
        return lname;
    }

    /**
     *
     * @param lname
     * The lname
     */
    public void setLname(String lname) {
        this.lname = lname;
    }

    /**
     *
     * @return
     * The email
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * @param email
     * The email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     *
     * @return
     * The password
     */
    public String getPassword() {
        return password;
    }

    /**
     *
     * @param password
     * The password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     *
     * @return
     * The countryCode
     */
    public String getCountryCode() {
        return countryCode;
    }

    /**
     *
     * @param countryCode
     * The country_code
     */
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    /**
     *
     * @return
     * The phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     *
     * @param phone
     * The phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     *
     * @return
     * The gender
     */
    public String getGender() {
        return gender;
    }

    /**
     *
     * @param gender
     * The gender
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     *
     * @return
     * The dob
     */
    public String getDob() {
        return dob;
    }

    /**
     *
     * @param dob
     * The dob
     */
    public void setDob(String dob) {
        this.dob = dob;
    }

    /**
     *
     * @return
     * The latitude
     */
    public String getLatitude() {
        return latitude;
    }

    /**
     *
     * @param latitude
     * The latitude
     */
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    /**
     *
     * @return
     * The longitude
     */
    public String getLongitude() {
        return longitude;
    }

    /**
     *
     * @param longitude
     * The longitude
     */
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    /**
     *
     * @return
     * The deviceId
     */
    public String getDeviceId() {
        return deviceId;
    }

    /**
     *
     * @param deviceId
     * The device_id
     */
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    /**
     *
     * @return
     * The deviceType
     */
    public String getDeviceType() {
        return deviceType;
    }

    /**
     *
     * @param deviceType
     * The device_type
     */
    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    /**
     *
     * @return
     * The profileImage
     */
    public String getProfileImage() {
        return profileImage;
    }

    /**
     *
     * @param profileImage
     * The profile_image
     */
    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    /**
     *
     * @return
     * The carType
     */
    public String getCarType() {
        return carType;
    }

    /**
     *
     * @param carType
     * The car_type
     */
    public void setCarType(String carType) {
        this.carType = carType;
    }

    /**
     *
     * @return
     * The carTypeImage
     */
    public String getCarTypeImage() {
        return carTypeImage;
    }

    /**
     *
     * @param carTypeImage
     * The car_type_image
     */
    public void setCarTypeImage(String carTypeImage) {
        this.carTypeImage = carTypeImage;
    }

    /**
     *
     * @return
     * The streetAddress
     */
    public String getStreetAddress() {
        return streetAddress;
    }

    /**
     *
     * @param streetAddress
     * The street_address
     */
    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    /**
     *
     * @return
     * The streetAddressLine2
     */
    public String getStreetAddressLine2() {
        return streetAddressLine2;
    }

    /**
     *
     * @param streetAddressLine2
     * The street_address_line2
     */
    public void setStreetAddressLine2(String streetAddressLine2) {
        this.streetAddressLine2 = streetAddressLine2;
    }

    /**
     *
     * @return
     * The city
     */
    public String getCity() {
        return city;
    }

    /**
     *
     * @param city
     * The city
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     *
     * @return
     * The state
     */
    public String getState() {
        return state;
    }

    /**
     *
     * @param state
     * The state
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     *
     * @return
     * The country
     */
    public String getCountry() {
        return country;
    }

    /**
     *
     * @param country
     * The country
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     *
     * @return
     * The postalcode
     */
    public String getPostalcode() {
        return postalcode;
    }

    /**
     *
     * @param postalcode
     * The postalcode
     */
    public void setPostalcode(String postalcode) {
        this.postalcode = postalcode;
    }

    /**
     *
     * @return
     * The countryOfBirth
     */
    public String getCountryOfBirth() {
        return countryOfBirth;
    }

    /**
     *
     * @param countryOfBirth
     * The country_of_birth
     */
    public void setCountryOfBirth(String countryOfBirth) {
        this.countryOfBirth = countryOfBirth;
    }

    /**
     *
     * @return
     * The ewInAustralia
     */
    public String getEwInAustralia() {
        return ewInAustralia;
    }

    /**
     *
     * @param ewInAustralia
     * The ew_in_australia
     */
    public void setEwInAustralia(String ewInAustralia) {
        this.ewInAustralia = ewInAustralia;
    }

    /**
     *
     * @return
     * The englishProficiency
     */
    public String getEnglishProficiency() {
        return englishProficiency;
    }

    /**
     *
     * @param englishProficiency
     * The english_proficiency
     */
    public void setEnglishProficiency(String englishProficiency) {
        this.englishProficiency = englishProficiency;
    }

    /**
     *
     * @return
     * The upVotes
     */
    public String getUpVotes() {
        return upVotes;
    }

    /**
     *
     * @param upVotes
     * The up_votes
     */
    public void setUpVotes(String upVotes) {
        this.upVotes = upVotes;
    }

    /**
     *
     * @return
     * The downVotes
     */
    public String getDownVotes() {
        return downVotes;
    }

    /**
     *
     * @param downVotes
     * The down_votes
     */
    public void setDownVotes(String downVotes) {
        this.downVotes = downVotes;
    }

    /**
     *
     * @return
     * The token
     */
    public String getToken() {
        return token;
    }

    /**
     *
     * @param token
     * The token
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     *
     * @return
     * The settlementDate
     */
    public String getSettlementDate() {
        return settlementDate;
    }

    /**
     *
     * @param settlementDate
     * The settlement_date
     */
    public void setSettlementDate(String settlementDate) {
        this.settlementDate = settlementDate;
    }

    /**
     *
     * @return
     * The docStatus
     */
    public String getDocStatus() {
        return docStatus;
    }

    /**
     *
     * @param docStatus
     * The doc_status
     */
    public void setDocStatus(String docStatus) {
        this.docStatus = docStatus;
    }

    /**
     *
     * @return
     * The isService
     */
    public String getIsService() {
        return isService;
    }

    /**
     *
     * @param isService
     * The is_service
     */
    public void setIsService(String isService) {
        this.isService = isService;
    }

    /**
     *
     * @return
     * The isFree
     */
    public String getIsFree() {
        return isFree;
    }

    /**
     *
     * @param isFree
     * The is_free
     */
    public void setIsFree(String isFree) {
        this.isFree = isFree;
    }

    /**
     *
     * @return
     * The isLogin
     */
    public String getIsLogin() {
        return isLogin;
    }

    /**
     *
     * @param isLogin
     * The is_login
     */
    public void setIsLogin(String isLogin) {
        this.isLogin = isLogin;
    }

    /**
     *
     * @return
     * The isActive
     */
    public String getIsActive() {
        return isActive;
    }

    /**
     *
     * @param isActive
     * The is_active
     */
    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    /**
     *
     * @return
     * The lastLogin
     */
    public String getLastLogin() {
        return lastLogin;
    }

    /**
     *
     * @param lastLogin
     * The last_login
     */
    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    /**
     *
     * @return
     * The insertdate
     */
    public String getInsertdate() {
        return insertdate;
    }

    /**
     *
     * @param insertdate
     * The insertdate
     */
    public void setInsertdate(String insertdate) {
        this.insertdate = insertdate;
    }

    /**
     *
     * @return
     * The driverId
     */
    public String getDriverId() {
        return driverId;
    }

    /**
     *
     * @param driverId
     * The driver_id
     */
    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    /**
     *
     * @return
     * The uploadsItem
     */
    public String getUploadsItem() {
        return uploadsItem;
    }

    /**
     *
     * @param uploadsItem
     * The uploads_item
     */
    public void setUploadsItem(String uploadsItem) {
        this.uploadsItem = uploadsItem;
    }

    /**
     *
     * @return
     * The haveAudLicence
     */
    public String getHaveAudLicence() {
        return haveAudLicence;
    }

    /**
     *
     * @param haveAudLicence
     * The have_aud_licence
     */
    public void setHaveAudLicence(String haveAudLicence) {
        this.haveAudLicence = haveAudLicence;
    }

    /**
     *
     * @return
     * The licenceNumber
     */
    public String getLicenceNumber() {
        return licenceNumber;
    }

    /**
     *
     * @param licenceNumber
     * The licence_number
     */
    public void setLicenceNumber(String licenceNumber) {
        this.licenceNumber = licenceNumber;
    }

    /**
     *
     * @return
     * The licenceExpiry
     */
    public String getLicenceExpiry() {
        return licenceExpiry;
    }

    /**
     *
     * @param licenceExpiry
     * The licence_expiry
     */
    public void setLicenceExpiry(String licenceExpiry) {
        this.licenceExpiry = licenceExpiry;
    }

    /**
     *
     * @return
     * The uploadLicence
     */
    public String getUploadLicence() {
        return uploadLicence;
    }

    /**
     *
     * @param uploadLicence
     * The upload_licence
     */
    public void setUploadLicence(String uploadLicence) {
        this.uploadLicence = uploadLicence;
    }

    /**
     *
     * @return
     * The uploadCldExtension
     */
    public String getUploadCldExtension() {
        return uploadCldExtension;
    }

    /**
     *
     * @param uploadCldExtension
     * The upload_cld_extension
     */
    public void setUploadCldExtension(String uploadCldExtension) {
        this.uploadCldExtension = uploadCldExtension;
    }

    /**
     *
     * @return
     * The accountName
     */
    public String getAccountName() {
        return accountName;
    }

    /**
     *
     * @param accountName
     * The account_name
     */
    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    /**
     *
     * @return
     * The bankName
     */
    public String getBankName() {
        return bankName;
    }

    /**
     *
     * @param bankName
     * The bank_name
     */
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    /**
     *
     * @return
     * The bsb
     */
    public String getBsb() {
        return bsb;
    }

    /**
     *
     * @param bsb
     * The bsb
     */
    public void setBsb(String bsb) {
        this.bsb = bsb;
    }

    /**
     *
     * @return
     * The bankAccount
     */
    public String getBankAccount() {
        return bankAccount;
    }

    /**
     *
     * @param bankAccount
     * The bank_account
     */
    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    /**
     *
     * @return
     * The vehicleRegNumber
     */
    public String getVehicleRegNumber() {
        return vehicleRegNumber;
    }

    /**
     *
     * @param vehicleRegNumber
     * The vehicle_reg_number
     */
    public void setVehicleRegNumber(String vehicleRegNumber) {
        this.vehicleRegNumber = vehicleRegNumber;
    }

    /**
     *
     * @return
     * The vehicleMake
     */
    public String getVehicleMake() {
        return vehicleMake;
    }

    /**
     *
     * @param vehicleMake
     * The vehicle_make
     */
    public void setVehicleMake(String vehicleMake) {
        this.vehicleMake = vehicleMake;
    }

    /**
     *
     * @return
     * The vehicleModel
     */
    public String getVehicleModel() {
        return vehicleModel;
    }

    /**
     *
     * @param vehicleModel
     * The vehicle_model
     */
    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    /**
     *
     * @return
     * The vehicleYear
     */
    public String getVehicleYear() {
        return vehicleYear;
    }

    /**
     *
     * @param vehicleYear
     * The vehicle_year
     */
    public void setVehicleYear(String vehicleYear) {
        this.vehicleYear = vehicleYear;
    }

    /**
     *
     * @return
     * The uploadInsCertificate
     */
    public String getUploadInsCertificate() {
        return uploadInsCertificate;
    }

    /**
     *
     * @param uploadInsCertificate
     * The upload_ins_certificate
     */
    public void setUploadInsCertificate(String uploadInsCertificate) {
        this.uploadInsCertificate = uploadInsCertificate;
    }

    /**
     *
     * @return
     * The uploadVehicleCertification
     */
    public String getUploadVehicleCertification() {
        return uploadVehicleCertification;
    }

    /**
     *
     * @param uploadVehicleCertification
     * The upload_vehicle_certification
     */
    public void setUploadVehicleCertification(String uploadVehicleCertification) {
        this.uploadVehicleCertification = uploadVehicleCertification;
    }

    /**
     *
     * @return
     * The abnNumber
     */
    public String getAbnNumber() {
        return abnNumber;
    }

    /**
     *
     * @param abnNumber
     * The abn_number
     */
    public void setAbnNumber(String abnNumber) {
        this.abnNumber = abnNumber;
    }

    /**
     *
     * @return
     * The isFavouriteDriver
     */
    public boolean isFavouriteDriver() {
        return this.isFavouriteDriver;
    }

    /**
     *
     * @param isFavouriteDriver
     * The is_favourite_driver
     */
    public void setFavouriteDriver(boolean isFavouriteDriver) {
        this.isFavouriteDriver = isFavouriteDriver;
    }

    /**
     * @return The eta
     */
    public String getEta() {
        return eta;
    }

    /**
     * @param eta The eta
     */
    public void setEta(String eta) {
        this.eta = eta;
    }

}
