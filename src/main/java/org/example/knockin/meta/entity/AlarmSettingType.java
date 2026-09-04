package org.example.knockin.meta.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AlarmSettingType {
    NOTIFICATION("알림");

    private final String message;
}