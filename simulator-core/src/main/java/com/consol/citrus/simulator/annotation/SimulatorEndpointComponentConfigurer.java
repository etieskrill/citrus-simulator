/*
 * Copyright 2006-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.consol.citrus.simulator.annotation;

import com.consol.citrus.endpoint.Endpoint;
import org.springframework.context.ApplicationContext;

/**
 * @author Christoph Deppisch
 */
public interface SimulatorEndpointComponentConfigurer extends SimulatorConfigurer {

    /**
     * Gets the target endpoint.
     *
     * @param applicationContext
     * @return
     */
    Endpoint endpoint(ApplicationContext applicationContext);

    /**
     * Should operate with SOAP envelope. This automatically adds SOAP envelope
     * handling to the inbound and outbound messages.
     *
     * @return
     */
    boolean useSoapEnvelope();

}
