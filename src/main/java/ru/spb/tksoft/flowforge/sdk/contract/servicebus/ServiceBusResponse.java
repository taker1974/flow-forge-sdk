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

/**
 * Service bus response item interface.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2026
 */
public interface ServiceBusResponse {

    /**
     * Get the request ID.
     * 
     * @return the request ID.
     */
    String getRequestId();

    /**
     * Get the completed flag.
     * 
     * @return the completed flag.
     */
    boolean isCompleted();

    /**
     * Has error.
     * 
     * @return true if the response has an error, false otherwise.
     */
    boolean hasError();

    /**
     * Get the error message of the response.
     * 
     * @return the error message of the response.
     */
    String getErrorMessage();

    /**
     * Get the response data.
     * 
     * @return the response data.
     */
    Object getResponseData();
}
