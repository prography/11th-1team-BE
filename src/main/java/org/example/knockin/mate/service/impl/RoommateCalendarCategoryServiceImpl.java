package org.example.knockin.mate.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.knockin.mate.entity.RoommateCalendarCategory;
import org.example.knockin.mate.repository.RoommateCalendarCategoryRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoommateCalendarCategoryServiceImpl {

    private final RoommateCalendarCategoryRepository roommateCalendarCategoryRepository;

    public RoommateCalendarCategory save(String name) {
        RoommateCalendarCategory category = RoommateCalendarCategory.builder()
                .name(name)
                .build();
        return roommateCalendarCategoryRepository.save(category);
    }
}
