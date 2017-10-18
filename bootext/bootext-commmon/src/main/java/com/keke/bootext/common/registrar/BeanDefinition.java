package com.keke.bootext.common.registrar;

import com.keke.bootext.common.enums.ExceptionEnum;
import com.keke.bootext.common.exception.ErrorCodeException;
import org.springframework.beans.factory.config.RuntimeBeanNameReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeanDefinition<T> {

    private Class<T> beanClass;//class对象
    private String beanName;//bean名称
    private List<String> dependsOnList;//dependsOn列表
    private Map<String,Object> properties;//属性map
    private List<Object> constructorArgumentValues;//构造器参数值
    private String initMethodName;
    private String destroyMethodName;

    private BeanDefinition(){}
    public static <T> BeanDefinition<T> newInstance(Class<T> t){
        BeanDefinition<T> beanDefinition = new BeanDefinition<>();
        beanDefinition.setBeanClass(t);
        return beanDefinition;
    }

    public String getBeanName() {
        return beanName;
    }

    public BeanDefinition setBeanName(String beanName) {
        this.beanName = beanName;
        return this;
    }

    public Class<T> getBeanClass(){
        return beanClass;
    }
    public BeanDefinition setBeanClass(Class<T> beanClass){
        this.beanClass=beanClass;
        return this;
    }

    public BeanDefinition addNullPropertyValue(String name){
        if(properties==null){
            properties = new HashMap<>();
        }
        properties.put(name,null);
        return this;
    }
    public BeanDefinition addPropertyValue(String name, Object value){
        if(value==null){
            return this;
        }
        if(properties==null){
            properties = new HashMap<>();
        }
        properties.put(name,value);
        return this;
    }
    public BeanDefinition addPropertyReference(String name, String beanName){
        if(properties==null){
            properties = new HashMap<>();
        }
        properties.put(name,new RuntimeBeanNameReference(beanName));
        return this;
    }
    public BeanDefinition addNullConstructorArgValue(){
        if(constructorArgumentValues==null){
            constructorArgumentValues = new ArrayList<>();
        }
        constructorArgumentValues.add(null);
        return this;
    }

    public BeanDefinition addConstructorArgValue(Object... value){
        if(constructorArgumentValues==null){
            constructorArgumentValues = new ArrayList<>();
        }
        if(value==null){
            return this;
        }
        for (Object o : value) {
            constructorArgumentValues.add(o);
        }
        return this;
    }
    public BeanDefinition addConstructorArgReference(String... beanName){
        if(constructorArgumentValues==null){
            constructorArgumentValues = new ArrayList<>();
        }
        if(beanName==null){
            throw new ErrorCodeException(ExceptionEnum.PARAMETER_EXCEPTION,"BeanDefinition.addConstructorArgValue",beanName);
        }
        for (String s : beanName) {
            constructorArgumentValues.add(new RuntimeBeanNameReference(s));
        }
        return this;
    }

    public List<String> getDependsOnList() {
        return dependsOnList;
    }

    public BeanDefinition addDependsOn(String dependsOn) {
        if(dependsOnList==null){
            this.dependsOnList=new ArrayList<>();
        }
        this.dependsOnList.add(dependsOn);
        return this;
    }

    public Map<String,Object> getProperties() {
        return properties;
    }


    public List<Object> getConstructorArgumentValues() {
        return constructorArgumentValues;
    }


    public String getInitMethodName() {
        return initMethodName;
    }

    public BeanDefinition setInitMethodName(String initMethodName) {
        this.initMethodName = initMethodName;
        return this;
    }

    public String getDestroyMethodName() {
        return destroyMethodName;
    }

    public BeanDefinition setDestroyMethodName(String destroyMethodName) {
        this.destroyMethodName = destroyMethodName;
        return this;
    }
}
