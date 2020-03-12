package test.lxzl;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
public class SampleMail {
    private static final String ALIDM_SMTP_HOST = "smtpdm.aliyun.com";
//    private static final String ALIDM_SMTP_PORT = "25";// 或25 "80"
    public static void main(String[] args) {
        // c配置发送邮件的环境属性
        final Properties props = new Properties();
        // c表示SMTP发送邮件，需要进行身份验证
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", ALIDM_SMTP_HOST);
//        props.put("mail.smtp.port", ALIDM_SMTP_PORT);
        // c如果使用ssl，则去掉使用25端口的配置，进行如下配置,
         props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
         props.put("mail.smtp.socketFactory.port", "465");
         props.put("mail.smtp.port", "465");
        // c发件人的账号，填写控制台配置的发信地址,比如xxx@xxx.com
        props.put("mail.user", "dailyreport@toyotareport.saas-b.accenture.cn");
        // c访问SMTP服务时需要提供的密码(在控制台选择发信地址进行设置)
        props.put("mail.password", "123qweASDz");
        // c构建授权信息，用于进行SMTP进行身份验证
        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                // c用户名、密码
                String userName = props.getProperty("mail.user");
                String password = props.getProperty("mail.password");
                return new PasswordAuthentication(userName, password);
            }
        };
        // c使用环境属性和授权信息，创建邮件会话
        Session mailSession = Session.getInstance(props, authenticator);
//        mailSession.setDebug(true);
        //UUID uuid = UUID.randomUUID();
        //final String messageIDValue = "<" + uuid.toString() + ">";
        // c创建邮件消息
        MimeMessage message = new MimeMessage(mailSession){
            //@Override
            //protected void updateMessageID() throws MessagingException {
                //c设置自定义Message-ID值
                //setHeader("Message-ID", messageIDValue);
            //}
        };
        try {
        // c设置发件人邮件地址和名称。填写控制台配置的发信地址,比如xxx@xxx.com。和上面的mail.user保持一致。名称用户可以自定义填写。
        InternetAddress from = new InternetAddress("dailyreport@toyotareport.saas-b.accenture.cn", "DailyReporter");
        message.setFrom(from);
        //c可选。设置回信地址
//        Address[] a = new Address[1];
//        a[0] = new InternetAddress("***");
//        message.setReplyTo(a);
        // c设置收件人邮件地址，比如yyy@yyy.com
        InternetAddress to = new InternetAddress("nannan.c.wang@accenture.com");
        message.setRecipient(MimeMessage.RecipientType.TO, to);
        //c如果同时发给多人，才将上面两行替换为如下（因为部分收信系统的一些限制，尽量每次投递给一个人；同时我们限制单次允许发送的人数是30人）：
        //InternetAddress[] adds = new InternetAddress[2];
        //adds[0] = new InternetAddress("xxxxx@qq.com");
        //adds[1] = new InternetAddress("xxxxx@qq.com");
        //message.setRecipients(Message.RecipientType.TO, adds);
        String ccUser = "348436272@qq.com";
        // c设置多个抄送地址
        if(null != ccUser && !ccUser.isEmpty()){
            @SuppressWarnings("static-access")
            InternetAddress[] internetAddressCC = new InternetAddress().parse(ccUser);
            message.setRecipients(Message.RecipientType.CC, internetAddressCC);
        }
//            String bccUser = "密送邮箱";
//            // c设置多个密送地址
//            if(null != bccUser && !bccUser.isEmpty()){
//                @SuppressWarnings("static-access")
//                InternetAddress[] internetAddressBCC = new InternetAddress().parse(bccUser);
//                message.setRecipients(Message.RecipientType.BCC, internetAddressBCC);
//            }
        // c设置邮件标题
        message.setSubject("测试邮件");
        // c设置邮件的内容体
        message.setContent("测试的HTML邮件", "text/html;charset=UTF-8");
        //c若需要开启邮件跟踪服务，请使用以下代码设置跟踪链接头。首先域名需要备案，设置且已正确解析了CNAME配置；其次发信需要打Tag，此Tag在控制台已创建并存在，Tag创建10分钟后方可使用；
        //String tagName = "Test";
        //HashMap<String, String> trace = new HashMap<>();
        //trace.put("OpenTrace", "1");
        //trace.put("TagName", tagName);
        //String jsonTrace = JSON.toJSONString(trace);
        //String base64Trace = new String(Base64.encodeBase64(jsonTrace.getBytes()));
        //c设置跟踪链接头
        //message.addHeader("X-AliDM-Trace", base64Trace);
        // c发送附件，总的邮件大小不超过15M，创建消息部分
        BodyPart messageBodyPart = new MimeBodyPart();
        // c消息
        String messages = "各位\r\n" + 
        		"\r\n" + 
        		"お世話になっております。\r\n" + 
        		"\r\n" + 
        		"2020年1月22日までのWeChatミニプログラムでの試乗予約実績レポートを送付いたします。\r\n" + 
        		"ご確認の程、よろしくお願いいたします。\r\n" + 
        		"\r\n" + 
        		"ちなみに、レポート形式を戻した。\r\n" + 
        		"ご了承ください。\r\n" + 
        		"\r\n" + 
        		"以上、よろしくお願いいたします。\r\n" + 
        		"\r\n" + 
        		"NOTE: This is an automated email message. Please do not reply.";
        messageBodyPart.setText("各位\r\n" + 
        		"\r\n" + 
        		"お世話になっております。\r\n" + 
        		"\r\n" + 
        		"2020年1月13日までのWeChatミニプログラムでの試乗予約実績レポートを送付いたします。\r\n" + 
        		"ご確認の程、よろしくお願いいたします。\r\n" + 
        		"\r\n" + 
        		"以上、よろしくお願いいたします。\r\n" + 
        		"\r\n" + 
        		"Xueping.song\r\n" + 
        		"");
        // c创建多重消息
        Multipart multipart = new MimeMultipart();
        // c设置文本消息部分
        multipart.addBodyPart(messageBodyPart);
        // c附件部分
        messageBodyPart = new MimeBodyPart();
        //c设置要发送附件的文件路径
        String filename = "C:\\wnn\\project\\Toyota\\WorkSpace\\lxzl\\pom.xml";
        FileDataSource source = new FileDataSource(filename);
        messageBodyPart.setDataHandler(new DataHandler(source));
        //c处理附件名称中文（附带文件路径）乱码问题
        messageBodyPart.setFileName(MimeUtility.encodeText("pom.xml"));
        multipart.addBodyPart(messageBodyPart);
        // c发送含有附件的完整消息
        message.setContent(multipart);
        // c发送附件代码，结束
        // c发送邮件
        Transport.send(message);
        }
        catch (MessagingException e) {
            String err = e.getMessage();
            // c在这里处理message内容， 格式是固定的
            System.out.println(err);
        } catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}