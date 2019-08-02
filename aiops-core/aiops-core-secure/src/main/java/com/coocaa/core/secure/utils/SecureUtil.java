/**
 * Copyright (c) 2018-2028, Chill Zhuang 庄骞 (smallchill@163.com).
 * <p>
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE 3.0;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/lgpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.coocaa.core.secure.utils;

import com.coocaa.core.secure.AiOpsUser;
import com.coocaa.core.secure.constant.*;
import com.coocaa.core.secure.exception.SecureException;
import com.coocaa.core.tool.utils.*;
import io.jsonwebtoken.*;
import lombok.SneakyThrows;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.*;

/**
 * Secure工具类
 *
 * @author Chill
 */
public class SecureUtil {
    private static final String BLADE_USER_REQUEST_ATTR = "_BLADE_USER_REQUEST_ATTR_";

    private final static String HEADER = TokenConstant.HEADER;
    private final static String BEARER = TokenConstant.BEARER;
    private final static String ACCOUNT = TokenConstant.ACCOUNT;
    private final static String USER_ID = TokenConstant.USER_ID;
    private final static String ROLE_ID = TokenConstant.ROLE_ID;
    private final static String USER_NAME = TokenConstant.USER_NAME;
    private final static String ROLE_NAME = TokenConstant.ROLE_NAME;
    private final static String TENANT_CODE = TokenConstant.TENANT_CODE;
    private final static String CLIENT_ID = TokenConstant.CLIENT_ID;
    private final static Integer AUTH_LENGTH = TokenConstant.AUTH_LENGTH;
    private static String BASE64_SECURITY = Base64.getEncoder().encodeToString(TokenConstant.SIGN_KEY.getBytes(Charsets.UTF_8));


    /**
     * 获取用户信息
     *
     * @return AiOpsUser
     */
    public static AiOpsUser getUser() {
        HttpServletRequest request = WebUtil.getRequest();
        if (request == null) {
            return null;
        }
        // 优先从 request 中获取
        Object bladeUser = request.getAttribute(BLADE_USER_REQUEST_ATTR);
        if (bladeUser == null) {
            bladeUser = getUser(request);
            if (bladeUser != null) {
                // 设置到 request 中
                request.setAttribute(BLADE_USER_REQUEST_ATTR, bladeUser);
            }
        }
        return (AiOpsUser) bladeUser;
    }

    /**
     * 获取用户信息
     *
     * @param request request
     * @return AiOpsUser
     */
    public static AiOpsUser getUser(HttpServletRequest request) {
        Claims claims = getClaims(request);
        if (claims == null) {
            return null;
        }
        String clientId = Func.toStr(claims.get(SecureUtil.CLIENT_ID));
        Integer userId = Func.toInt(claims.get(SecureUtil.USER_ID));
        String tenantCode = Func.toStr(claims.get(SecureUtil.TENANT_CODE));
        String roleId = Func.toStr(claims.get(SecureUtil.ROLE_ID));
        String account = Func.toStr(claims.get(SecureUtil.ACCOUNT));
        String roleName = Func.toStr(claims.get(SecureUtil.ROLE_NAME));
        String userName = Func.toStr(claims.get(SecureUtil.USER_NAME));
        AiOpsUser bladeUser = new AiOpsUser();
        bladeUser.setClientId(clientId);
        bladeUser.setUserId(userId);
        bladeUser.setTenantCode(tenantCode);
        bladeUser.setAccount(account);
        bladeUser.setRoleId(roleId);
        bladeUser.setRoleName(roleName);
        bladeUser.setUserName(userName);
        return bladeUser;
    }


    /**
     * 获取用户id
     *
     * @return userId
     */
    public static Integer getUserId() {
        AiOpsUser user = getUser();
        return (null == user) ? -1 : user.getUserId();
    }

    /**
     * 获取用户id
     *
     * @param request request
     * @return userId
     */
    public static Integer getUserId(HttpServletRequest request) {
        AiOpsUser user = getUser(request);
        return (null == user) ? -1 : user.getUserId();
    }

    /**
     * 获取用户账号
     *
     * @return userAccount
     */
    public static String getUserAccount() {
        AiOpsUser user = getUser();
        return (null == user) ? StringPool.EMPTY : user.getAccount();
    }

    /**
     * 获取用户账号
     *
     * @param request request
     * @return userAccount
     */
    public static String getUserAccount(HttpServletRequest request) {
        AiOpsUser user = getUser(request);
        return (null == user) ? StringPool.EMPTY : user.getAccount();
    }

    /**
     * 获取用户名
     *
     * @return userName
     */
    public static String getUserName() {
        AiOpsUser user = getUser();
        return (null == user) ? StringPool.EMPTY : user.getUserName();
    }

    /**
     * 获取用户名
     *
     * @param request request
     * @return userName
     */
    public static String getUserName(HttpServletRequest request) {
        AiOpsUser user = getUser(request);
        return (null == user) ? StringPool.EMPTY : user.getUserName();
    }

    /**
     * 获取用户角色
     *
     * @return userName
     */
    public static String getUserRole() {
        AiOpsUser user = getUser();
        return (null == user) ? StringPool.EMPTY : user.getRoleName();
    }

    /**
     * 获取用角色
     *
     * @param request request
     * @return userName
     */
    public static String getUserRole(HttpServletRequest request) {
        AiOpsUser user = getUser(request);
        return (null == user) ? StringPool.EMPTY : user.getRoleName();
    }

    /**
     * 获取租户编号
     *
     * @return tenantCode
     */
    public static String getTenantCode() {
        AiOpsUser user = getUser();
        return (null == user) ? StringPool.EMPTY : user.getTenantCode();
    }

    /**
     * 获取租户编号
     *
     * @param request request
     * @return tenantCode
     */
    public static String getTenantCode(HttpServletRequest request) {
        AiOpsUser user = getUser(request);
        return (null == user) ? StringPool.EMPTY : user.getTenantCode();
    }

    /**
     * 获取客户端id
     *
     * @return tenantCode
     */
    public static String getClientId() {
        AiOpsUser user = getUser();
        return (null == user) ? StringPool.EMPTY : user.getClientId();
    }

    /**
     * 获取客户端id
     *
     * @param request request
     * @return tenantCode
     */
    public static String getClientId(HttpServletRequest request) {
        AiOpsUser user = getUser(request);
        return (null == user) ? StringPool.EMPTY : user.getClientId();
    }

    /**
     * 获取Claims
     *
     * @param request request
     * @return Claims
     */
    public static Claims getClaims(HttpServletRequest request) {
        String auth = request.getHeader(HEADER);
        if ((auth != null) && (auth.length() > AUTH_LENGTH)) {
            return SecureUtil.parseJWT(auth);
        }
        return null;
    }

    /**
     * 获取请求头
     *
     * @return header
     */
    public static String getHeader() {
        return getHeader(Objects.requireNonNull(WebUtil.getRequest()));
    }

    /**
     * 获取请求头
     *
     * @param request request
     * @return header
     */
    public static String getHeader(HttpServletRequest request) {
        return request.getHeader(HEADER);
    }

    /**
     * 解析jsonWebToken
     *
     * @param jsonWebToken jsonWebToken
     * @return Claims
     */
    public static Claims parseJWT(String jsonWebToken) {
        try {
            return Jwts.parser()
                    .setSigningKey(Base64.getDecoder().decode(BASE64_SECURITY))
                    .parseClaimsJws(jsonWebToken).getBody();
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * 创建令牌
     *
     * @param user     user
     * @param audience audience
     * @param issuer   issuer
     * @param isExpire isExpire
     * @return jwt
     */
    public static String createJWT(Map<String, String> user, String audience, String issuer, boolean isExpire) {

//		String[] tokens = extractAndDecodeHeader();
//		assert tokens.length == 2;
//		String clientId = tokens[0];
//		String clientSecret = tokens[1];
//
//		// 校验客户端信息
//		if (!validateClient(clientId, clientSecret)) {
//			throw new SecureException("客户端认证失败!");
//		}

        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        //生成签名密钥
        byte[] apiKeySecretBytes = Base64.getDecoder().decode(BASE64_SECURITY);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        //添加构成JWT的类
        JwtBuilder builder = Jwts.builder().setHeaderParam("typ", "JsonWebToken")
                .setIssuer(issuer)
                .setAudience(audience)
                .signWith(signatureAlgorithm, signingKey);

        //设置JWT参数
        user.forEach(builder::claim);

        //设置应用id
        builder.claim(CLIENT_ID, 11);

        //添加Token过期时间
        if (isExpire) {
            long expMillis = nowMillis + AppConstant.TOKEN_EXPIRE_TIME;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp).setNotBefore(now);
        }

        //生成JWT
        return builder.compact();
    }

    /**
     * 获取过期时间(次日凌晨3点)
     *
     * @return expire
     */
    public static long getExpire() {
//		Calendar cal = Calendar.getInstance();
//		cal.add(Calendar.DAY_OF_YEAR, 1);
//		cal.set(Calendar.HOUR_OF_DAY, 3);
//		cal.set(Calendar.SECOND, 0);
//		cal.set(Calendar.MINUTE, 0);
//		cal.set(Calendar.MILLISECOND, 0);
//		return cal.getTimeInMillis() - System.currentTimeMillis();
        return 6 * 10000000L;
    }

    /**
     * 获取过期时间的秒数(次日凌晨3点)
     *
     * @return expire
     */
    public static int getExpireSeconds() {
        return (int) (getExpire() / 1000);
    }

    /**
     * 客户端信息解码
     */
    @SneakyThrows
    public static String[] extractAndDecodeHeader() {
        // 获取请求头客户端信息
        String header = Objects.requireNonNull(WebUtil.getRequest()).getHeader(SecureConstant.BASIC_HEADER_KEY);
        if (header == null || !header.startsWith(SecureConstant.BASIC_HEADER_PREFIX)) {
            throw new SecureException("No client information in request header");
        }
        byte[] base64Token = header.substring(6).getBytes(Charsets.UTF_8_NAME);

        byte[] decoded;
        try {
            decoded = Base64.getDecoder().decode(base64Token);
        } catch (IllegalArgumentException var7) {
            throw new RuntimeException("Failed to decode basic authentication token");
        }

        String token = new String(decoded, Charsets.UTF_8_NAME);
        int index = token.indexOf(StringPool.COLON);
        if (index == -1) {
            throw new RuntimeException("Invalid basic authentication token");
        } else {
            return new String[]{token.substring(0, index), token.substring(index + 1)};
        }
    }

    /**
     * 获取请求头中的客户端id
     */
    public static String getClientIdFromHeader() {
        String[] tokens = extractAndDecodeHeader();
        assert tokens.length == 2;
        return tokens[0];
    }

    /**
     * 校验Client
     *
     * @param clientId     客户端id
     * @param clientSecret 客户端密钥
     * @return boolean
     */
    private static boolean validateClient(String clientId, String clientSecret) {
        return true;
    }

}