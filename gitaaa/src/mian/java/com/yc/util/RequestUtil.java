package com.yc.util;

import java.lang.reflect.Method;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

public class  RequestUtil<T> {
	/**
	 * @param map
	 * @param c
	 * @return
	 * @throws Exception
	 */
	public static <T> T getParemeter(Map<String,Object> map ,Class<T> c) throws Exception{
		T obj = c.newInstance();
		List<Method> setMethods = getAllSetMethods(c);
		List<String> params = getAllParameters(setMethods);//setUname->uname
		for(String p:params){
			if( map.get(p)==null){ 
				continue;
			}
			String value = map.get(p).toString();
			if(value !=null && !"".equals(value)){
				for(Method m :setMethods){
					if(m.getName().equalsIgnoreCase("set"+p)){
						Class typeClass = m.getParameterTypes()[0];
						String typeClassName = typeClass.getName();
						if("int".equals(typeClassName)|| "java.lang.Integer".equals(typeClassName)){
							int v = Integer.parseInt(value);
							m.invoke(obj, v);
						}else if("float".equals(typeClassName)|| "java.lang.Float".equals(typeClassName)){
							float v = Float.parseFloat(value);
							m.invoke(obj, v);
						}else if("double".equals(typeClassName)|| "java.lang.Double".equals(typeClassName)){
							double v = Double.parseDouble(value);
							m.invoke(obj, v);
						}else if("Date".equals(typeClassName)||"java.sql.Date".equals(typeClassName)){
							Date v = Date.valueOf(value);
							m.invoke(obj,v );
						}else if("Date".equals(typeClassName)||"java.util.Date".equals(typeClassName)){
							Date v = Date.valueOf(value);
							m.invoke(obj,v );
						}else{
							m.invoke(obj, value);  
						}
					}
				}
			}
		}
		return obj;
		
	}

	/**
	 * 从request中取出所有的参数，将参数值存到一个object对象
	 * @param request
	 * @param c
	 * @return
	 * @throws Exception
	 */
	public static <T> T getParemeter(HttpServletRequest request ,Class<T> c) throws Exception{
		//根据c来创建一个对象，这个对象用于存request中的所有值
		T obj = c.newInstance();//User obj=new User();
		//取出c中的所有的set方法
		List<Method> setMethods = getAllSetMethods(c);
		//将setMethods中的每个方法的set去掉，并将首字母转小写，存到一个集合中setUname()->uname
		//setEmpSal() ->empSal
		List<String> params = getAllParameters(setMethods);
		//从request中取出所有的参数
		for(String p:params){
			String value = request.getParameter(p);
			//System.out.println(value);
			if(value !=null && !"".equals(value)){
				for(Method m :setMethods){
					//判读那是哪一个set方法要运行起来
					if(m.getName().equalsIgnoreCase("set"+p)){
						//还需要判断m这个setXXX（参数类型）
						//判断参数类型
						Class typeClass = m.getParameterTypes()[0];//因为set方法是标准的javabean方法，它的参数有且只有一个
						String typeClassName = typeClass.getName();
						if("int".equals(typeClassName)|| "java.lang.Integer".equals(typeClassName)){
							int v = Integer.parseInt(value);
							m.invoke(obj, v);
						}else if("float".equals(typeClassName)|| "java.lang.Float".equals(typeClassName)){
							float v = Float.parseFloat(value);
							m.invoke(obj, v);
						}else if("double".equals(typeClassName)|| "java.lang.Double".equals(typeClassName)){
							double v = Double.parseDouble(value);
							m.invoke(obj, v);
						}else if("Date".equals(typeClassName)||"java.sql.Date".equals(typeClassName)){
							Date v = Date.valueOf(value);
							m.invoke(obj,v );
						}else if("Date".equals(typeClassName)||"java.util.Date".equals(typeClassName)){
							Date v = Date.valueOf(value);
							m.invoke(obj,v );
						}else{
							m.invoke(obj, value);
						}
					}
				}
			}
		}
		return obj;
	}
	
	private static List<String> getAllParameters(List<Method> setMethods){
		List<String > list = new ArrayList<String>();
		for(Method m:setMethods){
			String pname = m.getName().substring(3, m.getName().length());
			pname = pname.substring(0, 1).toLowerCase() + pname.substring(1);
			list.add(pname);
		}
		return list;
	}
	
	/**
	 * 取出一个类中所有的set方法
	 * @param c
	 * @return
	 */
	private static <T> List<Method> getAllSetMethods(Class<T> c){
		List<Method> setMethods = new ArrayList<Method>();
		if(c!=null){
			Method[] ms = c.getMethods();//取出User类中所有方法，但我只要set
			if(ms!=null){
				for(Method m:ms){
					if(m.getName().startsWith("set")){//如果方法名以set开头，则保存到
						setMethods.add(m);				//setMethods中
					}
				}
			}
		}
		return setMethods;
	}
	
	/**
	 * 取出一个类中所有的get方法
	 * @param c
	 * @return
	 */
	private static <T> List<Method> getAllGetMethods(Class<T> c){
		List<Method> setMethods = new ArrayList<Method>();
		if(c!=null){
			Method[] ms = c.getMethods();//取出User类中所有方法，但我只要set
			if(ms!=null){
				for(Method m:ms){
					if(m.getName().startsWith("get")){//如果方法名以set开头，则保存到
						setMethods.add(m);				//setMethods中
					}
				}
			}
		}
		return setMethods;
	}
	
}
