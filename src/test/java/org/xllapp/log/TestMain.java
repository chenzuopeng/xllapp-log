package org.xllapp.log;

import java.net.InterfaceAddress;

import javax.management.RuntimeErrorException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 * @Copyright: Copyright (c) 2013 FFCS All Rights Reserved 
 * @Company: 北京福富软件有限公司 
 * @author 陈作朋 Dec 27, 2013
 * @version 1.00.00
 * @history:
 * 
 */
public class TestMain {

	public static void test() throws Exception{
		throw new Exception("aaa", new Throwable("bbb")){};
	}
	
	public static void test1(){
		  throw new RuntimeException("RuntimeException",new Throwable("ccc"));
	}

	public static void main(String[] args) throws InterruptedException {
         final Logger logger=LoggerFactory.getLogger(TestMain.class);
/*         logger.debug("DEBUG");
         logger.info("INFO");*/
//         logger.error("ERROR-{}","123");
        try {
//			test();
		} catch (Exception e) {
			logger.error("", e);
//			logger.error("1111{}", "22");
		}
         
//			test1();
			
         Thread thread=new Thread(){

				@Override
				public void run() {
					int len=30;
					
					for (int j = 0; j < len; j++) {
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					throw new RuntimeErrorException(null,"123");
					
				}
				
			};
			thread.start();

			System.out.println(thread.getState());
			Thread.sleep(999999);
	}

}

