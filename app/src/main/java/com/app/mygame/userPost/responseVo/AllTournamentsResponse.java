package com.app.mygame.userPost.responseVo;

import com.app.mygame.utils.BaseResponse;

import java.io.Serializable;
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
public class AllTournamentsResponse extends BaseResponse implements Serializable {

    public List<Data> data;

    @lombok.Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Data implements Serializable {
        public int tournamentId;
        public String title;
        public String description;
        public double winningPrice;
        public double entryFee;
        public String imageUrl;
        public Date tournamentStartDateTime;
        public boolean active;
        public int tableType;
        public int gameLevel;
        public Object winnerId;
        public Object winnerName;
        public double maxParticipants;
        public int totalRegistered;
        public boolean registered;
        public int questionId1;
        public int questionId2;
        public Object questionId3;
        public Object questionId4;
        public Object questionId5;
        public Object questionId6;
        public Object questionId7;
        public Object questionId8;
        public Object questionId9;
        public Object questionId10;
    }
}
