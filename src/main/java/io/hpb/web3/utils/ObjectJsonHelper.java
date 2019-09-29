package io.hpb.web3.utils;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;

import org.springframework.util.Base64Utils;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
public class ObjectJsonHelper {
	public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
	private static ObjectMapper om=new ObjectMapper();
	static{
		om.configure(Feature.ALLOW_SINGLE_QUOTES,true);
		om.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES,true);
		om.setSerializationInclusion(Include.ALWAYS);
		om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		om.setDateFormat(new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS));
	}
	public static String serialize(Object o) throws JsonProcessingException{
		return om.writeValueAsString(o);
	}
	public static  <T> T deserialize(String str,Class<T> clazz) throws JsonParseException, JsonMappingException, IOException{
		return om.readValue(str.getBytes(),clazz);
	}
	public static void main(String[] args) throws JsonProcessingException{
		String serialize="[\"AccountIndex\",\"FE343LEKF\",\"/Account/queryAcountInfo\",\"DF34F\",\"123456\"]";
		byte[] encode = Base64Utils.encode(serialize.getBytes(StandardCharsets.UTF_8));
		System.out.println(new String(encode,StandardCharsets.UTF_8));
		String decodeParam=new String(Base64Utils.decode(encode),StandardCharsets.UTF_8);
		System.out.println(decodeParam);
	}
}
