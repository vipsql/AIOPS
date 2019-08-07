package io.jsonwebtoken.impl;

import java.util.Base64;

/**
 * Base64Codec
 *
 * @author jjwt
 */
public class Base64Codec extends AbstractTextCodec {

	@Override
	public String encode(byte[] data) {
		return Base64.getEncoder().encodeToString(data);
	}

	@Override
	public byte[] decode(String encoded) {
		return Base64.getDecoder().decode(encoded);
	}

}
