package com.db.browser.postgre.service;

import com.db.browser.core.service.ConnectionService;
import com.db.browser.core.service.StatisticsService;
import com.db.browser.core.service.dto.ColumnStatistics;
import com.db.browser.core.service.dto.TableStatistics;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class PostgreStatisticsService implements StatisticsService {

    private final ConnectionService connectionService;


    @Override
    public TableStatistics tableStatistics(Integer databaseId, String schemaName, String tableName) {
        JdbcTemplate template = connectionService
                .getById(databaseId)
                .orElseThrow(() -> new IllegalArgumentException("Database not found"));


        return template.query(
                String.format("select * from \n" +
                                "(select count(*) as row_numbers from %s.%s) as row_numbers,\n" +
                                "(select count(*) as attributes_numbers from information_schema.columns where table_schema='%s' and table_name='%s') as attributes_numbers",
                        schemaName, tableName, schemaName, tableName),
                rs -> {
                    if (rs.next()) {
                        TableStatistics statistics = new TableStatistics();
                        statistics.setNumberOfRecords(rs.getInt("row_numbers"));
                        statistics.setNumberOfAttributes(rs.getInt("attributes_numbers"));
                        return statistics;
                    }
                    return null;
                });
    }

    @Override
    public List<ColumnStatistics> columnStatistics(Integer databaseId, String schemaName, String tableName) {
        JdbcTemplate template = connectionService
                .getById(databaseId)
                .orElseThrow(() -> new IllegalArgumentException("Database not found"));
        List<String> columns = template.query(
                String.format("select column_name, data_type from information_schema.columns \n" +
                                "where table_schema='%s' and table_name='%s' and data_type in ('smallint', 'integer', 'bigint', 'numeric(p,s)', 'real', 'double precision');",
                        schemaName, tableName),
                (rs, i) -> {
                    return rs.getString("column_name");
                });

        return columns == null ? emptyList() : columns.stream()
                .map(column -> fetchStatistics(template, column, schemaName, tableName))
                .collect(toList());
    }

    private ColumnStatistics fetchStatistics(JdbcTemplate template, String column, String schemaName, String tableName) {
        return template.query(
                String.format("select * from \n" +
                                "(select avg(%s) as _avg from %s.%s) as _avg,\n" +
                                "(select max(%s) as _max from %s.%s) as _max,\n" +
                                "(select min(%s) as _min from %s.%s) as _min,\n" +
                                "(select percentile_cont(0.5) within group ( order by %s )  as median FROM %s.%s) as median",
                        column, schemaName, tableName,
                        column, schemaName, tableName,
                        column, schemaName, tableName,
                        column, schemaName, tableName),
                (rs) -> {
                    rs.next();
                    ColumnStatistics statistics = new ColumnStatistics();
                    statistics.setName(column);
                    statistics.setAvg(rs.getDouble("_avg"));
                    statistics.setMax(rs.getDouble("_max"));
                    statistics.setMin(rs.getDouble("_min"));
                    statistics.setMedian(rs.getDouble("median"));
                    return statistics;
                });
    }
}