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

package com.doitincloud.rdbcache.models;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

public class KvIdType implements Serializable, Cloneable {

    private static final long serialVersionUID = 20180316L;

    private String id;

    private String type = "data";

    public KvIdType(String id, String type) {
        this.id = id;
        this.type = type;
    }

    public KvIdType(String id) {
        this.id = id;
    }

    public KvIdType(KvIdType idType) {
        id = idType.id;
        type = idType.type;
    }

    public KvIdType() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public KvIdType clone() {
        return new KvIdType(id, type);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KvIdType kvIdType = (KvIdType) o;
        return Objects.equals(id, kvIdType.id) &&
               Objects.equals(type, kvIdType.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type);
    }

    @Override
    public String toString() {
        return "{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
