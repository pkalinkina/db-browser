package com.db.browser.postgre.dto;

import com.db.browser.core.service.dto.ColumnMetadata;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostgreColumnMetadata implements ColumnMetadata {

    private Integer numericPrecisionRadix;
    private Integer datetimePrecision;
    private Integer maximumCardinality;
    private Integer characterMaximumLength;
    private Integer numericScale;
    private Integer intervalPrecision;
    private Integer numericPrecision;
    private Integer characterOctetLength;
    private Integer ordinalPosition;
    private String isNullable;
    private String columnName;
    private String isUpdatable;
    private String isIdentity;
    private String isSelfReferencing;
    private String tableCatalog;
    private String isGenerated;
    private String dataType;
    private String udtCatalog;
    private String dtdIdentifier;
    private String identityCycle;
    private String udtName;
    private String udtSchema;
}
