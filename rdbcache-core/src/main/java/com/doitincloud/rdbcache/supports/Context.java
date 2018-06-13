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

import com.doitincloud.commons.Utils;
import com.doitincloud.rdbcache.configs.AppCtx;
import com.doitincloud.rdbcache.configs.PropCfg;
import com.doitincloud.rdbcache.models.Monitor;
import com.doitincloud.rdbcache.models.StopWatch;
import com.doitincloud.rdbcache.services.DbaseOps;

import javax.servlet.http.HttpServletRequest;

public class Context {

    private String action;

    private Monitor monitor;

    private Boolean sendValue = true;

    private Boolean batch = false;

    private String traceId;

    private Boolean monitorEnabled = PropCfg.getEnableMonitor();

    private String attr = PropCfg.getDefaultAttr();

    private Long duration;

    public Context(Boolean sendValue) {
        this.sendValue = sendValue;
        traceId = Utils.generateId();
        StackTraceElement element = Thread.currentThread().getStackTrace()[2];
        action = element.getMethodName();
        if (attr != null && !attr.equals("async")) action += "/"+attr;
        monitor = new Monitor(element.getFileName(), element.getClassName(), action);
    }

    public Context(Boolean sendValue, Boolean batch) {
        this.sendValue = sendValue;
        this.batch = batch;
        traceId = Utils.generateId();
        StackTraceElement element = Thread.currentThread().getStackTrace()[2];
        action = element.getMethodName();
        if (attr != null && !attr.equals("async")) action += "/"+attr;
        monitor = new Monitor(element.getFileName(), element.getClassName(), action);
    }

    public Context(String traceId) {
        this.sendValue = false;
        this.traceId = traceId;
        StackTraceElement element = Thread.currentThread().getStackTrace()[2];
        action = element.getMethodName();
        if (attr != null && !attr.equals("async")) action += "/"+attr;
        monitor = new Monitor(element.getFileName(), element.getClassName(), action);
    }

    public Context() {
        this.sendValue = false;
        traceId = Utils.generateId();
        action = Thread.currentThread().getStackTrace()[2].getMethodName();
        StackTraceElement element = Thread.currentThread().getStackTrace()[2];
        action = element.getMethodName();
        if (attr != null && !attr.equals("async")) action += "/"+attr;
        monitor = new Monitor(element.getFileName(), element.getClassName(), action);
    }

    public void enableMonitor(HttpServletRequest request) {
        monitorEnabled = true;
        if (monitor == null) {
            monitor = new Monitor(request.getRequestURI(), "http", action);
        } else {
            monitor.setName(request.getRequestURI());
            monitor.setTypeAndAction("http", action);
        }
        monitor.setTraceId(traceId);
    }

    public void enableMonitor(String name, String type, String action) {
        monitorEnabled = true;
        if (monitor == null) {
            monitor = new Monitor(name, type, action);
        } else {
            monitor.setName(name);
            monitor.setTypeAndAction(type, action);
        }
        monitor.setTraceId(traceId);
    }

    public boolean isMonitorEnabled() {
        if (monitor == null) {
            monitorEnabled = false;
        }
        return monitorEnabled;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Monitor getMonitor() {
        return monitor;
    }

    public void setMonitor(Monitor monitor) {
        this.monitor = monitor;
    }

    public Boolean isSendValue() {
        return sendValue;
    }

    public void setSendValue(Boolean sendValue) {
        this.sendValue = sendValue;
    }

    public Boolean isBatch() {
        return batch;
    }

    public void setBatch(Boolean batch) {
        this.batch = batch;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public Boolean isSync() {
        if (attr == null) return false;
        return attr.equals("sync");
    }

    public Boolean isDelayed() {
        if (attr == null) return false;
        return attr.equals("delayed");
    }

    public void setSync(Boolean sync) {
        if (sync) this.attr = "sync";
        else this.attr = "async";
    }

    public void setDelayed() {
        this.attr = "delayed";
    }

    public Long getDuration() {
        if (duration == null && monitor != null) {
            duration = monitor.getMainDuration();
        }
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public StopWatch startStopWatch(String type, String action) {
        if (!monitorEnabled || monitor == null) {
            return null;
        }
        return monitor.startStopWatch(type, action);
    }

    synchronized public void closeMonitor() {
        if (monitor == null) {
            return;
        }
        duration = monitor.getMainDuration();
        monitor.stopNow();
        DbaseOps dbaseOps = AppCtx.getDbaseOps();
        if (dbaseOps != null) {
            try {
                dbaseOps.saveMonitor(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        monitor = null;
    }

    public void logTraceMessage(String message) {
        if (traceId == null) {
            return;
        }
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        AppCtx.getDbaseOps().logTraceMessage(traceId, message, trace);
    }

    @Override
    protected void finalize() throws Throwable {
        closeMonitor();
        super.finalize();
    }
}
