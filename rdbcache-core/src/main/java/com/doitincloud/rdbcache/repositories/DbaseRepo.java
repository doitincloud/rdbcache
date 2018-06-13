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

package com.doitincloud.rdbcache.repositories;

import com.doitincloud.rdbcache.supports.AnyKey;
import com.doitincloud.rdbcache.supports.Context;
import com.doitincloud.rdbcache.supports.KvPairs;
import com.doitincloud.rdbcache.models.KeyInfo;
import com.doitincloud.rdbcache.models.KvPair;
import org.springframework.stereotype.Repository;

@Repository
public interface DbaseRepo {

    public boolean find(final Context context, final KvPair pair, final KeyInfo keyInfo);

    public boolean find(final Context context, final KvPairs pairs, final AnyKey anyKey);

    public boolean save(final Context context, final KvPair pair, final KeyInfo keyInfo);

    public boolean save(final Context context, final KvPairs pairs, final AnyKey anyKey);

    public boolean insert(final Context context, final KvPair pair, final KeyInfo keyInfo);

    public boolean insert(final Context context, final KvPairs pairs, final AnyKey anyKey);

    public boolean update(final Context context, final KvPair pair, final KeyInfo keyInfo);

    public boolean update(final Context context, final KvPairs pairs, final AnyKey anyKey);

    public boolean delete(final Context context, final KvPair pair, final KeyInfo keyInfo);

    public boolean delete(final Context context, final KvPairs pairs, final AnyKey anyKey);
}
