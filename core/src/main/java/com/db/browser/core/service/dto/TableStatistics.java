package com.db.browser.core.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Contains number of records and attributes for a table.
 */

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TableStatistics {
    private Integer numberOfRecords;
    private Integer numberOfAttributes;
}
