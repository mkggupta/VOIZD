/**
 * 
 */
package com.voizd.appstats.core.service.user.v1_0;

/**
 * @author Suresh
 * 
 */
public interface UserStatService {

	void addLoginStats();

	void addRegistrationStats();

	void addLogoutStats();

	void addChangePasswordStats();

	void addResetPasswordStats();

	void addVerifyEmailStats();

	void addResetPasswordRequestStats();

	void addInitiateEmailVerificationStats();

	void addUpdateProfileStats();

}
