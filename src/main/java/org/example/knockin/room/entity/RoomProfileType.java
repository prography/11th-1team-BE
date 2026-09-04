package org.example.knockin.room.entity;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum RoomProfileType {
    SEEKER(Values.SEEKER),
    OFFER(Values.OFFER);

    private final String value;

    public static class Values {
        public static final String SEEKER = "SEEKER";
        public static final String OFFER = "OFFER";
    }
}
