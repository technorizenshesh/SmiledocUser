package com.smiledocuser.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BidsModel {

    @SerializedName("result")
    @Expose
    private List<Result> result = null;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public class Result {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("user_id")
        @Expose
        private String userId;
        @SerializedName("doctor_id")
        @Expose
        private String doctorId;
        @SerializedName("booking_date")
        @Expose
        private String bookingDate;
        @SerializedName("booking_time")
        @Expose
        private String bookingTime;
        @SerializedName("amount")
        @Expose
        private String amount;
        @SerializedName("date_time")
        @Expose
        private String dateTime;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("age")
        @Expose
        private String age;
        @SerializedName("gender")
        @Expose
        private String gender;
        @SerializedName("number")
        @Expose
        private String number;
        @SerializedName("problem")
        @Expose
        private String problem;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("payment_type")
        @Expose
        private String paymentType;
        @SerializedName("doctor_detial")
        @Expose
        private DoctorDetial doctorDetial;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getDoctorId() {
            return doctorId;
        }

        public void setDoctorId(String doctorId) {
            this.doctorId = doctorId;
        }

        public String getBookingDate() {
            return bookingDate;
        }

        public void setBookingDate(String bookingDate) {
            this.bookingDate = bookingDate;
        }

        public String getBookingTime() {
            return bookingTime;
        }

        public void setBookingTime(String bookingTime) {
            this.bookingTime = bookingTime;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getDateTime() {
            return dateTime;
        }

        public void setDateTime(String dateTime) {
            this.dateTime = dateTime;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getProblem() {
            return problem;
        }

        public void setProblem(String problem) {
            this.problem = problem;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getPaymentType() {
            return paymentType;
        }

        public void setPaymentType(String paymentType) {
            this.paymentType = paymentType;
        }

        public DoctorDetial getDoctorDetial() {
            return doctorDetial;
        }

        public void setDoctorDetial(DoctorDetial doctorDetial) {
            this.doctorDetial = doctorDetial;
        }


        public class DoctorDetial {

            @SerializedName("id")
            @Expose
            private String id;
            @SerializedName("first_name")
            @Expose
            private String firstName;
            @SerializedName("last_name")
            @Expose
            private String lastName;
            @SerializedName("mobile")
            @Expose
            private String mobile;
            @SerializedName("password")
            @Expose
            private String password;
            @SerializedName("type")
            @Expose
            private String type;
            @SerializedName("email")
            @Expose
            private String email;
            @SerializedName("image")
            @Expose
            private String image;
            @SerializedName("register_id")
            @Expose
            private String registerId;
            @SerializedName("social_id")
            @Expose
            private String socialId;
            @SerializedName("address")
            @Expose
            private String address;
            @SerializedName("lat")
            @Expose
            private String lat;
            @SerializedName("lon")
            @Expose
            private String lon;
            @SerializedName("status")
            @Expose
            private String status;
            @SerializedName("date_time")
            @Expose
            private String dateTime;
            @SerializedName("gender")
            @Expose
            private String gender;
            @SerializedName("zipcode")
            @Expose
            private String zipcode;
            @SerializedName("fees")
            @Expose
            private String fees;
            @SerializedName("dob")
            @Expose
            private String dob;
            @SerializedName("attachment1")
            @Expose
            private String attachment1;
            @SerializedName("attachment2")
            @Expose
            private String attachment2;
            @SerializedName("attachment3")
            @Expose
            private String attachment3;
            @SerializedName("attachment4")
            @Expose
            private String attachment4;
            @SerializedName("attachment5")
            @Expose
            private String attachment5;
            @SerializedName("otp")
            @Expose
            private String otp;
            @SerializedName("category_id")
            @Expose
            private String categoryId;
            @SerializedName("sub_category_id")
            @Expose
            private String subCategoryId;
            @SerializedName("consult_type_id")
            @Expose
            private String consultTypeId;
            @SerializedName("education_name")
            @Expose
            private String educationName;
            @SerializedName("education_year")
            @Expose
            private String educationYear;
            @SerializedName("open_time")
            @Expose
            private String openTime;
            @SerializedName("close_time")
            @Expose
            private String closeTime;
            @SerializedName("about_us")
            @Expose
            private String aboutUs;
            @SerializedName("experience")
            @Expose
            private String experience;
            @SerializedName("current_working_place")
            @Expose
            private String currentWorkingPlace;
            @SerializedName("current_position")
            @Expose
            private String currentPosition;
            @SerializedName("professional_location")
            @Expose
            private String professionalLocation;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getFirstName() {
                return firstName;
            }

            public void setFirstName(String firstName) {
                this.firstName = firstName;
            }

            public String getLastName() {
                return lastName;
            }

            public void setLastName(String lastName) {
                this.lastName = lastName;
            }

            public String getMobile() {
                return mobile;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
            }

            public String getPassword() {
                return password;
            }

            public void setPassword(String password) {
                this.password = password;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public String getRegisterId() {
                return registerId;
            }

            public void setRegisterId(String registerId) {
                this.registerId = registerId;
            }

            public String getSocialId() {
                return socialId;
            }

            public void setSocialId(String socialId) {
                this.socialId = socialId;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getLat() {
                return lat;
            }

            public void setLat(String lat) {
                this.lat = lat;
            }

            public String getLon() {
                return lon;
            }

            public void setLon(String lon) {
                this.lon = lon;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getDateTime() {
                return dateTime;
            }

            public void setDateTime(String dateTime) {
                this.dateTime = dateTime;
            }

            public String getGender() {
                return gender;
            }

            public void setGender(String gender) {
                this.gender = gender;
            }

            public String getZipcode() {
                return zipcode;
            }

            public void setZipcode(String zipcode) {
                this.zipcode = zipcode;
            }

            public String getFees() {
                return fees;
            }

            public void setFees(String fees) {
                this.fees = fees;
            }

            public String getDob() {
                return dob;
            }

            public void setDob(String dob) {
                this.dob = dob;
            }

            public String getAttachment1() {
                return attachment1;
            }

            public void setAttachment1(String attachment1) {
                this.attachment1 = attachment1;
            }

            public String getAttachment2() {
                return attachment2;
            }

            public void setAttachment2(String attachment2) {
                this.attachment2 = attachment2;
            }

            public String getAttachment3() {
                return attachment3;
            }

            public void setAttachment3(String attachment3) {
                this.attachment3 = attachment3;
            }

            public String getAttachment4() {
                return attachment4;
            }

            public void setAttachment4(String attachment4) {
                this.attachment4 = attachment4;
            }

            public String getAttachment5() {
                return attachment5;
            }

            public void setAttachment5(String attachment5) {
                this.attachment5 = attachment5;
            }

            public String getOtp() {
                return otp;
            }

            public void setOtp(String otp) {
                this.otp = otp;
            }

            public String getCategoryId() {
                return categoryId;
            }

            public void setCategoryId(String categoryId) {
                this.categoryId = categoryId;
            }

            public String getSubCategoryId() {
                return subCategoryId;
            }

            public void setSubCategoryId(String subCategoryId) {
                this.subCategoryId = subCategoryId;
            }

            public String getConsultTypeId() {
                return consultTypeId;
            }

            public void setConsultTypeId(String consultTypeId) {
                this.consultTypeId = consultTypeId;
            }

            public String getEducationName() {
                return educationName;
            }

            public void setEducationName(String educationName) {
                this.educationName = educationName;
            }

            public String getEducationYear() {
                return educationYear;
            }

            public void setEducationYear(String educationYear) {
                this.educationYear = educationYear;
            }

            public String getOpenTime() {
                return openTime;
            }

            public void setOpenTime(String openTime) {
                this.openTime = openTime;
            }

            public String getCloseTime() {
                return closeTime;
            }

            public void setCloseTime(String closeTime) {
                this.closeTime = closeTime;
            }

            public String getAboutUs() {
                return aboutUs;
            }

            public void setAboutUs(String aboutUs) {
                this.aboutUs = aboutUs;
            }

            public String getExperience() {
                return experience;
            }

            public void setExperience(String experience) {
                this.experience = experience;
            }

            public String getCurrentWorkingPlace() {
                return currentWorkingPlace;
            }

            public void setCurrentWorkingPlace(String currentWorkingPlace) {
                this.currentWorkingPlace = currentWorkingPlace;
            }

            public String getCurrentPosition() {
                return currentPosition;
            }

            public void setCurrentPosition(String currentPosition) {
                this.currentPosition = currentPosition;
            }

            public String getProfessionalLocation() {
                return professionalLocation;
            }

            public void setProfessionalLocation(String professionalLocation) {
                this.professionalLocation = professionalLocation;
            }

        }


    }

}




