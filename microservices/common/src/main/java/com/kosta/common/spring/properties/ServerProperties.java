package com.kosta.common.spring.properties;

import java.util.concurrent.ThreadLocalRandom;

public abstract class ServerProperties {

    protected String[] makeURLS;
    protected abstract String abstractURL();
    public String[] getUrlList(){
        if(makeURLS == null){
            String[] values = abstractURL().replaceAll(" ", "").split(",");
            makeURLS = new String[values.length];
            for(int i = 0; i < values.length; ++i){
                makeURLS[i] = "http://" + values[i];
            }
        }
        return makeURLS;
    }

    public String randURL(){
        String[] urls = getUrlList();

        int min = 0;
        int max = urls.length;

        int randomInt = ThreadLocalRandom.current().nextInt(min, max);

        return urls[randomInt];
    }
}
