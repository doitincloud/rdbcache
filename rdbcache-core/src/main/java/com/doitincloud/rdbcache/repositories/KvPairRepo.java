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

import com.doitincloud.rdbcache.models.KvIdType;
import com.doitincloud.rdbcache.models.KvPair;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KvPairRepo {

    KvPair findById(KvIdType idType);

    Iterable<KvPair> findAllById(List<KvIdType> idTypes);

    boolean save(KvPair pair);

    boolean saveAll(List<KvPair> pairs);

    boolean delete(KvPair pair);

    boolean deleteAll(List<KvPair> pairs);
}