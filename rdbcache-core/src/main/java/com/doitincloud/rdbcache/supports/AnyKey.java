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

package com.doitincloud.rdbcache.supports;

import com.doitincloud.rdbcache.exceptions.ServerErrorException;
import com.doitincloud.rdbcache.models.KeyInfo;

import java.util.ArrayList;

public class AnyKey extends ArrayList<KeyInfo> {

    public AnyKey(KeyInfo keyInfo) {
        add(keyInfo);
    }

    public AnyKey() {
    }

    public void setKeyInfo(KeyInfo keyInfo) {
        clear();
        add(keyInfo);
    }

    public KeyInfo getKeyInfo() {
        if (size() == 0) {
            return null;
        }
        return get(0);
    }

    public KeyInfo getAny() {
        return getAny(0);
    }

    public KeyInfo getAny(int index) {
        int size = size();
        if (index > size) {
            throw new ServerErrorException("getAny index out of range");
        }
        if (size == 0 || index == size) {
            KeyInfo keyInfo = null;
            if (size == 0) {
                keyInfo = new KeyInfo();
                keyInfo.setIsNew(true);
            } else {
                keyInfo = get(0).clone();
                keyInfo.setIsNew(true);
                keyInfo.clearParams();
            }
            add(keyInfo);
        }
        return get(index);
    }

    public String printTable() {
        if (size() == 0) {
            return null;
        }
        String s = get(0).getTable();
        if (size() > 1) {
            s += "...";
        }
        return s;
    }

    public boolean isNoOps() {
        if (size() == 0) {
            return false;
        }
        return getAny(0).isNoOps();
    }

    public String print() {
        if (size() == 0) {
            return null;
        }
        String s = get(0).toString();
        if (size() > 1) {
            s += "...";
        }
        return s;
    }

    public AnyKey clone() {
        AnyKey anyKey = new AnyKey();
        for (KeyInfo keyInfo: this) {
            anyKey.add(keyInfo.clone());
        }
        return anyKey;
    }
}
