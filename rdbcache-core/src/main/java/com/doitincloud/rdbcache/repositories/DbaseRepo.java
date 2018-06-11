/**
 * @link http://rdbcache.com/
 * @copyright Copyright (c) 2017-2018 Sam Wen
 * @license http://rdbcache.com/license/
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
