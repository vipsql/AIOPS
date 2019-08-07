package com.coocaa.core.boot.redis;

import org.springframework.cache.interceptor.SimpleKey;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.lang.Nullable;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * 将redis key序列化为字符串
 *
 * <p>
 * spring cache中的简单基本类型直接使用 StringRedisSerializer 会有问题
 * </p>
 *
 * @author dongyang_wu
 */
public class RedisKeySerializer implements RedisSerializer<Object> {
	private final Charset charset;
	private final ConversionService converter;

	public RedisKeySerializer() {
		this(StandardCharsets.UTF_8);
	}

	public RedisKeySerializer(Charset charset) {
		Objects.requireNonNull(charset, "Charset must not be null");
		this.charset = charset;
		this.converter = DefaultConversionService.getSharedInstance();
	}

	@Override
	public Object deserialize(byte[] bytes) {
		// redis keys 会用到反序列化
		if (bytes == null) {
			return null;
		}
		return new String(bytes, charset);
	}

	@Override
	@Nullable
	public byte[] serialize(Object object) {
		Objects.requireNonNull(object, "redis key is null");
		String key;
		if (object instanceof SimpleKey) {
			key = "";
		} else if (object instanceof String) {
			key = (String) object;
		} else {
			key = converter.convert(object, String.class);
		}
		return key.getBytes(this.charset);
	}

}
