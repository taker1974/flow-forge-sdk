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
 * Service bus request item interface.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2026
 */
public interface ServiceBusRequest {

    /**
     * Set the service name.
     * 
     * @param serviceName - the service name.
     */
    void setServiceName(String serviceName);

    /**
     * Get the service name.
     * 
     * @return the service name.
     */
    String getServiceName();

    /**
     * Set the request data.
     * 
     * @param requestData - the request data.
     */
    void setRequestData(Object requestData);

    /**
     * Get the request data.
     * 
     * @return the request data.
     */
    Object getRequestData();

    /**
     * Set request ID.
     * 
     * @param requestId - the request ID.
     */
    void setRequestId(String requestId);

    /**
     * Get the request ID.
     * 
     * @return the request ID.
     */
    String getRequestId();

    /**
     * Set the request timestamp.
     * 
     * @param requestTimestamp - the request timestamp.
     */
    void setRequestTimestamp(long requestTimestamp);

    /**
     * Get the request timestamp.
     * 
     * @return the request timestamp.
     */
    long getRequestTimestamp();

    /**
     * Set the request completed flag.
     * 
     * @param requestCompleted - the request completed flag.
     */
    void setRequestCompleted(boolean requestCompleted);

    /**
     * Get the request completed flag.
     * 
     * @return the request completed flag.
     */
    boolean isRequestCompleted();

    /**
     * Set the request error.
     * 
     * @param hasError - the error flag.
     * @param errorMessage - the error message.
     */
    void setError(boolean hasError, String errorMessage);

    /**
     * Has error.
     * 
     * @return true if the request has an error, false otherwise.
     */
    boolean hasError();

    /**
     * Get the error message of the request.
     * 
     * @return the error message of the request.
     */
    String getErrorMessage();
}
