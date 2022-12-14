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

package org.apache.agent.core.mock.material;

import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Have to redefine this class dynamic, so never add `final` modifier.
 */
@NoArgsConstructor
public class Material {

    public Material(final List<String> queues) {
        queues.add("constructor");
    }

    /**
     * Mock method for testing.
     *
     * @param queues queues
     * @return result
     */
    public String mock(final List<String> queues) {
        queues.add("on");
        return "invocation";
    }

    /**
     * Mock static method for testing.
     *
     * @param queues queues
     * @return result
     */
    public static String staticMock(final List<String> queues) {
        queues.add("on");
        return "static invocation";
    }
}
