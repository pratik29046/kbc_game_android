package com.app.mygame.userPost.responseVo;

import com.app.mygame.utils.BaseResponse;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class TournamentsRegisterResponse extends BaseResponse implements Serializable {

    public  Data data;

    @lombok.Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Data{
        public int id;
        public int tournamentId;
        public int userId;
        public Date createTime;
        public Object modifyTime;
    }
}