/*
 * Copyright 2026 Konstantin Terskikh
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package ru.spb.tksoft.flowforge.sdk.contract.servicebus;

import java.util.Optional;

/**
 * Service bus client interface.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2026
 */
public interface ServiceBusClient {

    /**
     * Send a request to the service bus.
     * 
     * @param request - the request.
     * @return the request ID or empty if the request is not sent.
     */
    Optional<String> sendRequest(ServiceBusRequest request);

    /**
     * Get a response from the service bus.
     * 
     * @param requestId - the request ID.
     * @return the response or empty if the response is not found.
     */
    Optional<ServiceBusResponse> getResponse(String requestId);
}
