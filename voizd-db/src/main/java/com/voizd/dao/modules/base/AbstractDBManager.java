package com.voizd.dao.modules.base;

import com.ibatis.sqlmap.client.SqlMapClient;

public class AbstractDBManager {

	protected SqlMapClient sqlMapClient_;
	protected SqlMapClient sqlMapClientSlave_;

	protected SqlMapClient statsSqlMapClient_;
	protected SqlMapClient statsSqlMapClientSlave_;

	public SqlMapClient getSqlMapClient_() {
		return sqlMapClient_;
	}

	public void setSqlMapClient_(SqlMapClient sqlMapClient_) {
		this.sqlMapClient_ = sqlMapClient_;
	}

	public SqlMapClient getSqlMapClientSlave_() {
		return sqlMapClientSlave_;
	}

	public void setSqlMapClientSlave_(SqlMapClient sqlMapClientSlave_) {
		this.sqlMapClientSlave_ = sqlMapClientSlave_;
	}

	/**
	 * @return the statsSqlMapClient_
	 */
	public SqlMapClient getStatsSqlMapClient_() {
		return statsSqlMapClient_;
	}

	/**
	 * @param statsSqlMapClient_
	 *            the statsSqlMapClient_ to set
	 */
	public void setStatsSqlMapClient_(SqlMapClient statsSqlMapClient_) {
		this.statsSqlMapClient_ = statsSqlMapClient_;
	}

	/**
	 * @return the statsSqlMapClientSlave_
	 */
	public SqlMapClient getStatsSqlMapClientSlave_() {
		return statsSqlMapClientSlave_;
	}

	/**
	 * @param statsSqlMapClientSlave_
	 *            the statsSqlMapClientSlave_ to set
	 */
	public void setStatsSqlMapClientSlave_(SqlMapClient statsSqlMapClientSlave_) {
		this.statsSqlMapClientSlave_ = statsSqlMapClientSlave_;
	}

}