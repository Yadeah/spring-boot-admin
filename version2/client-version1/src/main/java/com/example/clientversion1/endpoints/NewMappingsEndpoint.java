package com.example.clientversion1.endpoints;

import org.springframework.aop.support.AopUtils;
import org.springframework.boot.actuate.endpoint.RequestMappingEndpoint;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.AbstractHandlerMethodMapping;
import org.springframework.web.servlet.handler.AbstractUrlHandlerMapping;

import java.util.*;

@ConfigurationProperties(prefix = "endpoints.mappings")
public class NewMappingsEndpoint extends RequestMappingEndpoint {
    public NewMappingsEndpoint() {
        super();
    }

    @Override
    public Map<String, Object> invoke() {
        return super.invoke();
    }

    @Override
    protected void extractMethodMappings(ApplicationContext applicationContext, Map<String, Object> result) {
//        super.extractMethodMappings(applicationContext, result);
        List<Map<String,Object>> list = (List)result.get("dispatcherServlet");
        if (applicationContext != null) {
            for (Map.Entry<String, AbstractHandlerMethodMapping> bean : applicationContext.getBeansOfType(AbstractHandlerMethodMapping.class).entrySet()) {
                @SuppressWarnings("unchecked")
                Map<?, HandlerMethod> methods = bean.getValue().getHandlerMethods();
                for (Map.Entry<?, HandlerMethod> method : methods.entrySet()) {
//                    Map<String, String> map = new LinkedHashMap<String, String>();
//                    map.put("handler",method.getValue().toString())
//                    map.put("bean", bean.getKey());
//                    map.put("method", method.getValue().toString());
//                    result.put(method.getKey().toString(), map);
                    System.out.println(method.getKey() + " " + method.getValue());
                }
            }
        }
    }

    @Override
    protected void extractHandlerMappings(ApplicationContext applicationContext, Map<String, Object> result) {
//        super.extractHandlerMappings(applicationContext, result);
        List<Map<String,Object>> list = new LinkedList<>();
        if (applicationContext != null) {
            Map<String, AbstractUrlHandlerMapping> mappings = applicationContext.getBeansOfType(AbstractUrlHandlerMapping.class);
            for (Map.Entry<String, AbstractUrlHandlerMapping> mapping : mappings.entrySet()) {
                Map<String, Object> handlers = getHandlerMap(mapping.getValue());
                for (Map.Entry<String, Object> handler : handlers.entrySet()) {
                    Map<String,Object> map = new LinkedHashMap<>();
                    map.put("handler",mapping.getKey());
                    map.put("predicate",handler.getKey());
                    map.put("details",null);
                    list.add(map);
//                    result.put(handler.getKey(), Collections.singletonMap("bean", mapping.getKey()));
                }
            }
        }
        result.put("dispatcherServlet",list);
    }

    private Map<String, Object> getHandlerMap(AbstractUrlHandlerMapping mapping) {
        if (AopUtils.isCglibProxy(mapping)) {
            // If the AbstractUrlHandlerMapping is a cglib proxy we can't call
            // the final getHandlerMap() method.
            return Collections.emptyMap();
        }
        return mapping.getHandlerMap();
    }

    @Override
    protected void extractHandlerMappings(Collection<AbstractUrlHandlerMapping> handlerMappings, Map<String, Object> result) {
//        super.extractHandlerMappings(handlerMappings, result);
    }

    @Override
    protected void extractMethodMappings(Collection<AbstractHandlerMethodMapping<?>> methodMappings, Map<String, Object> result) {
//        super.extractMethodMappings(methodMappings, result);
    }

    /**
     * A description of an application's request mappings. Primarily intended for
     * serialization to JSON.
     */
    public static final class ApplicationMappings {

        private final Map<String, ContextMappings> contextMappings;

        private ApplicationMappings(Map<String, ContextMappings> contextMappings) {
            this.contextMappings = contextMappings;
        }

        public Map<String, ContextMappings> getContexts() {
            return this.contextMappings;
        }

    }

    /**
     * A description of an application context's request mappings. Primarily intended for
     * serialization to JSON.
     */
    public static final class ContextMappings {

        private final Map<String, Object> mappings;

        private final String parentId;

        private ContextMappings(Map<String, Object> mappings, String parentId) {
            this.mappings = mappings;
            this.parentId = parentId;
        }

        public String getParentId() {
            return this.parentId;
        }

        public Map<String, Object> getMappings() {
            return this.mappings;
        }

    }

    //    public Map<String, Object> transform(Map<String,Object> oldRet) {
//        Map<String,Object> newRet = new HashMap<>();
//
//        Set<String> oldKey = oldRet.keySet();
//        for(String key: oldKey) {
//            Map<String,Object> value = (Map)oldRet.get(oldKey);
//
//            Map<String,Object> dispartcherServlet = new HashMap<>();
//
//            dispartcherServlet.put("handler",value.get("method"));
//
//            if(key.charAt(0) == '{' && key.charAt(key.length()-1) == '}') {
//
//            }
//            else {
//
//            }
//            String[] split = key.split(",");
//        }
//
//        return newRet;
//    }
}
