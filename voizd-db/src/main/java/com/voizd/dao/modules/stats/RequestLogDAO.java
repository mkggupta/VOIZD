/**
 * 
 */
package com.voizd.dao.modules.stats;

import com.voizd.dao.entities.RequestLog;
import com.voizd.dao.exception.DataUpdateFailedException;

/**
 * @author Manish
 * 
 */
public interface RequestLogDAO {

	void saveRequestLog(RequestLog requestLog) throws DataUpdateFailedException;
}
