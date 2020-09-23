
package com.driver.pojo.TrackRidePojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DriverData {

    @Expose
    private String id;
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
    @Expose
    private String passport;
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
    private String zipcode;
    @SerializedName("up_votes")
    @Expose
    private String upVotes;
    @SerializedName("down_votes")
    @Expose
    private String downVotes;
    @Expose
    private String token;
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
    @SerializedName("another_name")
    @Expose
    private String anotherName;
    @SerializedName("another_fname")
    @Expose
    private String anotherFname;
    @SerializedName("another_lname")
    @Expose
    private String anotherLname;
    @SerializedName("work_in_australia")
    @Expose
    private String workInAustralia;
    @SerializedName("years_of_lived_here")
    @Expose
    private String yearsOfLivedHere;
    @SerializedName("previous_street_address")
    @Expose
    private String previousStreetAddress;
    @SerializedName("previous_street_address_line2")
    @Expose
    private String previousStreetAddressLine2;
    @SerializedName("previous_city")
    @Expose
    private String previousCity;
    @SerializedName("previous_state")
    @Expose
    private String previousState;
    @SerializedName("previous_country")
    @Expose
    private String previousCountry;
    @SerializedName("previous_zipcode")
    @Expose
    private String previousZipcode;
    @SerializedName("have_a_licence")
    @Expose
    private String haveALicence;
    @SerializedName("years_of_full_licence")
    @Expose
    private String yearsOfFullLicence;
    @SerializedName("licence_number")
    @Expose
    private String licenceNumber;
    @SerializedName("licence_expiry")
    @Expose
    private String licenceExpiry;
    @SerializedName("licence_copy")
    @Expose
    private String licenceCopy;
    @SerializedName("have_a_licence_extension")
    @Expose
    private String haveALicenceExtension;
    @SerializedName("vehicle_worthiness_copy")
    @Expose
    private String vehicleWorthinessCopy;
    @SerializedName("national_police_clearance")
    @Expose
    private String nationalPoliceClearance;
    @SerializedName("driving_medical_certificate")
    @Expose
    private String drivingMedicalCertificate;
    @SerializedName("vehicle_registration_number")
    @Expose
    private String vehicleRegistrationNumber;
    @SerializedName("vehicle_year")
    @Expose
    private String vehicleYear;
    @SerializedName("vehicle_make")
    @Expose
    private String vehicleMake;
    @SerializedName("vehicle_model")
    @Expose
    private String vehicleModel;
    @SerializedName("vehicle_photo")
    @Expose
    private String vehiclePhoto;
    @SerializedName("insuredto_carry_passengers")
    @Expose
    private String insuredtoCarryPassengers;
    @SerializedName("have_accident")
    @Expose
    private String haveAccident;
    @SerializedName("insurance_copy")
    @Expose
    private String insuranceCopy;
    @Expose
    private String about;
    @SerializedName("driverpartner_on_anotherapp")
    @Expose
    private String driverpartnerOnAnotherapp;
    @SerializedName("licence_extension_copy")
    @Expose
    private String licenceExtensionCopy;
    @SerializedName("registered_before")
    @Expose
    private String registeredBefore;
    @SerializedName("is_verified")
    @Expose
    private String isVerified;
    @SerializedName("is_favourite_driver")
    @Expose
    private boolean isFavouriteDriver;

    /**
     * 
     * @return
     *     The id
     */
    public String getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 
     * @return
     *     The prefixname
     */
    public String getPrefixname() {
        return prefixname;
    }

    /**
     * 
     * @param prefixname
     *     The prefixname
     */
    public void setPrefixname(String prefixname) {
        this.prefixname = prefixname;
    }

    /**
     * 
     * @return
     *     The fname
     */
    public String getFname() {
        return fname;
    }

    /**
     * 
     * @param fname
     *     The fname
     */
    public void setFname(String fname) {
        this.fname = fname;
    }

    /**
     * 
     * @return
     *     The mname
     */
    public String getMname() {
        return mname;
    }

    /**
     * 
     * @param mname
     *     The mname
     */
    public void setMname(String mname) {
        this.mname = mname;
    }

    /**
     * 
     * @return
     *     The lname
     */
    public String getLname() {
        return lname;
    }

    /**
     * 
     * @param lname
     *     The lname
     */
    public void setLname(String lname) {
        this.lname = lname;
    }

    /**
     * 
     * @return
     *     The email
     */
    public String getEmail() {
        return email;
    }

    /**
     * 
     * @param email
     *     The email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * 
     * @return
     *     The password
     */
    public String getPassword() {
        return password;
    }

    /**
     * 
     * @param password
     *     The password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 
     * @return
     *     The countryCode
     */
    public String getCountryCode() {
        return countryCode;
    }

    /**
     * 
     * @param countryCode
     *     The country_code
     */
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    /**
     * 
     * @return
     *     The phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 
     * @param phone
     *     The phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * 
     * @return
     *     The gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * 
     * @param gender
     *     The gender
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * 
     * @return
     *     The dob
     */
    public String getDob() {
        return dob;
    }

    /**
     * 
     * @param dob
     *     The dob
     */
    public void setDob(String dob) {
        this.dob = dob;
    }

    /**
     * 
     * @return
     *     The latitude
     */
    public String getLatitude() {
        return latitude;
    }

    /**
     * 
     * @param latitude
     *     The latitude
     */
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    /**
     * 
     * @return
     *     The longitude
     */
    public String getLongitude() {
        return longitude;
    }

    /**
     * 
     * @param longitude
     *     The longitude
     */
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    /**
     * 
     * @return
     *     The deviceId
     */
    public String getDeviceId() {
        return deviceId;
    }

    /**
     * 
     * @param deviceId
     *     The device_id
     */
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    /**
     * 
     * @return
     *     The deviceType
     */
    public String getDeviceType() {
        return deviceType;
    }

    /**
     * 
     * @param deviceType
     *     The device_type
     */
    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    /**
     * 
     * @return
     *     The profileImage
     */
    public String getProfileImage() {
        return profileImage;
    }

    /**
     * 
     * @param profileImage
     *     The profile_image
     */
    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    /**
     * 
     * @return
     *     The carType
     */
    public String getCarType() {
        return carType;
    }

    /**
     * 
     * @param carType
     *     The car_type
     */
    public void setCarType(String carType) {
        this.carType = carType;
    }

    /**
     * 
     * @return
     *     The passport
     */
    public String getPassport() {
        return passport;
    }

    /**
     * 
     * @param passport
     *     The passport
     */
    public void setPassport(String passport) {
        this.passport = passport;
    }

    /**
     * 
     * @return
     *     The streetAddress
     */
    public String getStreetAddress() {
        return streetAddress;
    }

    /**
     * 
     * @param streetAddress
     *     The street_address
     */
    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    /**
     * 
     * @return
     *     The streetAddressLine2
     */
    public String getStreetAddressLine2() {
        return streetAddressLine2;
    }

    /**
     * 
     * @param streetAddressLine2
     *     The street_address_line2
     */
    public void setStreetAddressLine2(String streetAddressLine2) {
        this.streetAddressLine2 = streetAddressLine2;
    }

    /**
     * 
     * @return
     *     The city
     */
    public String getCity() {
        return city;
    }

    /**
     * 
     * @param city
     *     The city
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * 
     * @return
     *     The state
     */
    public String getState() {
        return state;
    }

    /**
     * 
     * @param state
     *     The state
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * 
     * @return
     *     The country
     */
    public String getCountry() {
        return country;
    }

    /**
     * 
     * @param country
     *     The country
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * 
     * @return
     *     The zipcode
     */
    public String getZipcode() {
        return zipcode;
    }

    /**
     * 
     * @param zipcode
     *     The zipcode
     */
    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    /**
     * 
     * @return
     *     The upVotes
     */
    public String getUpVotes() {
        return upVotes;
    }

    /**
     * 
     * @param upVotes
     *     The up_votes
     */
    public void setUpVotes(String upVotes) {
        this.upVotes = upVotes;
    }

    /**
     * 
     * @return
     *     The downVotes
     */
    public String getDownVotes() {
        return downVotes;
    }

    /**
     * 
     * @param downVotes
     *     The down_votes
     */
    public void setDownVotes(String downVotes) {
        this.downVotes = downVotes;
    }

    /**
     * 
     * @return
     *     The token
     */
    public String getToken() {
        return token;
    }

    /**
     * 
     * @param token
     *     The token
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * 
     * @return
     *     The isService
     */
    public String getIsService() {
        return isService;
    }

    /**
     * 
     * @param isService
     *     The is_service
     */
    public void setIsService(String isService) {
        this.isService = isService;
    }

    /**
     * 
     * @return
     *     The isFree
     */
    public String getIsFree() {
        return isFree;
    }

    /**
     * 
     * @param isFree
     *     The is_free
     */
    public void setIsFree(String isFree) {
        this.isFree = isFree;
    }

    /**
     * 
     * @return
     *     The isLogin
     */
    public String getIsLogin() {
        return isLogin;
    }

    /**
     * 
     * @param isLogin
     *     The is_login
     */
    public void setIsLogin(String isLogin) {
        this.isLogin = isLogin;
    }

    /**
     * 
     * @return
     *     The isActive
     */
    public String getIsActive() {
        return isActive;
    }

    /**
     * 
     * @param isActive
     *     The is_active
     */
    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    /**
     * 
     * @return
     *     The lastLogin
     */
    public String getLastLogin() {
        return lastLogin;
    }

    /**
     * 
     * @param lastLogin
     *     The last_login
     */
    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    /**
     * 
     * @return
     *     The insertdate
     */
    public String getInsertdate() {
        return insertdate;
    }

    /**
     * 
     * @param insertdate
     *     The insertdate
     */
    public void setInsertdate(String insertdate) {
        this.insertdate = insertdate;
    }

    /**
     * 
     * @return
     *     The driverId
     */
    public String getDriverId() {
        return driverId;
    }

    /**
     * 
     * @param driverId
     *     The driver_id
     */
    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    /**
     * 
     * @return
     *     The uploadsItem
     */
    public String getUploadsItem() {
        return uploadsItem;
    }

    /**
     * 
     * @param uploadsItem
     *     The uploads_item
     */
    public void setUploadsItem(String uploadsItem) {
        this.uploadsItem = uploadsItem;
    }

    /**
     * 
     * @return
     *     The anotherName
     */
    public String getAnotherName() {
        return anotherName;
    }

    /**
     * 
     * @param anotherName
     *     The another_name
     */
    public void setAnotherName(String anotherName) {
        this.anotherName = anotherName;
    }

    /**
     * 
     * @return
     *     The anotherFname
     */
    public String getAnotherFname() {
        return anotherFname;
    }

    /**
     * 
     * @param anotherFname
     *     The another_fname
     */
    public void setAnotherFname(String anotherFname) {
        this.anotherFname = anotherFname;
    }

    /**
     * 
     * @return
     *     The anotherLname
     */
    public String getAnotherLname() {
        return anotherLname;
    }

    /**
     * 
     * @param anotherLname
     *     The another_lname
     */
    public void setAnotherLname(String anotherLname) {
        this.anotherLname = anotherLname;
    }

    /**
     * 
     * @return
     *     The workInAustralia
     */
    public String getWorkInAustralia() {
        return workInAustralia;
    }

    /**
     * 
     * @param workInAustralia
     *     The work_in_australia
     */
    public void setWorkInAustralia(String workInAustralia) {
        this.workInAustralia = workInAustralia;
    }

    /**
     * 
     * @return
     *     The yearsOfLivedHere
     */
    public String getYearsOfLivedHere() {
        return yearsOfLivedHere;
    }

    /**
     * 
     * @param yearsOfLivedHere
     *     The years_of_lived_here
     */
    public void setYearsOfLivedHere(String yearsOfLivedHere) {
        this.yearsOfLivedHere = yearsOfLivedHere;
    }

    /**
     * 
     * @return
     *     The previousStreetAddress
     */
    public String getPreviousStreetAddress() {
        return previousStreetAddress;
    }

    /**
     * 
     * @param previousStreetAddress
     *     The previous_street_address
     */
    public void setPreviousStreetAddress(String previousStreetAddress) {
        this.previousStreetAddress = previousStreetAddress;
    }

    /**
     * 
     * @return
     *     The previousStreetAddressLine2
     */
    public String getPreviousStreetAddressLine2() {
        return previousStreetAddressLine2;
    }

    /**
     * 
     * @param previousStreetAddressLine2
     *     The previous_street_address_line2
     */
    public void setPreviousStreetAddressLine2(String previousStreetAddressLine2) {
        this.previousStreetAddressLine2 = previousStreetAddressLine2;
    }

    /**
     * 
     * @return
     *     The previousCity
     */
    public String getPreviousCity() {
        return previousCity;
    }

    /**
     * 
     * @param previousCity
     *     The previous_city
     */
    public void setPreviousCity(String previousCity) {
        this.previousCity = previousCity;
    }

    /**
     * 
     * @return
     *     The previousState
     */
    public String getPreviousState() {
        return previousState;
    }

    /**
     * 
     * @param previousState
     *     The previous_state
     */
    public void setPreviousState(String previousState) {
        this.previousState = previousState;
    }

    /**
     * 
     * @return
     *     The previousCountry
     */
    public String getPreviousCountry() {
        return previousCountry;
    }

    /**
     * 
     * @param previousCountry
     *     The previous_country
     */
    public void setPreviousCountry(String previousCountry) {
        this.previousCountry = previousCountry;
    }

    /**
     * 
     * @return
     *     The previousZipcode
     */
    public String getPreviousZipcode() {
        return previousZipcode;
    }

    /**
     * 
     * @param previousZipcode
     *     The previous_zipcode
     */
    public void setPreviousZipcode(String previousZipcode) {
        this.previousZipcode = previousZipcode;
    }

    /**
     * 
     * @return
     *     The haveALicence
     */
    public String getHaveALicence() {
        return haveALicence;
    }

    /**
     * 
     * @param haveALicence
     *     The have_a_licence
     */
    public void setHaveALicence(String haveALicence) {
        this.haveALicence = haveALicence;
    }

    /**
     * 
     * @return
     *     The yearsOfFullLicence
     */
    public String getYearsOfFullLicence() {
        return yearsOfFullLicence;
    }

    /**
     * 
     * @param yearsOfFullLicence
     *     The years_of_full_licence
     */
    public void setYearsOfFullLicence(String yearsOfFullLicence) {
        this.yearsOfFullLicence = yearsOfFullLicence;
    }

    /**
     * 
     * @return
     *     The licenceNumber
     */
    public String getLicenceNumber() {
        return licenceNumber;
    }

    /**
     * 
     * @param licenceNumber
     *     The licence_number
     */
    public void setLicenceNumber(String licenceNumber) {
        this.licenceNumber = licenceNumber;
    }

    /**
     * 
     * @return
     *     The licenceExpiry
     */
    public String getLicenceExpiry() {
        return licenceExpiry;
    }

    /**
     * 
     * @param licenceExpiry
     *     The licence_expiry
     */
    public void setLicenceExpiry(String licenceExpiry) {
        this.licenceExpiry = licenceExpiry;
    }

    /**
     * 
     * @return
     *     The licenceCopy
     */
    public String getLicenceCopy() {
        return licenceCopy;
    }

    /**
     * 
     * @param licenceCopy
     *     The licence_copy
     */
    public void setLicenceCopy(String licenceCopy) {
        this.licenceCopy = licenceCopy;
    }

    /**
     * 
     * @return
     *     The haveALicenceExtension
     */
    public String getHaveALicenceExtension() {
        return haveALicenceExtension;
    }

    /**
     * 
     * @param haveALicenceExtension
     *     The have_a_licence_extension
     */
    public void setHaveALicenceExtension(String haveALicenceExtension) {
        this.haveALicenceExtension = haveALicenceExtension;
    }

    /**
     * 
     * @return
     *     The vehicleWorthinessCopy
     */
    public String getVehicleWorthinessCopy() {
        return vehicleWorthinessCopy;
    }

    /**
     * 
     * @param vehicleWorthinessCopy
     *     The vehicle_worthiness_copy
     */
    public void setVehicleWorthinessCopy(String vehicleWorthinessCopy) {
        this.vehicleWorthinessCopy = vehicleWorthinessCopy;
    }

    /**
     * 
     * @return
     *     The nationalPoliceClearance
     */
    public String getNationalPoliceClearance() {
        return nationalPoliceClearance;
    }

    /**
     * 
     * @param nationalPoliceClearance
     *     The national_police_clearance
     */
    public void setNationalPoliceClearance(String nationalPoliceClearance) {
        this.nationalPoliceClearance = nationalPoliceClearance;
    }

    /**
     * 
     * @return
     *     The drivingMedicalCertificate
     */
    public String getDrivingMedicalCertificate() {
        return drivingMedicalCertificate;
    }

    /**
     * 
     * @param drivingMedicalCertificate
     *     The driving_medical_certificate
     */
    public void setDrivingMedicalCertificate(String drivingMedicalCertificate) {
        this.drivingMedicalCertificate = drivingMedicalCertificate;
    }

    /**
     * 
     * @return
     *     The vehicleRegistrationNumber
     */
    public String getVehicleRegistrationNumber() {
        return vehicleRegistrationNumber;
    }

    /**
     * 
     * @param vehicleRegistrationNumber
     *     The vehicle_registration_number
     */
    public void setVehicleRegistrationNumber(String vehicleRegistrationNumber) {
        this.vehicleRegistrationNumber = vehicleRegistrationNumber;
    }

    /**
     * 
     * @return
     *     The vehicleYear
     */
    public String getVehicleYear() {
        return vehicleYear;
    }

    /**
     * 
     * @param vehicleYear
     *     The vehicle_year
     */
    public void setVehicleYear(String vehicleYear) {
        this.vehicleYear = vehicleYear;
    }

    /**
     * 
     * @return
     *     The vehicleMake
     */
    public String getVehicleMake() {
        return vehicleMake;
    }

    /**
     * 
     * @param vehicleMake
     *     The vehicle_make
     */
    public void setVehicleMake(String vehicleMake) {
        this.vehicleMake = vehicleMake;
    }

    /**
     * 
     * @return
     *     The vehicleModel
     */
    public String getVehicleModel() {
        return vehicleModel;
    }

    /**
     * 
     * @param vehicleModel
     *     The vehicle_model
     */
    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    /**
     * 
     * @return
     *     The vehiclePhoto
     */
    public String getVehiclePhoto() {
        return vehiclePhoto;
    }

    /**
     * 
     * @param vehiclePhoto
     *     The vehicle_photo
     */
    public void setVehiclePhoto(String vehiclePhoto) {
        this.vehiclePhoto = vehiclePhoto;
    }

    /**
     * 
     * @return
     *     The insuredtoCarryPassengers
     */
    public String getInsuredtoCarryPassengers() {
        return insuredtoCarryPassengers;
    }

    /**
     * 
     * @param insuredtoCarryPassengers
     *     The insuredto_carry_passengers
     */
    public void setInsuredtoCarryPassengers(String insuredtoCarryPassengers) {
        this.insuredtoCarryPassengers = insuredtoCarryPassengers;
    }

    /**
     * 
     * @return
     *     The haveAccident
     */
    public String getHaveAccident() {
        return haveAccident;
    }

    /**
     * 
     * @param haveAccident
     *     The have_accident
     */
    public void setHaveAccident(String haveAccident) {
        this.haveAccident = haveAccident;
    }

    /**
     * 
     * @return
     *     The insuranceCopy
     */
    public String getInsuranceCopy() {
        return insuranceCopy;
    }

    /**
     * 
     * @param insuranceCopy
     *     The insurance_copy
     */
    public void setInsuranceCopy(String insuranceCopy) {
        this.insuranceCopy = insuranceCopy;
    }

    /**
     * 
     * @return
     *     The about
     */
    public String getAbout() {
        return about;
    }

    /**
     * 
     * @param about
     *     The about
     */
    public void setAbout(String about) {
        this.about = about;
    }

    /**
     * 
     * @return
     *     The driverpartnerOnAnotherapp
     */
    public String getDriverpartnerOnAnotherapp() {
        return driverpartnerOnAnotherapp;
    }

    /**
     * 
     * @param driverpartnerOnAnotherapp
     *     The driverpartner_on_anotherapp
     */
    public void setDriverpartnerOnAnotherapp(String driverpartnerOnAnotherapp) {
        this.driverpartnerOnAnotherapp = driverpartnerOnAnotherapp;
    }

    /**
     * 
     * @return
     *     The licenceExtensionCopy
     */
    public String getLicenceExtensionCopy() {
        return licenceExtensionCopy;
    }

    /**
     * 
     * @param licenceExtensionCopy
     *     The licence_extension_copy
     */
    public void setLicenceExtensionCopy(String licenceExtensionCopy) {
        this.licenceExtensionCopy = licenceExtensionCopy;
    }

    /**
     * 
     * @return
     *     The registeredBefore
     */
    public String getRegisteredBefore() {
        return registeredBefore;
    }

    /**
     * 
     * @param registeredBefore
     *     The registered_before
     */
    public void setRegisteredBefore(String registeredBefore) {
        this.registeredBefore = registeredBefore;
    }

    /**
     * 
     * @return
     *     The isVerified
     */
    public String getIsVerified() {
        return isVerified;
    }

    /**
     * 
     * @param isVerified
     *     The is_verified
     */
    public void setIsVerified(String isVerified) {
        this.isVerified = isVerified;
    }

    /**
     *
     * @return
     *     The isFavouriteDriver
     */
    public boolean isFavouriteDriver() {
        return isFavouriteDriver;
    }

    /**
     *
     * @param isFavouriteDriver
     *     The is_favourite_driver
     */
    public void setFavouriteDriver(boolean isFavouriteDriver) {
        this.isFavouriteDriver = isFavouriteDriver;
    }


}
