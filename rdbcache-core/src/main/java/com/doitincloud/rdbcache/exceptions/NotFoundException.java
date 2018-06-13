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

package com.doitincloud.rdbcache.exceptions;

import com.doitincloud.rdbcache.supports.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotFoundException.class);

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(Context context, String message) {
        super(message);
        if (context != null) {
            context.logTraceMessage(message);
            context.closeMonitor();
            LOGGER.info(HttpStatus.NOT_FOUND + " NOT FOUND " + context.getAction());
        } else {
            LOGGER.info(HttpStatus.NOT_FOUND + " NOT FOUND");
        }
    }
}
