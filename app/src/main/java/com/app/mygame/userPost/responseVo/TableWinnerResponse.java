package com.app.mygame.userPost.responseVo;

import com.app.mygame.utils.BaseResponse;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class TableWinnerResponse extends BaseResponse {

    public Data data;

    @lombok.Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Data{
        public int id;
        public int tournamentId;
        public int gameLevel;
        public int questionId;
        public int userId;
        public String userName;
        public int tableNo;
        public int answerTime;
        public Date createTime;
        public Object modifyTime;
    }

}
