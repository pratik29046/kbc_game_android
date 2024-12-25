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
public class OtpSuccessResponse extends BaseResponse {

    public Data data;
    @lombok.Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Data{
        public String mobileNo;
        public int otp;
        public int userId;
    }

}
