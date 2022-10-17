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

package org.apache.agent.plugin.data.transaction.advice;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.agent.api.api.advice.AdviceTargetObject;
import org.apache.agent.api.api.advice.InstanceMethodAroundAdvice;
import org.apache.agent.api.api.result.MethodInvocationResult;
import org.apache.agent.plugin.data.transaction.threadlocal.ElapsedTimeThreadLocal;

import java.lang.reflect.Method;

/**
 * Transaction advice.
 */
@Slf4j
public final class TransactionAdvice implements InstanceMethodAroundAdvice {

    private static final Gson GSON = new Gson();

    @Override
    public void beforeMethod(AdviceTargetObject target, Method method, Object[] args, MethodInvocationResult result) {
        ElapsedTimeThreadLocal.INSTANCE.set(System.currentTimeMillis());
        log.info("Invoke Method: {}, Parameter: {}", method.getName(), GSON.toJson(args));
    }

    @Override
    public void afterMethod(AdviceTargetObject target, Method method, Object[] args, MethodInvocationResult result) {
        try {
            long elapsedTime = System.currentTimeMillis() - ElapsedTimeThreadLocal.INSTANCE.get();
            log.info("Invoke Method: {}, Result: {}, Cost: {}ms", method.getName(), GSON.toJson(result.getResult()), elapsedTime);
        } finally {
            ElapsedTimeThreadLocal.INSTANCE.remove();
        }
    }

    @Override
    public void onThrowing(AdviceTargetObject target, Method method, Object[] args, Throwable throwable) {
        log.info("Invoke Method: {}, Throw: {}", method.getName(), throwable.getMessage());
        ElapsedTimeThreadLocal.INSTANCE.remove();
    }
}
