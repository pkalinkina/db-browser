package com.db.browser.postgre.service;

import com.db.browser.postgre.dto.PostgreColumn;
import lombok.SneakyThrows;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.Array;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Types;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ResultSetMapping implements RowMapper {

    @SneakyThrows
    private static Map<String, Object> get(ResultSet rs) {

        ResultSetMetaData metaData = rs.getMetaData();
        int numColumns = metaData.getColumnCount();
        String[] columnNames = new String[numColumns];
        int[] columnTypes = new int[numColumns];

        for (int i = 0; i < columnNames.length; i++) {
            columnNames[i] = metaData.getColumnLabel(i + 1);
            columnTypes[i] = metaData.getColumnType(i + 1);
        }


        Map<String, Object> content = new HashMap<>();
        boolean b;
        long l;
        double d;

        for (int i = 0; i < columnNames.length; i++) {
            String key = columnNames[i];
            Object value = null;
            switch (columnTypes[i]) {
                case Types.INTEGER:
                    value = rs.getInt(i + 1);
                    break;
                case Types.BIGINT:
                    value = rs.getLong(i + 1);
                    break;
                case Types.DECIMAL:
                case Types.NUMERIC:
                    value = rs.getBigDecimal(i + 1).toString();
                    break;
                case Types.FLOAT:
                case Types.REAL:
                case Types.DOUBLE:
                    value = rs.getDouble(i + 1);
                    break;
                case Types.NVARCHAR:
                case Types.VARCHAR:
                case Types.LONGNVARCHAR:
                case Types.LONGVARCHAR:
                case Types.CHAR:
                case Types.OTHER:
                    value = rs.getString(i + 1);
                    break;

                case Types.BOOLEAN:
                case Types.BIT:
                    value = rs.getBoolean(i + 1);
                    break;
                case Types.BINARY:
                case Types.VARBINARY:
                case Types.LONGVARBINARY:
                    value = Arrays.toString(rs.getBytes(i + 1));
                    break;

                case Types.TINYINT:
                case Types.SMALLINT:
                    value = rs.getShort(i + 1);
                    break;
                case Types.DATE:
                    value = rs.getDate(i + 1);
                    break;

                case Types.TIMESTAMP:
                    value = rs.getTime(i + 1);
                    break;
                case Types.BLOB:
                    Blob blob = rs.getBlob(i + 1);
                    InputStream stream = rs.getBlob(i).getBinaryStream();
                    StringBuilder textBuilder = new StringBuilder();
                    try (Reader reader = new BufferedReader(new InputStreamReader
                            (stream, Charset.forName(StandardCharsets.UTF_8.name())))) {
                        int c;
                        while ((c = reader.read()) != -1) {
                            textBuilder.append((char) c);
                        }
                    }
                    value = textBuilder.toString();
                    blob.free();
                    break;
                case Types.ARRAY:
                    Array array = rs.getArray(i + 1);
                    if (array != null) {
                        Object[] objects = (Object[]) array.getArray();
                        value = Arrays.stream(objects).collect(Collectors.toList());
                    }
                    break;

                case Types.STRUCT:
                    throw new RuntimeException("ResultSetSerializer not yet implemented for SQL type STRUCT");

                case Types.DISTINCT:
                    throw new RuntimeException("ResultSetSerializer not yet implemented for SQL type DISTINCT");

                case Types.REF:
                    throw new RuntimeException("ResultSetSerializer not yet implemented for SQL type REF");
                default:
                    value = rs.getString(i + 1);
                    break;
            }
            if (value != null) {
                content.put(key, value);
            }
        }


        return content;
    }

    @Override
    public PostgreColumn mapRow(ResultSet rs, int rowNum) {
        return new PostgreColumn(rowNum, get(rs));
    }
}
