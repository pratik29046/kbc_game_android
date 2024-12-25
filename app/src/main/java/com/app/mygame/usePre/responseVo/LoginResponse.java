package com.app.mygame.usePre.responseVo;

import com.app.mygame.utils.BaseResponse;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class LoginResponse extends BaseResponse {
    public Data data;
    @lombok.Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Data{
        public int id;
        public int userId;
        public String userDevice;
        public String userNotificationId;
        public String deviceType;
        public String userMobileNumber;
        public String userDeviceModel;
        public boolean userIsActiveStatus;
        public String token;
    }
}
