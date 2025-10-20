package org.kwakmunsu.randsome.admin;

import java.util.List;

public record PageResponse<T>(
        List<T> content,
        Long count
) {

}
