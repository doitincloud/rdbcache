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
public interface RedisRepo {

    public boolean ifExist(final Context context, final KvPair pair, final KeyInfo keyInfo);

    public boolean ifExist(final Context context, final KvPairs pairs, final AnyKey anyKey);

    public boolean find(final Context context, final KvPair pair, final KeyInfo keyInfo);

    public boolean find(final Context context, final KvPairs pairs, final AnyKey anyKey);

    public boolean save(final Context context, final KvPair pair, final KeyInfo keyInfo);

    public boolean save(final Context context, final KvPairs pairs, final AnyKey anyKey);

    public boolean update(final Context context, final KvPair pair, final KeyInfo keyInfo);

    public boolean update(final Context context, final KvPairs pairs, final AnyKey anyKey);

    public boolean findAndSave(final Context context, final KvPair pair, final KeyInfo keyInfo);

    public boolean findAndSave(final Context context, final KvPairs pairs, final AnyKey anyKey);

    public void delete(final Context context, final KvPair pair, final KeyInfo keyInfo);

    public void delete(final Context context, final KvPairs pairs, final AnyKey anyKey);
}