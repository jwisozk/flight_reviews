/*
 * Copyright (C) 2019 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jwisozk.flightreviews

import kotlinx.coroutines.withTimeout

/**
 * Repository provides an interface to fetch a data or request a new one be generated.
 */
class Repository(private val network: MainNetwork) {

    /**
     * Refresh the current data.
     */
    suspend fun refreshData() {
        try {
            withTimeout(5_000) {
                network.getData()
            }
        } catch (error: Throwable) {
            throw DataRefreshError("Unable to connect to the server", error)
        }
    }
}

/**
 * Thrown when there was a error fetching a new data
 *
 * @property message user ready error message
 * @property cause the original cause of this exception
 */
class DataRefreshError(message: String, cause: Throwable) : Throwable(message, cause)


