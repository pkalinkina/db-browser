package com.db.browser.core.service;

import com.db.browser.core.service.dto.Column;
import com.db.browser.core.service.dto.ColumnMetadata;
import com.db.browser.core.service.dto.Schema;
import com.db.browser.core.service.dto.Table;

import java.util.List;

public interface DatabaseExploreService {

    /**
     * Implementations should return schemas for provided database
     */
    List<Schema> listSchemas(Integer databaseId);

    /**
     * Implementations should return tables for provided database and schema
     */
    List<Table> listTables(Integer databaseId, String schemaName);

    /**
     * Implementations should return views for provided database and schema
     */
    List<Table> listViews(Integer databaseId, String schemaName);

    /**
     * Implementations should return columns metadata for provided database, schema and table
     */
    List<ColumnMetadata> listColumns(Integer databaseId, String schemaName, String tableName);

    /**
     * Implementations should return columns data for provided database, schema and table
     */
    List<Column> preViewColumns(Integer databaseId, String schemaName, String tableName);
}
