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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class StopWatch {

    private Long id;

    @JsonProperty("monitor_id")
    private Long monitorId;

    private String type = "";

    private String action;

    @JsonProperty("thread_id")
    private Long threadId;

    private Long duration;

    @JsonProperty("started_at")
    private Long startedAt;

    @JsonProperty("ended_at")
    private Long endedAt;

    public StopWatch(String type, String action) {
        threadId = Thread.currentThread().getId();
        this.startedAt = System.nanoTime();
        this.type = (type.length() <= 16 ? type : type.substring(type.length() - 16));
        this.action = action;
    }

    public StopWatch() {
        threadId = Thread.currentThread().getId();
        this.startedAt = System.nanoTime();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMonitorId() {
        return monitorId;
    }

    public void setMonitorId(Long monitorId) {
        this.monitorId  = monitorId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Long getThreadId() {
        return threadId;
    }

    public void setThreadId(Long threadId) {
        this.threadId = threadId;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Long getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Long startedAt) {
        this.startedAt = startedAt;
    }

    public Long getEndedAt() {
        return endedAt;
    }

    public void setEndedAt(Long endedAt) {
        this.endedAt = endedAt;
    }

    public void startNow() {
        this.startedAt = System.nanoTime();
    }

    public Long stopNow() {
        if (endedAt != null) {
            return duration;
        }
        endedAt = System.nanoTime();
        duration = endedAt - startedAt;
        return duration;
    }
}
