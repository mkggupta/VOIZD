package com.voizd.modules.push.message.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLSession;

import org.bouncycastle.util.io.Streams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voizd.modules.agent.util.resource.AgentResourceUtils;
import com.voizd.modules.push.message.util.PushNotificationConstants;
import org.apache.commons.lang.StringUtils;

public class AndroidPushMessageServiceImpl implements PushMessageService {
	private static Logger logger = LoggerFactory
			.getLogger(AndroidPushMessageServiceImpl.class);
	private static int waitTimeOut = 15000;

	
	private static Set<String> errorStates;
	private static int retryCount = 1;
	private static String pushurl = "https://android.googleapis.com/gcm/send";
	private static String pushkey = "";
	private final static Pattern ERROR_PATTERN = Pattern.compile("Error=(.*)",
			Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
	public AndroidPushMessageServiceImpl() {
		pushurl = AgentResourceUtils.getSystemConfigProperty("android.pushurl");
		pushkey = AgentResourceUtils.getSystemConfigProperty("android.authorization.key");
		waitTimeOut = Integer.parseInt(AgentResourceUtils.getSystemConfigProperty("android.wait.time"));
		
	}

	@Override
	public boolean pushNotification(Map<String, Object> params) {
		try {
			String deviceKey = (String) params
					.get(PushNotificationConstants.DEVICE_KEY);
			String pushMessage = (String) params
					.get(PushNotificationConstants.PUSH_MESSAGE);
			logger.debug("pushMessage called for Android ::"+pushMessage+"deviceKey ::"+deviceKey);
			
			if(StringUtils.isNotBlank(deviceKey)&& StringUtils.isNotBlank(pushMessage)){
				boolean pushed = pushMessage(deviceKey, pushMessage);
				return pushed;
			}else{
				return false;
			}
		} catch (Exception e) {
			logger.error(
					"Exception while AndroidpushNotification "
							+ e.getLocalizedMessage(), e);
		}
		return false;
	}

	protected boolean pushMessage(String pin, String message) {
		logger.info("pushMessage called for Android.");
		
		boolean pushed = false;

		int count = 0;
		while (count < retryCount) {

			logger.info("Inside while count is " + count + " retryCount " + retryCount);
			URL url;
			HttpsURLConnection connection = null;
			DataOutputStream wr = null;
			try {
				String content = "registration_id="
						+ URLEncoder.encode(pin, "UTF-8") + "&collapse_key="
						+ URLEncoder.encode("0", "UTF-8") + "&data.message="
						+ URLEncoder.encode(message, "UTF-8");
				url = new URL(pushurl);
				connection = (HttpsURLConnection) url.openConnection();
				connection.setHostnameVerifier(new HostnameVerifier() {

					public boolean verify(String hostname, SSLSession session) {
						return true;
					}
				});
				connection.setRequestMethod("POST");
				connection.setReadTimeout(waitTimeOut);
				connection.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded;charset=UTF-8");
				connection.setRequestProperty("Authorization",
						"key=" + pushkey);

				connection.setDoInput(true);
				connection.setDoOutput(true);

				wr = new DataOutputStream(connection.getOutputStream());

				wr.writeBytes(content);
				wr.flush();
				wr.close();

				String response = new String(Streams.readAll(connection
						.getInputStream()));

				logger.info("Response " + response);
				int responseCode = connection.getResponseCode();
				String responseMessage = connection.getResponseMessage();
				logger.info("Status is " + responseCode);
				if (responseCode == 200) {
					count++;
					if ("OK".equalsIgnoreCase(responseMessage)) {
						pushed = true;
						count = retryCount;
						Matcher matcher = ERROR_PATTERN.matcher(response);

						if (matcher.find()) {
							response = matcher.group(1);
							matcher.end();
							if (errorStates.contains(response.trim())) {
								logger.error("push failed due to " + response);

							}
							break;
						} else {
							logger.info("Successfully pushed");
						}
					} else {
						count++;
						logger.error(" Due to some problme could not push "
								+ responseCode);
					}
				}
			} catch (SSLHandshakeException e) {
				logger.error("push failed", e);
				count++;
			} catch (IOException e) {

				count++;
				
				logger.error("push failed  IOException ", e);
			} catch (Exception e) {
				logger.error("push failed", e);
				count++;

			} finally {
				try {
					if (wr != null) {
						wr.close();
					}
					if (connection != null) {
						connection.disconnect();
					}
				} catch (IOException e) {

					logger.error("Push failed", e);
				}
			}
		}
		logger.info("pushMessage finished for Android.");
		return pushed;
	}

/*	public static String getToken() {
		StringBuilder buf = new StringBuilder();
		HttpsURLConnection httpsURLConn = null;
		OutputStreamWriter outStreamWriter = null;
		String strAuthToken = null;

		try {
			URL url = new URL(tokenUrl);
			httpsURLConn = (HttpsURLConnection) url.openConnection();
			httpsURLConn.setDoOutput(true);
			httpsURLConn.setDoInput(true);
			buf.append("accountType").append("=")
					.append((URLEncoder.encode("GOOGLE ", "UTF-8")));
			buf.append("&Email").append("=")
					.append((URLEncoder.encode(appId, "UTF-8")));
			buf.append("&Passwd").append("=")
					.append((URLEncoder.encode(appPassword, "UTF-8")));
			buf.append("&service").append("=")
					.append((URLEncoder.encode("ac2dm", "UTF-8")));
			buf.append("&source")
					.append("=")
					.append((URLEncoder.encode(
							"Rocketalk-RocketalkApplication-1.0", "UTF-8")));

			httpsURLConn.setRequestMethod("POST");
			httpsURLConn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			httpsURLConn.setRequestProperty("Content-Length", buf.toString()
					.getBytes().length + "");

			outStreamWriter = new OutputStreamWriter(
					httpsURLConn.getOutputStream());
			outStreamWriter.write(buf.toString());
			outStreamWriter.flush();

			int code = httpsURLConn.getResponseCode();
			logger.info("code == " + code);
			if (code == 200) {
				BufferedReader in = new BufferedReader(new InputStreamReader(
						httpsURLConn.getInputStream()));
				buf = new StringBuilder();
				String inputLine;
				while ((inputLine = in.readLine()) != null) {
					logger.info("inputLine = " + inputLine);
					if (inputLine.startsWith("Auth=")) {
						strAuthToken = inputLine.substring(5);
					}
					buf.append(inputLine);
					inputLine = null;
				}
				in.close();

			} else {
				logger.error("Failed to get token " + code);
			}
			if (strAuthToken != null) {
				logger.info("Auth Token = Done" + strAuthToken);
			}
		} catch (Exception e) {
			logger.error("Exception occured while getting token. using url "
					+ tokenUrl + " app id " + appId, e);
		} finally {
			try {
				if (outStreamWriter != null) {
					outStreamWriter.close();
				}
				if (httpsURLConn != null) {
					httpsURLConn.disconnect();
				}
			} catch (IOException e) {
				logger.error("Exception  ", e);
			}
		}
		return strAuthToken;
	}*/
}
