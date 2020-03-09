package com.db.browser.dist.controller;

import com.db.browser.core.service.StatisticsService;
import com.db.browser.core.service.dto.ColumnStatistics;
import com.db.browser.core.service.dto.TableStatistics;
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

@Api(description = "Returns table and schema's statistics", tags = {"General"})
@Controller
@RequestMapping(method = GET, path = "/{dbId}", produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class StatisticsController {

    private final StatisticsService statisticsService;


    @ApiOperation(value = "Returns table statistics", tags = {"Table"})
    @RequestMapping(path = "/schemas/{schemaName}/tables/{tableName}/statistics")
    public ResponseEntity<TableStatistics> tableStatistics(
            @PathVariable("dbId") Integer databaseId,
            @PathVariable("schemaName") String schemaName,
            @PathVariable("tableName") String tableName
    ) {
        return ok(statisticsService.tableStatistics(databaseId, schemaName, tableName));
    }

    @ApiOperation(value = "Returns column statistics", tags = {"Column"})
    @RequestMapping(path = "/schemas/{schemaName}/tables/{tableName}/columns/statistics")
    public ResponseEntity<List<ColumnStatistics>> columnStatistics(
            @PathVariable("dbId") Integer databaseId,
            @PathVariable("schemaName") String schemaName,
            @PathVariable("tableName") String tableName
    ) {
        return ok(statisticsService.columnStatistics(databaseId, schemaName, tableName));
    }

}
