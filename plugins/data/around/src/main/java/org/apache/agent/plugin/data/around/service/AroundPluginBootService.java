/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.agent.plugin.data.around.service;

import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.apache.agent.api.config.PluginConfiguration;
import org.apache.agent.api.spi.boot.PluginBootService;
import org.apache.agent.plugin.data.around.Type;

/**
 * Open tracing plugin boot service.
 */
@Slf4j
public final class AroundPluginBootService implements PluginBootService {

    private static final String KEY_JVM_INFORMATION_COLLECTOR_ENABLED = "jvm-information-collector-enabled";

    @Override
    public void start(final PluginConfiguration pluginConfig) {
        Preconditions.checkState(pluginConfig.getPort() > 0, "Prometheus config error, host is null or port is `%s`", pluginConfig.getPort());
        log.info(getType() + " plugin started");
        log.info(getType() + " running on {}:{} ", pluginConfig.getHost(), pluginConfig.getPort());
        log.info(KEY_JVM_INFORMATION_COLLECTOR_ENABLED + " state: {}", pluginConfig.getProps().getProperty(KEY_JVM_INFORMATION_COLLECTOR_ENABLED));
    }

    @Override
    public void close() {
        log.info(getType() + " plugin closed");
    }

    @Override
    public String getType() {
        return Type.PLUGIN_TYPE;
    }
}
