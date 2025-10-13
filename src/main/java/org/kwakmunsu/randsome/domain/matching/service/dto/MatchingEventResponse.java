package org.kwakmunsu.randsome.domain.matching.service.dto;

import java.time.LocalDateTime;
import org.kwakmunsu.randsome.domain.matching.enums.MatchingEventType;

public record MatchingEventResponse(
        String eventType,
        String eventDescription,
        LocalDateTime createdAt
) {

    public static MatchingEventResponse from(MatchingEventType eventType, String eventDescription, LocalDateTime createdAt) {
        return new MatchingEventResponse(
                eventType.getDescription(),
                eventDescription,
                createdAt
        );
    }

}