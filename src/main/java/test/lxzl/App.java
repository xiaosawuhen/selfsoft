package test.lxzl;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        String ccUser = "348436272@qq.com,nannan.c.wang@accenture.com";
        // c设置多个抄送地址
            try {
				InternetAddress[] internetAddressCC = new InternetAddress().parse(ccUser);
				for (int i = 0; i < internetAddressCC.length; i++) {
					System.out.println(internetAddressCC[i].getAddress());
				}
			} catch (AddressException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
       String alidmSmtpMailToUser = "123";

			String[] toAddressArray = alidmSmtpMailToUser.split(",");
			System.out.println(toAddressArray.length);
			for (int i = 0; i < toAddressArray.length; i++) {
			System.out.println("==" + toAddressArray[i]);	
			}
    }
}
