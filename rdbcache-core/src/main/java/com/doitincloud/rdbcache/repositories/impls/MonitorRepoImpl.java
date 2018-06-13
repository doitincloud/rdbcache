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
import com.doitincloud.rdbcache.models.Monitor;
import com.doitincloud.rdbcache.models.StopWatch;
import com.doitincloud.rdbcache.repositories.MonitorRepo;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository
public class MonitorRepoImpl implements MonitorRepo {

    private String monitorTable = "rdbcache_monitor";

    private String stopWatchTable = "rdbcache_stopwatch";

    private JdbcTemplate jdbcTemplate;

    @EventListener
    public void handleApplicationReadyEvent(ApplicationReadyEvent event) {
        jdbcTemplate = AppCtx.getJdbcTemplate();
    }

    @Override
    public Monitor findById(Long id) {
        String sql = "select * from " + monitorTable + " where id = ?";
        Object[] params = new Object[] { id };
        Monitor monitor = jdbcTemplate.queryForObject(sql, params, Monitor.class);
        if (monitor == null) {
            return null;
        }
        sql = "select * from " + stopWatchTable + " where monitor_id = ?";
        List<StopWatch> list = jdbcTemplate.queryForList(sql, params, StopWatch.class);
        monitor.setStopWatches(list);
        return monitor;
    }

    @Override
    public void save(Monitor monitor) {

        Map<String, Object> map = Utils.toMap(monitor);
        String fields = "";
        String values = "";
        final List<Object> params1 = new ArrayList<>();
        for (Map.Entry<String, Object> entry: map.entrySet()) {
            params1.add(entry.getValue());
            if (fields.length() != 0) {
                fields += ", ";
                values += ", ";
            }
            fields += entry.getKey();
            values += "?";
        }
        final String sql1 = "insert into " + monitorTable + " (" + fields + ") values (" + values + ")";

        int result = 0;
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            result = jdbcTemplate.update(new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                    PreparedStatement ps;
                    ps = connection.prepareStatement(sql1, Statement.RETURN_GENERATED_KEYS);
                    int i = 1;
                    for (Object param : params1) {
                        ps.setObject(i++, param);
                    }
                    return ps;
                }
            }, keyHolder);

        } catch (Exception e) {
            e.getStackTrace();
            return;
        }
        if (result == 0) {
            return;
        }
        Long id = Long.valueOf(keyHolder.getKey().toString());

        List<StopWatch> watches = monitor.getStopWatches();
        if (watches != null && watches.size() > 0) {

            map = AppCtx.getDbaseOps().getTableColumns(null, stopWatchTable);
            Set<String> keySet = map.keySet();

            fields = "";
            values = "";
            for (String key: keySet) {
                if (fields.length() != 0) {
                    fields += ", ";
                    values += ", ";
                }
                fields += key;
                values += "?";
            }
            final String sql2 = "insert into " + stopWatchTable + "(" + fields + ") values (" + values + ")";

            List<Object[]> paramsList = new ArrayList<Object[]>();

            for (StopWatch watch: watches) {
                watch.setMonitorId(id);
                map = Utils.toMap(watch);
                List<Object> params = new ArrayList<>();
                for (String key: keySet) {
                    if (map.containsKey(key)) {
                        params.add(map.get(key));
                    } else {
                        params.add(null);
                    }
                }
                paramsList.add(params.toArray());
            }
            jdbcTemplate.batchUpdate(sql2, paramsList);
        }
    }
}
