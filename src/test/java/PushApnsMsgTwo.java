import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dbay.apns4j.IApnsService;
import com.dbay.apns4j.impl.ApnsServiceImpl;
import com.dbay.apns4j.model.ApnsConfig;
import com.dbay.apns4j.model.Feedback;
import com.dbay.apns4j.model.Payload;
import com.zhuanche.util.PropertiesManager;



/**
 * 推送 苹果系统的消息  APNS 
 * @author 郭栋2017-03-22
 *
 */
public class PushApnsMsgTwo {
	
	private static final Logger logger = LoggerFactory.getLogger(PushApnsMsgTwo.class);
	
	private static IApnsService apnsService;
    
	private IApnsService getApnsService(Boolean devEnv , String p12file) {
		if (apnsService == null) {
			try {
				String keyStorePwd = PropertiesManager.getProperties("global.properties").getProperty("push.apns.keyStorePwd");
				ApnsConfig config = new ApnsConfig();
				InputStream is = new FileInputStream(new File(p12file));
				config.setKeyStore(is);
				config.setDevEnv(devEnv);
				config.setPassword(keyStorePwd);
				config.setPoolSize(3);
				apnsService = ApnsServiceImpl.createInstance(config);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		return apnsService;
	}
	
	
	/**
     * 开始发送
     * @param title
     * @param body
     * @param msgbody
     */
    public void sendApns(String deviceToken, String title, String body, String msgbody,Boolean devEnv , String p12file){
    	IApnsService service = getApnsService(devEnv,p12file);
    	Payload payload = new Payload();
    	payload.setAlertBody(body);
    	payload.setAlertTitle(title);
		payload.setSound("default");
		payload.addParam("msgbody", msgbody);
		service.sendNotification(deviceToken, payload);
		List<Feedback> list = service.getFeedbacks();
		if (list != null && list.size() > 0) {
			for (Feedback feedback : list) {
				logger.info("[apns2发送消息]循环Feedback：" + feedback.getDate() + " ： " + feedback.getToken());
			}
		}
    }
    

}
