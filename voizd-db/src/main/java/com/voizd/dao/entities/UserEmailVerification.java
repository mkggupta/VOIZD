/**
 * 
 */
package com.voizd.dao.entities;

import java.util.Date;


/**
 * @author Manish
 * 
 */
public class UserEmailVerification {

	private long id;
	private long userId;
	private String emailId;
	private String verificationCode;
	private int status;
	private Date createdDate;
	private Date expiryDate;

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the userId
	 */
	public long getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(long userId) {
		this.userId = userId;
	}

	/**
	 * @return the emailId
	 */
	public String getEmailId() {
		return emailId;
	}

	/**
	 * @param emailId
	 *            the emailId to set
	 */
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	/**
	 * @return the verificationCode
	 */
	public String getVerificationCode() {
		return verificationCode;
	}

	/**
	 * @param verificationCode
	 *            the verificationCode to set
	 */
	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * @return the createdDate
	 */
	public Date getCreatedDate() {
		return createdDate;
	}

	/**
	 * @param createdDate
	 *            the createdDate to set
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * @return the expiryDate
	 */
	public Date getExpiryDate() {
		return expiryDate;
	}

	/**
	 * @param expiryDate
	 *            the expiryDate to set
	 */
	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	/**
	 * @param userId
	 * @param emailId
	 * @param verificationCode
	 * @param status
	 * @param createdDate
	 * @param expiryDate
	 */
	public UserEmailVerification(long userId, String emailId, String verificationCode, int status, Date createdDate, Date expiryDate) {
		super();
		this.userId = userId;
		this.emailId = emailId;
		this.verificationCode = verificationCode;
		this.status = status;
		this.createdDate = createdDate;
		this.expiryDate = expiryDate;
	}

	/**
	 * 
	 */
	public UserEmailVerification() {
		super();
	}
	
	/*public static void getDate(){
		Date date= new Date();
		// Use the Calendar class to subtract one day
		 DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		 
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_YEAR,-10);
		Date oneDayBefore= cal.getTime();
		System.out.println("getDate() :: "+oneDayBefore);
		String result = dateFormat.format(oneDayBefore);
		try {
			Date myDate = dateFormat.parse(result);
			if(date.before(oneDayBefore)){
				
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		///subtracting year from Date
		Calendar cal = Calendar.getInstance();
        //cal.setTimeZone(TimeZone.getTimeZone("GMT"));
		Date date= new Date();
		cal.setTime(date);
        cal.add(Calendar.YEAR, -5);
        System.out.println("date before 5 years : " + getDate(cal));

	}
	
	public static String getDate(Calendar cal){
        return "" + cal.get(Calendar.DATE) +"/" +
                (cal.get(Calendar.MONTH)+1) + "/" + cal.get(Calendar.YEAR);
    }

	public static void main(String arg[]){
		getDate();
	}*/
}
