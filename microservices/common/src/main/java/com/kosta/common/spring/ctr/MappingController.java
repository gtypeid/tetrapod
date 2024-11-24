package com.kosta.common.spring.ctr;

import com.kosta.common.core.module.apichecker.data.MappingInfoAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@Profile("sync")
@RequestMapping("/api")
public class MappingController {

    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    @Autowired
    public MappingController(RequestMappingHandlerMapping requestMappingHandlerMapping){
        this.requestMappingHandlerMapping = requestMappingHandlerMapping;
    }

    @GetMapping("/apis")
    public List<MappingInfoAPI> getAPI(){
        Map<RequestMappingInfo, HandlerMethod> methods = requestMappingHandlerMapping.getHandlerMethods();
        List<MappingInfoAPI> mappingInfoAPIS = new ArrayList<>();
        methods.forEach( (key, value)->{
            var requestMethod = key.getMethodsCondition().getMethods()
                    .stream().findFirst();

            if(requestMethod.isPresent()){
                String method = requestMethod.get().toString();
                String pattern = key.getPathPatternsCondition().getPatterns()
                        .stream().findFirst().get().toString();

                Method getMethod = value.getMethod();
                String name = getMethod.getName();
                String returnType = getReturnType(getMethod);
                List<String> params = Arrays.stream(getMethod.getParameterTypes())
                        .map(Class::getName).toList();
                mappingInfoAPIS.add(new MappingInfoAPI(method, pattern, name, returnType, params) );
            }
        });
        return mappingInfoAPIS;
    }

    private String getReturnType(Method method) {
        Type genericReturnType = method.getGenericReturnType();
        if (genericReturnType instanceof ParameterizedType) {
            ParameterizedType monoGeneric = (ParameterizedType) genericReturnType;
            for (Type type : monoGeneric.getActualTypeArguments()){
                if(type instanceof ParameterizedType){
                    ParameterizedType generic = (ParameterizedType) type;
                    Type[] entityGeneric = generic.getActualTypeArguments();
                    for(Type it : entityGeneric){
                        return it.getTypeName();
                    }
                }
            }
            return monoGeneric.getActualTypeArguments()[0].getTypeName();

        } else {
            return genericReturnType.getTypeName();
        }
    }
}
