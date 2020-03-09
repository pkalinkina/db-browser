package com.db.browser.core.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



/**
 * min, max, average and median for a numeric column.
 */

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ColumnStatistics {
    private String name;
    private Double min;
    private Double max;
    private Double avg;
    private Double median;
}
