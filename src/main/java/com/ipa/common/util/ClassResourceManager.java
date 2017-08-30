package com.ipa.common.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.apache.log4j.Logger;


public class ClassResourceManager {

  public static final String CLASS_RESOURCE_TAG = "ClassResourceConfig";
  public final String PACKAGE_RESOURCE_TAG = "PackageResourceConfig";
  public final String PACKAGE_NAME_ATT = "packageName";
  public final String CLASS_TAG = "ClassConfig";
  public final String CLASS_KEY_ATT = "classKey";
  public final String CLASS_NAME_ATT = "className";

  public static final class ClassResourcePackages{
    public static final String DATA_REFERENCE= "DATA_REFERENCE";
    public static final String DATA_SOURCE= "DATA_SOURCE";
    public static final String DATA_INPUT= "DATA_INPUT";
    public static final String DATA_FACTORY= "DATA_FACTORY";
    public static final String DATA_BUILDER= "DATA_BUILDER";
    public static final String DATA_BROWSER_BUILDER = "DATA_BROWSER_BUILDER";
    public static final String DATA_BROWSER_INFO = "DATA_BROWSER_INFO";
    public static final String PATTERNS_PARSER = "PATTERNS_PARSER";
  }

  static Logger log = Logger.getLogger(ClassResourceManager.class);
  
  public static ClassLoader classLoader = null;

  public ClassResourceManager() {  	
  }

  public static ClassLoader getClassLoader() {
  	if (classLoader == null){
  		synchronized (ClassResourceManager.class) {
  			if (classLoader == null){
  				classLoader = ClassResourceManager.class.getClassLoader();
  			}
			}  		
  	}
  	return classLoader; 
  }


  public static Object createObject(String className){
    try{    	
      Object obj = getClassLoader().loadClass(className).newInstance();
      return obj;
    }catch(Exception e){
      log.error("Failed to load class " + className,new Throwable());         
      return null;
    }
  }
  
  public static Object createObject(String className,ClassLoader loader){
    try{
    	if (loader == null){
    		loader = ClassResourceManager.class.getClassLoader();
    	}
      Object obj = loader.loadClass(className).newInstance();
      return obj;
    }catch(Exception e){
      log.error("Failed to load class " + className,new Throwable());         
      return null;
    }
  }
  
  public static Class createClassObject(String className){
    try{
      Class c = getClassLoader().loadClass(className);
      return c;
    }catch(Exception e){
      log.error("Failed to load class " + className,new Throwable());
      return null;
    }
  }
  
  public static Class createClassObject(String className,ClassLoader loader){
    try{
    	if (loader == null){
    		loader = ClassResourceManager.class.getClassLoader();
    	}
      Class c = loader.loadClass(className);
      return c;
    }catch(Exception e){
      log.error("Failed to load class " + className,new Throwable());
      return null;
    }
  }
  
  public static Object createObject(String className, Object[] initArgs, Class[] paramTypes){
    try{    	
      Class c = getClassLoader().loadClass(className);
      Constructor constructor = c.getDeclaredConstructor(paramTypes);
      Object obj = constructor.newInstance(initArgs);
      return obj;
    }catch(Exception e){
      log.error("Failed to load class " + className,new Throwable());      
      return null;
    }
  }
  
  public static Object createSingleton(String className){
  	try {
  		
  		Class cls = getClassLoader().loadClass(className);
  		Method[] methods = cls.getDeclaredMethods();
  		for (int i=0; i<methods.length; i++){
  			if (methods[i].getName().equals("getInstance")){
  				return methods[i].invoke(null,null);
  			}
  		}
  		return null;
  		
  	}catch(Exception e){
  		log.error("Failed to load class " + className,new Throwable());  		
  		return null;
  	}
  }

 
}