import java.util.concurrent.TimeUnit;

import cn.teaey.apns4j.Apns4j;
import cn.teaey.apns4j.network.ApnsChannel;
import cn.teaey.apns4j.network.ApnsChannelFactory;
import cn.teaey.apns4j.network.ApnsGateway;
import cn.teaey.apns4j.network.async.ApnsService;
import cn.teaey.apns4j.protocol.ApnsPayload;




/**
 * 推送 苹果系统的消息  APNS 
 * @author 郭栋2017-03-22
 *
 */
public class PushApnsMsg {
	private static ApnsChannelFactory apnsChannelFactory ;
	private static ApnsChannel apnsChannel;
	private static ApnsService apnsService;
    
	/**
	 * 构造时，传入环境变量
	 * @param gateway（ApnsGateway.DEVELOPMENT 测试环境；ApnsGateway.PRODUCTION 生产环境）
	 */
	public PushApnsMsg(ApnsGateway gateway ,String keyStorePath){
		//String keyStorePwd = PropertiesManager.getProperties("global.properties").getProperty("push.apns.keyStorePwd");
		String keyStorePwd = "01zhuanche";
		apnsChannelFactory = Apns4j.newChannelFactoryBuilder().keyStoreMeta(keyStorePath).keyStorePwd(keyStorePwd).apnsGateway(gateway).build();
		apnsChannel = apnsChannelFactory.newChannel(keyStorePath,keyStorePwd);
		apnsService = new ApnsService(3, apnsChannelFactory, 3 ,keyStorePath,keyStorePwd);
	}
	
	/**
     * 开始发送
     * @param title
     * @param body
     * @param msgbody
     */
    public void sendApns(String deviceToken, String title, String body, String msgbody){
        ApnsPayload apnsPayload = Apns4j.newPayload()
                .alertTitle(title)
                .alertBody(body)
                .extend("msgbody", msgbody)
                .sound("default");
         apnsChannel.send(deviceToken, apnsPayload);
    }
    
    /**
     * 关闭苹果推送通道
     */
    public void shutdownApns(){
    	apnsService.shutdown(3, TimeUnit.SECONDS);
    }
    
    public static void main(String[] args) {
    	PushApnsMsg push = new PushApnsMsg(ApnsGateway.DEVELOPMENT,"/");
    	for(int i=0; i<5; i++){
    		push.sendApns("2e2886afe33beb90558ebb0fe51c4449d4b2e749c82ea60477a0ab4c4588a9cb",
    				"",
    				i+"试试没有title会怎样？这是消息内容，看不懂也没关系。我就是试试。", 
    				"{\\\"msgId\\\":\\\"111\\\",\\\"type\\\":\\\"1\\\",\\\"title\\\":\\\"测试广告1\\\",\\\"url\\\":\\\"http://www.baidu.com\\\",\\\"status\\\":\\\"1\\\",\\\"content\\\":\\\"这是一个测试广告，we\\\"}");
    	}
    	push.shutdownApns();
    }
    
  //代码同步 by:郭栋  2017年4月12日
}
