package org.example.knockin.dto;

import org.example.knockin.entity.room.RoomProfileType;

/**
 * 방을 구하는 경우와 내놓는 경우에 공통으로 확인할 값이다.
 */
public interface RoomProfileInput {
    RoomProfileType getType();

    Integer getDeposit();

    Integer getMounthRent();

    Integer getMinDeposit();

    Integer getMaxDeposit();

    Integer getMinMounthRent();

    Integer getMaxMounthRent();
}
