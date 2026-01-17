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
 * Service bus handler interface.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2026
 */
public interface ServiceBusHandler {

    /**
     * Get the next request from the service bus.
     * 
     * @return the next request or empty if the request is not found.
     */
    Optional<ServiceBusRequest> nextRequest();

    /**
     * Send a response to the service bus or throw an exception if the response is not sent.
     * 
     * @param response - the response.
     */
    void sendResponse(ServiceBusResponse response);
}
