package com.coocaa.core.secure.utils;

import com.novell.ldap.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.NamingException;
import javax.naming.ldap.*;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @description: 酷开域账号登录
 * @author: dongyang_wu
 * @create: 2019-07-30 20:02
 */
public class LdapUtil {
    private static final Logger logger = LoggerFactory.getLogger(com.coocaa.core.secure.utils.LdapUtil.class);
    private String url;
    private String basedn;
    private String domain;
    private String host;
    private int port;
    private Hashtable<String, String> env = new Hashtable();

    public LdapUtil() {
        this.host = "fennec.coocaa.com";
        this.port = 389;
        this.basedn = "ou=coocaa,dc=coocaa,dc=com";
    }

    public LdapUtil(String host, int port, String basedn) {
        this.host = host;
        this.port = port;
        this.basedn = basedn;
    }

    public LDAPEntry getUserInfo(String loginName, String password) {
        LDAPEntry nextEntry = null;
        LDAPConnection lc = new LDAPConnection();

        try {
            lc.connect(this.host, this.port);
            lc.bind(3, loginName, password.getBytes());
            String[] attrs = new String[]{"cn", "mail", "name", "mobile", "homePhone", "userPrincipalName"};

            for (LDAPSearchResults result = lc.search(this.basedn, 2, "(&(objectClass=*)(userPrincipalName=" + loginName + "))", attrs, false); result.hasMore(); nextEntry = result.next()) {
                ;
            }

            return nextEntry;
        } catch (LDAPException var10) {
            logger.error(var10.getMessage(), var10);
            return nextEntry;
        } finally {
            ;
        }
    }

    public boolean validateUser(String userName, String passwd) {
        StringBuilder sb = new StringBuilder();
        sb.append("ldap://").append(this.host).append(":").append(this.port).append("/").append(this.basedn);
        LdapContext ldapContext = null;
        this.env.put("java.naming.security.principal", userName);
        this.env.put("java.naming.security.credentials", passwd);
        this.env.put("java.naming.provider.url", sb.toString());
        this.env.put("java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
        this.env.put("java.naming.security.authentication", "simple");

        try {
            ldapContext = new InitialLdapContext(this.env, (Control[]) null);
            ldapContext.close();
            return true;
        } catch (NamingException var6) {
            logger.error("[LdapUtil]-[validateUser] {}", var6.getMessage(), var6);
            return false;
        }
    }

    public LDAPEntry validAndGetUser(String userName, String passwd) {
        return this.validateUser(userName, passwd) ? this.getUserInfo(userName, passwd) : null;
    }

    public boolean checkCoocaaEmail(String email) {
        boolean flag = false;
        try {
            String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@(coocaa\\.)+(com)+$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email.toLowerCase());
            flag = matcher.matches();
        } catch (Exception var6) {
            flag = false;
        }
        return flag;
    }
}