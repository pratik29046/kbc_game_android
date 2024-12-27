package com.app.mygame.userPost.responseVo;

import com.app.mygame.utils.BaseResponse;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BannerResponse extends BaseResponse {

    public List<Data> data;  // Changed from Data to List<Data>

    @lombok.Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Data {
        public int bannerId;
        public String name;
        public String description;
        public String imageUrl;
        public String reDirectUrl;
        public boolean active;
        public String extraInfo;
        public String bannerType;
    }
}

