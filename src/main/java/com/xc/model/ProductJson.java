package com.xc.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class ProductJson {
    @JsonProperty("data")
    private List<DataDTO> data;
    @JsonProperty("database")
    private String database;
    @JsonProperty("es")
    private Long es;
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("isDdl")
    private Boolean isDdl;
    @JsonProperty("mysqlType")
    private MysqlTypeDTO mysqlType;
    @JsonProperty("old")
    private List<OldDTO> old;
    @JsonProperty("pkNames")
    private Object pkNames;
    @JsonProperty("sql")
    private String sql;
    @JsonProperty("sqlType")
    private SqlTypeDTO sqlType;
    @JsonProperty("table")
    private String table;
    @JsonProperty("ts")
    private Long ts;
    @JsonProperty("type")
    private String type;

    @NoArgsConstructor
    @Data
    public static class MysqlTypeDTO {
        @JsonProperty("id")
        private String id;
        @JsonProperty("name")
        private String name;
        @JsonProperty("description")
        private String description;
    }

    @NoArgsConstructor
    @Data
    public static class SqlTypeDTO {
        @JsonProperty("id")
        private Integer id;
        @JsonProperty("name")
        private Integer name;
        @JsonProperty("description")
        private Integer description;
    }

    @NoArgsConstructor
    @Data
    public static class DataDTO {
        @JsonProperty("id")
        private String id;
        @JsonProperty("name")
        private String name;
        @JsonProperty("description")
        private String description;
    }

    @NoArgsConstructor
    @Data
    public static class OldDTO {
        @JsonProperty("description")
        private String description;
    }
}
