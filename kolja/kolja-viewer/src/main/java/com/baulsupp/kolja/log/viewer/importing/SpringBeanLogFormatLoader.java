package com.baulsupp.kolja.log.viewer.importing;

import java.beans.PropertyEditor;
import java.text.DateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.CustomEditorConfigurer;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.baulsupp.kolja.log.viewer.columns.ColumnWidths;
import com.baulsupp.kolja.log.viewer.columns.ColumnWidthsPropertyEditor;
import com.baulsupp.kolja.util.DateFormatPropertyEditor;
import com.baulsupp.kolja.util.PatternPropertyEditor;

public class SpringBeanLogFormatLoader {
  public static final LogFormat load(Resource r) {
    XmlBeanFactory beanFactory = loadBeanFactory(r);
    
    return (LogFormat) beanFactory.getBean("logFormat");
  }

  public static XmlBeanFactory loadBeanFactory(Resource r) {
    XmlBeanFactory beanFactory = new XmlBeanFactory(r);
    
    try {
      if (beanFactory.containsBean("customEditorConfigurer")) {
        CustomEditorConfigurer configurer = (CustomEditorConfigurer) beanFactory.getBean("customEditorConfigurer");    
        configurer.postProcessBeanFactory(beanFactory);
      }
    } catch (NoSuchBeanDefinitionException nsbde) {
      throw new RuntimeException(nsbde);
    }

    Map<Class, PropertyEditor> map = new HashMap<Class, PropertyEditor>();
    map.put(Pattern.class, new PatternPropertyEditor());
    map.put(DateFormat.class, new DateFormatPropertyEditor());
    map.put(ColumnWidths.class, new ColumnWidthsPropertyEditor());

    CustomEditorConfigurer configurer = new CustomEditorConfigurer();
    configurer.setCustomEditors(map);  
    configurer.postProcessBeanFactory(beanFactory);
    return beanFactory;
  }

  public static BeanFactory loadBeanFactory(String string) {
    return loadBeanFactory(new ClassPathResource(string));
  }
}
