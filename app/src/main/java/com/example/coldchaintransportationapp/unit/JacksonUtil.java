package com.example.coldchaintransportationapp.unit;

import android.util.Log;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class JacksonUtil {
    public static <T> T deserialize(String json,Class<T> cls){
        json=json.replace( "[","" ).replace( "]","" );
        ObjectMapper mapper = new ObjectMapper();
        T t = null;
        try{
            t=mapper.readValue(json, cls);
        }catch (Exception ex){
            return null;
        }
        return t;
    }

    public static <T> List<T> decode(String json,Class<T> cls) {
        ObjectMapper mapper = new ObjectMapper();
        List<T> list;
        try{
//            mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
//            mapper.configure( DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true) ;
//            mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
            JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, cls);
            list=(List<T>)mapper.readValue(json, javaType);
        }
        catch (Exception ex){
            Log.e( "listerror",ex.toString() );
            return null;
        }
        return list;
    }
}
