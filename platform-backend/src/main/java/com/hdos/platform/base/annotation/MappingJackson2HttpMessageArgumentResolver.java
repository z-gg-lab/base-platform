package com.hdos.platform.base.annotation;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hdos.platform.common.util.StringUtils;

/**
 * Jackson2 参数转换
 * @author chenyang
 *
 */
public class MappingJackson2HttpMessageArgumentResolver implements HandlerMethodArgumentResolver {

	private static final String REQUEST_JSON_PARAM = RequestJsonParam.class.getName();
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private final ObjectMapper objectMapper;

	/**
     * 
     */
	public MappingJackson2HttpMessageArgumentResolver() {
		this.objectMapper = new ObjectMapper();
		this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(RequestJsonParam.class)
		        && objectMapper.canSerialize(parameter.getParameterType());
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
	        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
		// content-type不是json的不处理
//        if (!request.getContentType().contains("application/json")) {
//            return null;
//        }

        // 把reqeust的body读取到StringBuilder
        BufferedReader reader = request.getReader();
        StringBuilder sb = new StringBuilder();

        char[] buf = new char[1024];
        int rd;
        while((rd = reader.read(buf)) != -1){
            sb.append(buf, 0, rd);
        }

        // 利用fastjson转换为对应的类型
//        if(JSONObjectWrapper.class.isAssignableFrom(parameter.getParameterType())){
//            return new JSONObjectWrapper(JSON.parseObject(sb.toString()));
//        } else {
//            return JSON.parseObject(sb.toString(), parameter.getParameterType());
//        }
        
		// 先提取缓存
		Map<String, JsonNode> jsonObjectMap = (Map<String, JsonNode>) webRequest.getAttribute(REQUEST_JSON_PARAM,
		        RequestAttributes.SCOPE_REQUEST);
		if (jsonObjectMap == null) {
			Map<String, JsonNode> jsonMap = new HashMap<String, JsonNode>();

			// 提取jsonParameter
			String jsonParameter = sb.toString();
			if (StringUtils.isNotEmpty(jsonParameter)) {
				// decode for encode XXX not need 2016-5-19 for %
				// String decode = URLDecoder.decode(jsonParameter, "UTF-8");
				// read json root
				JsonNode jsonNode;
				try {
					jsonNode = objectMapper.readTree(jsonParameter);
				} catch (IOException e) {
					throw e;
				}
				// iterator
				for (Iterator<Entry<String, JsonNode>> fields = jsonNode.fields(); fields.hasNext();) {
					Entry<String, JsonNode> entry = fields.next();
					jsonMap.put(entry.getKey(), entry.getValue());
				}
			}

			// set
			jsonObjectMap = jsonMap;
			// store
			webRequest.setAttribute(REQUEST_JSON_PARAM, jsonMap, RequestAttributes.SCOPE_REQUEST);
		}

		// 获取参数
		RequestJsonParam jsonParam = parameter.getParameterAnnotation(RequestJsonParam.class);

		// 参数名
		String parameterName = jsonParam.value();
		if (StringUtils.isEmpty(parameterName)) {
			parameterName = parameter.getParameterName();
		}

		Class<?> parameterType = parameter.getParameterType();

		// read from store
		JsonNode jsonParameterValue = jsonObjectMap.get(parameterName);
		if (jsonParameterValue == null) {
			// required
			if (jsonParam.required()) {
				logger.error("Required " + parameterType + " parameter '" + parameterName + "' is not present.");
				throw new RuntimeException("参数是必须的，不能为空");
			}

			// 提取默认值JSON格式
			String defaultValue = jsonParam.defaultValue();
			if (StringUtils.isEmpty(defaultValue)) {
				// not required
				if (Boolean.TYPE.equals(parameterType)) {
					// for boolean type
					return Boolean.FALSE;
				} else if (parameterType.isPrimitive()) {
					logger.error("Optional "
					        + parameterType
					        + " parameter '"
					        + parameterName
					        + "' is present but cannot be translated into a null value due to being declared as a "
					        + "primitive type. Consider declaring it as object wrapper for the corresponding primitive type.");
					throw new RuntimeException("参数是不合法");
				}
				return null;
			}
			try {
				jsonParameterValue = objectMapper.readTree(defaultValue);
			} catch (IOException e) {
				logger.error("Optional " + parameterType + " parameter '" + parameterName
				        + "' is not present. but defaultValue [" + defaultValue + "] translated failed.", e);
				throw new RuntimeException("参数是不合法");
			}
		}
		// deser
		Object value;
		try {
			value = objectMapper.readValue(jsonParameterValue.traverse(), parameterType);
		} catch (Exception e) {
			logger.error("参数是不合法");
			throw new RuntimeException(e);
		}

		return value;
	}
}
