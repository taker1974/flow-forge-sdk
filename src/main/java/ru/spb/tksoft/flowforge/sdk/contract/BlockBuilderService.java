/*
 * Copyright 2025 Konstantin Terskikh
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

package ru.spb.tksoft.flowforge.sdk.contract;

import java.util.List;

/**
 * Block builder service interface.
 * 
 * This interface must be supported by each plugin for use in JPMS + ServiceLoader scenarios.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
public interface BlockBuilderService {

    /**
     * Get the engine version that the block builder service expects.
     * 
     * @return the expected engine version.
     */
    String expectedEngineVersion();

    /**
     * Get the supported block type ids.
     * 
     * @return the supported block type ids.
     */
    List<String> getSupportedBlockTypeIds();

    /**
     * Build a block.
     * 
     * @param blockTypeId - the block type id.
     * @param args - the arguments.
     * @return the block.
     */
    Block buildBlock(final String blockTypeId, final Object... args);
}
