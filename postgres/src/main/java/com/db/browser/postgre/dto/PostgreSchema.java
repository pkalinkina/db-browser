package com.db.browser.postgre.dto;

import com.db.browser.core.service.dto.Schema;
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
public class PostgreSchema implements Schema {
    private String catalogName;
    private String schemaName;
    private String schemaOwner;
    private String defaultCharacterSetCatalog;
    private String defaultCharacterSetSchema;
    private String defaultCharacterSetName;
    private String sqlPath;
}
