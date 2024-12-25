package com.app.mygame.usePre.requestVo;

public class SendOtpRequest {
    private String mobileNo;

    private  String otp;

    public SendOtpRequest(String mobileNo, String otp) {
        this.mobileNo = mobileNo;
        this.otp = otp;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}
