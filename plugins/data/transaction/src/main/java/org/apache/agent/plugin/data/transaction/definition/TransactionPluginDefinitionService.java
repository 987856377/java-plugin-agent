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

package org.apache.agent.plugin.data.transaction.definition;

import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.matcher.ElementMatchers;
import org.apache.agent.api.api.point.PluginInterceptorPoint;
import org.apache.agent.api.spi.definition.AbstractPluginDefinitionService;
import org.apache.agent.core.entity.Interceptor;
import org.apache.agent.core.entity.Interceptors;
import org.apache.agent.core.entity.TargetPoint;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;

/**
 * Open tracing plugin definition service.
 */
@Slf4j
public final class TransactionPluginDefinitionService extends AbstractPluginDefinitionService {

    private static final String METHOD_INSTANCE_TYPE = "instance";
    private static final String METHOD_STATIC_TYPE = "static";

    private static final String DEFAULT_INTERCEPTORS_PATH = "/config/interceptors.yaml";

    @Override
    public void defineInterceptors() {
        InputStream inputStream = getClass().getResourceAsStream(DEFAULT_INTERCEPTORS_PATH);
        Interceptors interceptors = new Yaml().loadAs(inputStream, Interceptors.class);
        for (Interceptor each : interceptors.getInterceptors()) {
            if (null == each.getTarget()) {
                continue;
            }
            PluginInterceptorPoint.Builder builder = defineInterceptor(each.getTarget());
            if (null != each.getConstructAdvice() && !("".equals(each.getConstructAdvice()))) {
                builder.onConstructor(ElementMatchers.isConstructor()).implement(each.getConstructAdvice()).build();
                log.debug("Init construct: {}", each.getConstructAdvice());
            }
            if (null == each.getPoints()) {
                continue;
            }
            String[] instancePoints = each.getPoints().stream().filter(i -> METHOD_INSTANCE_TYPE.equals(i.getType())).map(TargetPoint::getName).toArray(String[]::new);
            String[] staticPoints = each.getPoints().stream().filter(i -> METHOD_STATIC_TYPE.equals(i.getType())).map(TargetPoint::getName).toArray(String[]::new);
            if (instancePoints.length > 0) {
                builder.aroundInstanceMethod(ElementMatchers.namedOneOf(instancePoints)).implement(each.getInstanceAdvice()).build();
                log.debug("Init instance: {}", each.getInstanceAdvice());
            }
            if (staticPoints.length > 0) {
                builder.aroundClassStaticMethod(ElementMatchers.namedOneOf(staticPoints)).implement(each.getStaticAdvice()).build();
                log.debug("Init static: {}", each.getStaticAdvice());
            }
        }
    }

    @Override
    public String getType() {
        return "Transaction";
    }
}
