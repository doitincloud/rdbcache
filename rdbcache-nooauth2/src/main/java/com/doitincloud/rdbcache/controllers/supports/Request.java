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

package com.doitincloud.rdbcache.controllers.supports;

import com.doitincloud.rdbcache.supports.AnyKey;
import com.doitincloud.rdbcache.supports.Context;
import com.doitincloud.rdbcache.supports.KvPairs;
import com.doitincloud.rdbcache.configs.AppCtx;
import com.doitincloud.rdbcache.configs.PropCfg;
import com.doitincloud.rdbcache.exceptions.BadRequestException;
import com.doitincloud.rdbcache.exceptions.ServerErrorException;
import com.doitincloud.rdbcache.models.KeyInfo;
import com.doitincloud.rdbcache.models.KvPair;
import com.doitincloud.rdbcache.queries.QueryInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

public class Request {

    private static final Logger LOGGER = LoggerFactory.getLogger(Request.class);

    private static Pattern expPattern = Pattern.compile("([0-9]+|-[0-9]+|\\+[0-9]+)(-sync)?$");

    public static AnyKey process(Context context, HttpServletRequest request) {
        return process(context, request, null);
    }

    public static AnyKey process(Context context, HttpServletRequest request, KvPairs pairs,
                                 Optional<String> ... opts) {

        LOGGER.info("URI: "+ request.getRequestURI());

        if (PropCfg.getEnableMonitor()) context.enableMonitor(request);

        String[] options = {null, null}; // {expire, table}

        for (int i = 0; i < opts.length; i++) {
            Optional<String> opt = opts[i];
            if (opt != null && opt.isPresent()) {
                assignOption(context, opt.get(), options);
            }
        }

        AnyKey anyKey = new AnyKey();

        if (pairs == null) {
            return anyKey;
        }

        KeyInfo keyInfo = anyKey.getAny();
        String table = options[1];

        if (pairs.size() > 0) {

            if (table != null) {
                // populate table info into all pairs
                for (KvPair pair: pairs) {
                    pair.setType(table);
                }
            }

            // find key info for the first pair
            //
            KvPair pair = pairs.get(0);
            if (!pair.isNewUuid()) {
                AppCtx.getKeyInfoRepo().find(context, pair, keyInfo);
            }
        }

        processOptions(context, request, keyInfo, options);

        if (pairs.size() == 0) {
            return anyKey;
        }

        // query string precedes all caches
        //
        if (keyInfo.getQuery() != null) {
            return anyKey;
        }

        // find key info for the second and after
        //
        if (pairs.size() > 1) {
            AppCtx.getKeyInfoRepo().find(context, pairs, anyKey);
        }

        // save key info to local cahce
        //
        for (int i = 0; i < pairs.size() && i < anyKey.size(); i++) {
            keyInfo = anyKey.get(i);
            if (keyInfo.getIsNew()) {
                keyInfo.setIsNew(false);
                KvPair pair = pairs.get(i);
                AppCtx.getCacheOps().putKeyInfo(pair.getIdType(), keyInfo);
                keyInfo.setIsNew(true);
            }
        }

        if (anyKey.size() != 1 && pairs.size() != anyKey.size()) {
            throw new ServerErrorException(context, "case not supported, anyKey size(" + anyKey.size() +
                    ") != 1 && pairs size(" + pairs.size() + ") != anyKey size(" + anyKey.size() + ")");
        }

        return anyKey;
    }

    private static void processOptions(Context context, HttpServletRequest request,
                                            KeyInfo keyInfo, String[] options) {

        Map<String, String[]> params = request.getParameterMap();

        if (keyInfo.getIsNew()) {
            if (options[1] != null) {
                keyInfo.setTable(options[1]);
            }
            if (options[0] != null) {
                keyInfo.setExpire(options[0]);
            }
            if (params != null && params.size() > 0) {
                QueryInfo queryInfo = new QueryInfo(keyInfo.getTable(), params);
                keyInfo.setQuery(queryInfo);
            }
        } else {
            if (options[0] != null && !options[0].equals(keyInfo.getExpire())) {
                keyInfo.setExpire(options[0]);
                keyInfo.setIsNew(true);
            }
            if (options[1] != null && !options[1].equals(keyInfo.getTable())) {
                throw new BadRequestException(context, "can not change table name for an existing key");
            }
            if (params != null && params.size() > 0) {
                QueryInfo queryInfo = new QueryInfo(keyInfo.getTable(), params);
                if (keyInfo.getQueryKey() == null || !keyInfo.getQueryKey().equals(queryInfo.getKey())) {
                    throw new BadRequestException(context, "can not modify condition for an existing key");
                }
            }
        }
    }

    private static void assignOption(Context context, String opt, String[] options) {

        opt = opt.trim();
        if (opt.equals("async")) {
            if (context.isSync()) {
                context.setSync(false);
            } else {
                LOGGER.trace("default is async, no need to have option async");
            }
            return;
        }
        if (opt.equals("sync")) {
            if (context.isSync()) {
                LOGGER.trace("default is sync, no need to have option sync");
            } else {
                context.setSync(true);
            }
            return;
        }
        if (opt.equals("delayed")) {
            if (context.isSync()) {
                LOGGER.trace("default is delayed, no need to have option delayed");
            } else {
                context.setDelayed();
            }
            return;
        }
        if (options[0] == null && expPattern.matcher(opt).matches()) {
            options[0] = opt;
            return;
        }
        if (options[1] == null) {
            Map<String, Object> tables = AppCtx.getDbaseOps().getTablesMap(context);
            if (tables.containsKey(opt)) {
                options[1] = opt;
                return;
            }
        }
        if (expPattern.matcher(opt).matches()) {
            throw new BadRequestException(context, "invalid path variable " + opt + ", expire already found");
        } else {
            throw new BadRequestException(context, "invalid path variable " + opt +
                    ", table not found OR missing primary/unique index");
        }
    }
}
