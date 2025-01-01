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
public class StartRoundResponse extends BaseResponse {

    public Data data;

    @lombok.Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Data {
        public int questionId;
        public String questionType;
        public String question;
        public String questionDescription;
        public String optionA;
        public String optionB;
        public String optionC;
        public String optionD;
        public String correctAnswer;
        public String questionLevel;
        public boolean active;
        public int tableNo;
        public Date createTime;
        public Object modifyTime;
    }
}
