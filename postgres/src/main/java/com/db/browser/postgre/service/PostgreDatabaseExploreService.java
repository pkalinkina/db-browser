package com.db.browser.postgre.service;

import com.db.browser.core.service.ConnectionService;
import com.db.browser.core.service.DatabaseExploreService;
import com.db.browser.core.service.dto.Column;
import com.db.browser.core.service.dto.ColumnMetadata;
import com.db.browser.core.service.dto.Schema;
import com.db.browser.core.service.dto.Table;
import com.db.browser.core.service.exception.ResourceNotFound;
import com.db.browser.postgre.dto.PostgreColumnMetadata;
import com.db.browser.postgre.dto.PostgreSchema;
import com.db.browser.postgre.dto.PostgreTable;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class PostgreDatabaseExploreService implements DatabaseExploreService {

    private final ConnectionService connectionService;
    private final ResultSetMapping resultSetMapping;

    @Override
    public List<Schema> listSchemas(Integer databaseId) {
        JdbcTemplate template = connectionService
                .getById(databaseId)
                .orElseThrow(() -> new ResourceNotFound("Database not found"));
        return template.query("select * from information_schema.schemata", (rs, i) -> {
            PostgreSchema schema = new PostgreSchema();
            schema.setCatalogName(rs.getString("catalog_name"));
            schema.setSchemaName(rs.getString("schema_name"));
            schema.setSchemaOwner(rs.getString("schema_owner"));
            schema.setDefaultCharacterSetCatalog(rs.getString("default_character_set_catalog"));
            schema.setDefaultCharacterSetName(rs.getString("default_character_set_schema"));
            schema.setDefaultCharacterSetSchema(rs.getString("default_character_set_name"));
            schema.setSqlPath(rs.getString("sql_path"));
            return schema;
        });
    }


    @Override
    public List<Table> listTables(Integer databaseId, String schemaName) {
        JdbcTemplate template = connectionService
                .getById(databaseId)
                .orElseThrow(() -> new ResourceNotFound("Database not found"));

        return template.query(
                String.format("select * from information_schema.tables where table_type = 'BASE TABLE' and table_schema = '%s'", schemaName),
                (rs, i) -> {
                    PostgreTable table = new PostgreTable();
                    table.setTableCatalog(rs.getString("table_catalog"));
                    table.setTableSchema(rs.getString("table_schema"));
                    table.setTableName(rs.getString("table_name"));
                    table.setSelfReferencingColumnName(rs.getString("self_referencing_column_name"));
                    table.setReferenceGeneration(rs.getString("reference_generation"));
                    table.setUserDefinedTypeCatalog(rs.getString("user_defined_type_catalog"));
                    table.setUserDefinedTypeSchema(rs.getString("user_defined_type_schema"));
                    table.setUserDefinedTypeName(rs.getString("user_defined_type_name"));
                    table.setIsInsertableInto(rs.getString("is_insertable_into"));
                    table.setIsTyped(rs.getString("is_typed"));
                    table.setCommitAction(rs.getString("commit_action"));
                    return table;
                });

    }

    @Override
    public List<Table> listViews(@NonNull Integer databaseId, @NonNull String schemaName) {
        JdbcTemplate template = connectionService
                .getById(databaseId)
                .orElseThrow(() -> new ResourceNotFound("Database not found"));

        return template.query(
                String.format("select * from information_schema.tables where table_type = 'VIEW' and table_schema = '%s'", schemaName),
                (rs, i) -> {
                    PostgreTable table = new PostgreTable();
                    table.setTableCatalog(rs.getString("table_catalog"));
                    table.setTableSchema(rs.getString("table_schema"));
                    table.setTableName(rs.getString("table_name"));
                    table.setSelfReferencingColumnName(rs.getString("self_referencing_column_name"));
                    table.setReferenceGeneration(rs.getString("reference_generation"));
                    table.setUserDefinedTypeCatalog(rs.getString("user_defined_type_catalog"));
                    table.setUserDefinedTypeSchema(rs.getString("user_defined_type_schema"));
                    table.setUserDefinedTypeName(rs.getString("user_defined_type_name"));
                    table.setIsInsertableInto(rs.getString("is_insertable_into"));
                    table.setIsTyped(rs.getString("is_typed"));
                    table.setCommitAction(rs.getString("commit_action"));
                    return table;
                });
    }


    @Override
    public List<Column> preViewColumns(Integer databaseId, String schemaName, String tableName) {
        JdbcTemplate template = connectionService
                .getById(databaseId)
                .orElseThrow(() -> new ResourceNotFound("Database not found"));
        return template.query(String.format("select * from %s.%s", schemaName, tableName), resultSetMapping);
    }

    @Override
    public List<ColumnMetadata> listColumns(Integer databaseId, String schemaName, String tableName) {
        JdbcTemplate template = connectionService
                .getById(databaseId)
                .orElseThrow(() -> new ResourceNotFound("Database not found"));


        return template.query(
                String.format("select * from information_schema.columns where columns.table_name ='%s' and table_schema = '%s'", tableName, schemaName), (rs, i) -> {
                    PostgreColumnMetadata column = new PostgreColumnMetadata();
                    column.setNumericPrecisionRadix(rs.getInt("numeric_precision_radix"));
                    column.setDatetimePrecision(rs.getInt("datetime_precision"));
                    column.setMaximumCardinality(rs.getInt("maximum_cardinality"));
                    column.setCharacterMaximumLength(rs.getInt("character_maximum_length"));
                    column.setNumericScale(rs.getInt("numeric_scale"));
                    column.setIntervalPrecision(rs.getInt("interval_precision"));
                    column.setNumericPrecision(rs.getInt("numeric_precision"));
                    column.setCharacterOctetLength(rs.getInt("character_octet_length"));
                    column.setOrdinalPosition(rs.getInt("ordinal_position"));
                    column.setIsNullable(rs.getString("is_nullable"));
                    column.setColumnName(rs.getString("column_name"));
                    column.setIsUpdatable(rs.getString("is_updatable"));
                    column.setIsIdentity(rs.getString("is_identity"));
                    column.setIsSelfReferencing(rs.getString("is_self_referencing"));
                    column.setTableCatalog(rs.getString("table_catalog"));
                    column.setIsGenerated(rs.getString("is_generated"));
                    column.setDataType(rs.getString("data_type"));
                    column.setUdtCatalog(rs.getString("udt_catalog"));
                    column.setDtdIdentifier(rs.getString("dtd_identifier"));
                    column.setIdentityCycle(rs.getString("identity_cycle"));
                    column.setUdtName(rs.getString("udt_name"));
                    column.setUdtSchema(rs.getString("udt_schema"));
                    return column;
                });
    }
}
