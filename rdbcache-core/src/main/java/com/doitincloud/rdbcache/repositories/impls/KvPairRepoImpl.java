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

package com.doitincloud.rdbcache.repositories.impls;

import com.doitincloud.commons.Utils;
import com.doitincloud.rdbcache.configs.AppCtx;
import com.doitincloud.rdbcache.models.KvIdType;
import com.doitincloud.rdbcache.models.KvPair;
import com.doitincloud.rdbcache.repositories.KvPairRepo;
import com.doitincloud.rdbcache.supports.DbUtils;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class KvPairRepoImpl implements KvPairRepo {

    private String table = "rdbcache_kv_pair";

    private JdbcTemplate jdbcTemplate;

    @EventListener
    public void handleApplicationReadyEvent(ApplicationReadyEvent event) {
        jdbcTemplate = AppCtx.getJdbcTemplate();
    }

    @Override
    public KvPair findById(KvIdType idType) {

        try {
            String sql = "select value from " + table + " where id = ? AND type = ?";
            Object[] params = new Object[]{idType.getId(), idType.getType()};
            String value = jdbcTemplate.queryForObject(sql, params, String.class);
            if (value == null) {
                return null;
            }
            KvPair pair = new KvPair(idType);
            pair.setValue(value);
            return pair;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Iterable<KvPair> findAllById(List<KvIdType> idTypes) {

        List<KvPair> pairs = new ArrayList<>();
        for (KvIdType idType: idTypes) {
            pairs.add(findById(idType));
        }
        return pairs;
    }

    @Override
    public boolean save(KvPair pair) {
        try {
            String sql = "insert into " + table + " (id, type, value) values (?, ?, ?) on duplicate key update id = ?, type = ?, value = ?";
            Object[] params = new Object[]{pair.getId(), pair.getType(), pair.getValue(), pair.getId(), pair.getType(), pair.getValue()};
            int result = jdbcTemplate.update(sql, params);
            return result == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        /*
        KvPair dbPair = findById(pair.getIdType());
        if (dbPair == null) {
            try {
                String sql = "insert into " + table + " (id, type, value) values (?, ?, ?)";
                Object[] params = new Object[]{pair.getId(), pair.getType(), pair.getValue()};
                int result = jdbcTemplate.update(sql, params);
                return result == 1;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } else if (!DbUtils.isMapEquals(pair.getData(), dbPair.getData())) {
            try {
                String sql = "update " + table + " set value = ? where id = ? AND type = ?";
                Object[] params = new Object[]{pair.getValue(), pair.getId(), pair.getType()};
                int result = jdbcTemplate.update(sql, params);
                return result == 1;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
        */
    }

    @Override
    public boolean saveAll(List<KvPair> pairs) {
        boolean allOk = true;
        for (KvPair pair: pairs) {
            if (!save(pair)) {
                allOk = false;
            }
        }
        return allOk;
    }

    @Override
    public boolean delete(KvPair pair) {
        try {
            String sql = "delete from " + table + " where  id = ? AND type = ?";
            Object[] params = new Object[]{pair.getId(), pair.getType()};
            int result = jdbcTemplate.update(sql, params);
            return result == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteAll(List<KvPair> pairs) {
        boolean allOk = true;
        for (KvPair pair: pairs) {
            if (!delete(pair)) {
                allOk = false;
            }
        }
        return allOk;
    }
}
