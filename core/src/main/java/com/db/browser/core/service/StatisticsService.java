package com.db.browser.core.service;

import com.db.browser.core.service.dto.ColumnStatistics;
import com.db.browser.core.service.dto.TableStatistics;

import java.util.List;

public interface StatisticsService {

    /**
     * Implementations should return table statistics.
     */
    TableStatistics tableStatistics(Integer databaseId, String schemaName, String tableName);

    /**
     * Implementations should return column statistics.
     */
    List<ColumnStatistics> columnStatistics(Integer databaseId, String schemaName, String tableName);
}
