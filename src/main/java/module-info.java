/**
 * Module descriptor for flow-forge-sdk.
 * 
 * This module provides SDK for creating executable entities for FlowForge project.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 * @since 2.0.5
 */
module ru.spb.tksoft.flowforge.sdk {

    // Export public API packages
    exports ru.spb.tksoft.flowforge.sdk.contract;
    exports ru.spb.tksoft.flowforge.sdk.enumeration;
    exports ru.spb.tksoft.flowforge.sdk.model;

    // Required modules
    requires jakarta.validation;
    requires org.slf4j;
    requires static lombok;

    requires ru.spb.tksoft.utils.log;
    requires ru.spb.tksoft.common.exceptions;
}

