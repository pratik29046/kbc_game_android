package com.app.mygame.usePre.responseVo;

import com.app.mygame.utils.BaseResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ProfileResponse extends BaseResponse {

    public Data data;

    @lombok.Data
    @NoArgsConstructor
    @Builder
    public static class Data{
        public int userId;
        public String userName;
        public String userLastName;
        public String userAddress;
        public String userAdhar;
        public int userAge;
        public String userPhone;
        public String userGender;
        public String userAccountNo;
        public boolean active;
        public String userEmail;
        public String userPan;
        public String ifscCode;
        public String bankName;
        public String bankAccountNumber;
        public String upi;
        public int otp;

        public Data(int userId, String userName, String userLastName, String userAddress, String userAdhar, int userAge, String userPhone, String userGender, String userAccountNo, boolean active, String userEmail, String userPan, String ifscCode, String bankName, String bankAccountNumber, String upi, int otp) {
            this.userId = userId;
            this.userName = userName;
            this.userLastName = userLastName;
            this.userAddress = userAddress;
            this.userAdhar = userAdhar;
            this.userAge = userAge;
            this.userPhone = userPhone;
            this.userGender = userGender;
            this.userAccountNo = userAccountNo;
            this.active = active;
            this.userEmail = userEmail;
            this.userPan = userPan;
            this.ifscCode = ifscCode;
            this.bankName = bankName;
            this.bankAccountNumber = bankAccountNumber;
            this.upi = upi;
            this.otp = otp;
        }
    }
    
    
}
