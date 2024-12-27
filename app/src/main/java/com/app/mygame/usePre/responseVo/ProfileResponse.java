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
    @AllArgsConstructor
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
    }
}
