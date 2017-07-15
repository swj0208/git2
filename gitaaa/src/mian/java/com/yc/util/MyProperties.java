package com.yc.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 自定义MyProperties类继承Properies，当前类拥有Properties公共属性和方法
 * 
 * @author Administrator
 *整个类只需要创建一个对象
 *设计成单例模式
 */



public class MyProperties extends Properties{

	private static final long serialVersionUID = 5107251348456879523L;
	private static MyProperties myProperties;
	
	private MyProperties(){
		InputStream in = MyProperties.class.getClassLoader().getResourceAsStream("db.properties");
		
		try {
			this.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static MyProperties getInstance() throws IOException{
		if(null==myProperties){
			myProperties = new MyProperties();
		}
		return myProperties;
	}
	
	
	
}
