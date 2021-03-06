/*
 *  Copyright 2017-2018 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.doitincloud.rdbcache.queries;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.doitincloud.commons.Utils;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.Serializable;
import java.util.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class QueryInfo {

    private String table;

    private Map<String, Condition> conditions = new LinkedHashMap<String, Condition>();

    private Integer limit;

    @JsonIgnore
    private String key;

    public QueryInfo(String table, Map<String, String[]> params) {
        this.table = table;
        Parser.prepareConditions(this, params);
    }

    public QueryInfo(String table) {
        this.table = table;
    }

    public QueryInfo() {
    }

    public String getKey() {
        if (key == null) {
            key = DigestUtils.md5Hex(table + toString());
        }
        return key;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public Map<String, Condition> getConditions() {
        return conditions;
    }

    public void setConditions(Map<String, Condition> conditions) {
        this.conditions = conditions;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QueryInfo queryInfo = (QueryInfo) o;

        if (table != null ? !table.equals(queryInfo.table) : queryInfo.table != null) return false;
        if (conditions != null ? !conditions.equals(queryInfo.conditions) : queryInfo.conditions != null) return false;
        return limit != null ? limit.equals(queryInfo.limit) : queryInfo.limit == null;
    }

    @Override
    public int hashCode() {
        int result = table != null ? table.hashCode() : 0;
        result = 31 * result + (conditions != null ? conditions.hashCode() : 0);
        result = 31 * result + (limit != null ? limit.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        String s2 = "";
        if (conditions != null) {
            s2 = Utils.toJsonMap(conditions).replace("\":", ": ");
            s2 = s2.replace("\"", "");
        }
        String s1 = (limit == null ? "" : "limit: " + limit);
        if (s1.length() > 0) return s1 + " " + s2;
        else return s2;
    }
}

