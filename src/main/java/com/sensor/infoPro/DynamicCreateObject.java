package com.sensor.infoPro;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.Modifier;

public class DynamicCreateObject {

	
	/**
     * 给类 添加类属性
     * @param className 需要创建的java类的名称
     * @param fieldMap  字段-字段值的属性map，需要添加的属性
     * @return
     * */
     public Object addField(String className,Map<String,Object> fieldMap) throws Exception {
    	 
    	  ClassPool pool = ClassPool.getDefault();//获取javassist类池
	        CtClass ctClass = pool.makeClass(className);//创建javassist类
	        
	        // 为创建的类ctClass添加属性
	        Iterator it = fieldMap.entrySet().iterator();
	        while (it.hasNext()) {  // 遍历所有的属性
	            Map.Entry entry = (Map.Entry) it.next();
	            String fieldName = (String)entry.getKey();
	            Object fieldValue = entry.getValue();
	            // 增加属性，这里仅仅是增加属性字段
	            String fieldType = fieldValue.getClass().getName();
	            CtField ctField = new CtField(pool.get(fieldType),fieldName, ctClass);
	            ctField.setModifiers(Modifier.STATIC );
	            ctField.setModifiers(Modifier.PUBLIC );
	            ctClass.addField(ctField);
	        }
	      //添加构造器
	        CtConstructor constructor = new CtConstructor(new CtClass[]{},ctClass);
	        constructor.setBody("{}");
	        ctClass.addConstructor(constructor);
	         Class c=ctClass.toClass();// 为创建的javassist类转换为java类
	        Object newObject = c.newInstance();// 为创建java对象
	 
	         // 为创建的类newObject属性赋值
	        it = fieldMap.entrySet().iterator();
	        while (it.hasNext()) {  // 遍历所有的属性
	            Map.Entry entry = (Map.Entry) it.next();
	            String fieldName = (String)entry.getKey();
	            Object fieldValue = entry.getValue();
	            // 为属性赋值
	            this.setFieldValue(newObject,fieldName,fieldValue);
	        }
		return  newObject;

        
}

	/**
      * 给对象属性赋值
      * @param dObject
      * @param fieldName
      * @param val
      * @return
      */
     public  Object setFieldValue(Object dObject, String fieldName, Object val) {
         Object result = null;
         try {
           Field fu = dObject.getClass().getDeclaredField(fieldName); // 获取对象的属性域
             try {
                 fu.setAccessible(true); // 设置对象属性域的访问属性
                 fu.set(dObject,val); // 设置对象属性域的属性值
                 result = fu.get(dObject); // 获取对象属性域的属性值
             } catch (IllegalAccessException e) {
                 e.printStackTrace();
             }
         } catch (NoSuchFieldException e) {
             e.printStackTrace();
         }
         return result;
     }

     /**
      * 获取对象属性赋值
      * @param dObject
      * @param fieldName 字段别名
      * @return
      */
     public  Object getFieldValue(Object dObject, String fieldName) {
         Object result = null;
         try {
           Field fu = dObject.getClass().getDeclaredField(fieldName); // 获取对象的属性域
             try {
                 fu.setAccessible(true); // 设置对象属性域的访问属性
                 result = fu.get(dObject); // 获取对象属性域的属性值
             } catch (IllegalAccessException e) {
                 e.printStackTrace();
             }
         } catch (NoSuchFieldException e) {
             e.printStackTrace();
         }
         return result;
     }
  
	     			
}
