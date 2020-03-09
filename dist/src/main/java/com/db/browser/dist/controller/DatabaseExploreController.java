package com.db.browser.dist.controller;

import com.db.browser.core.service.DatabaseExploreService;
import com.db.browser.core.service.dto.Column;
import com.db.browser.core.service.dto.ColumnMetadata;
import com.db.browser.core.service.dto.Schema;
import com.db.browser.core.service.dto.Table;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Api(description = "Provides insight to schemas, tables and columns of chosen database", tags = {"General"})
@Controller
@RequestMapping(path = "/{dbId}", method = GET, produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class DatabaseExploreController {

    private final DatabaseExploreService databaseExploreService;

    @ApiOperation(value = "Returns list of schemas for provided database", tags = {"Schema"})
    @RequestMapping(path = "/schemas")
    public ResponseEntity<List<Schema>> listSchemas(
            @PathVariable("dbId") Integer databaseId
    ) {
        return ok(databaseExploreService.listSchemas(databaseId));
    }

    @ApiOperation(value = "Returns list of tables for provided database and schema", tags = {"Table"})
    @RequestMapping(path = "/schemas/{schemaName}/tables")
    public ResponseEntity<List<Table>> listTables(
            @PathVariable("dbId") Integer databaseId,
            @PathVariable("schemaName") String schemaName
    ) {
        return ok(databaseExploreService.listTables(databaseId, schemaName));
    }

    @ApiOperation(value = "Returns list of views for provided database and schema", tags = {"View"})
    @RequestMapping(path = "/schemas/{schemaName}/views")
    public ResponseEntity<List<Table>> listViews(
            @PathVariable("dbId") Integer databaseId,
            @PathVariable("schemaName") String schemaName
    ) {
        return ok(databaseExploreService.listViews(databaseId, schemaName));
    }

    @ApiOperation(value = "Returns list of columns metadata for provided database, schema and table", tags = {"Column"})
    @RequestMapping(path = "/schemas/{schemaName}/tables/{tableName}/columns")
    public ResponseEntity<List<ColumnMetadata>> listColumns(
            @PathVariable("dbId") Integer databaseId,
            @PathVariable("schemaName") String schemaName,
            @PathVariable("tableName") String tableName
    ) {
        return ok(databaseExploreService.listColumns(databaseId, schemaName, tableName));
    }

    @ApiOperation(value = "Returns list of columns data for provided database, schema and table", tags = {"Column"})
    @RequestMapping(path = "/schemas/{schemaName}/tables/{tableName}/columns/preview")
    public ResponseEntity<List<Column>> previewColumns(
            @PathVariable("dbId") Integer databaseId,
            @PathVariable("schemaName") String schemaName,
            @PathVariable("tableName") String tableName
    ) {
        return ok(databaseExploreService.preViewColumns(databaseId, schemaName, tableName));
    }
}
