package com.voizd.modules.user.tap.service;

import com.voizd.common.beans.vo.TapClipVO;

public interface TapService {
	public boolean init() throws Exception;

	public void doTask(TapClipVO tapClipVO,String command) throws Exception;
}
