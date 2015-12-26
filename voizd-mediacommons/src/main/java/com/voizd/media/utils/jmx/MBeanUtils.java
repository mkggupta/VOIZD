package com.voizd.media.utils.jmx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.*;
import java.lang.management.ManagementFactory;
import java.util.Hashtable;

import static com.voizd.media.dataobject.MediaConstants.*;

/**
 * User: shalabh
 * Date: 1/8/12
 * Time: 2:16 PM
 */
public abstract class MBeanUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(MBeanUtils.class);

    public static void registerMBean(String moduleName, String subModuleName, String domainName, Object mbean)
            throws MBeanRegistrationException, InstanceAlreadyExistsException, NotCompliantMBeanException, MalformedObjectNameException {
        Hashtable<String, String> keyValTable = new Hashtable<String, String>();
        keyValTable.put(MBEAN_MODULE_KEY, moduleName);
        keyValTable.put(MBEAN_SUBMODULE_KEY, subModuleName);
        keyValTable.put(MBEAN_DOMAIN_KEY, domainName);
        ObjectName objectName = new ObjectName(MBEAN_ROCKETALK_DOMAIN_NAME, keyValTable);
        LOGGER.info("Registering MBean for Media Service with name " + objectName);
        MBeanServer mbeanServer = ManagementFactory.getPlatformMBeanServer();
        mbeanServer.registerMBean(mbean, objectName);
    }
}
