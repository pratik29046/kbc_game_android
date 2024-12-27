package com.app.mygame.userPost.responseVo;

import com.app.mygame.utils.BaseResponse;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class PlansResponse extends BaseResponse {

  public List<Data> data;

    @lombok.Data
    @AllArgsConstructor
    @NoArgsConstructor
   public static class Data {
        public int planId;
        public String purchaseId;
        public String description;
        public String imageUrl;
        public boolean active;
        public String name;
        public int amount;
        public int coin;
        public double discountPercentage;
        public Date createTime;
        public Object modifyTime;
    }
}