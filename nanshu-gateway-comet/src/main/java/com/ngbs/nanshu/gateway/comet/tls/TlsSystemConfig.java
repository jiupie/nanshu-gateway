package com.ngbs.nanshu.gateway.comet.tls;

/**
 * @author 南顾北衫
 */
public class TlsSystemConfig {
    public static final String TLS_SERVER_MODE = "tls.server.mode";

    //=====================服务端证书
    //秘钥
    public static final String TLS_SERVER_KEYPATH = "tls.server.keyPath";
    //ca证书
    public static final String TLS_SERVER_CERTPATH = "tls.server.certPath";
    //认证证书
    public static final String TLS_SERVER_TRUSTCERTPATH = "tls.server.trustCertPath";
    //=====================

    //=====================客户端证书
    //秘钥
    public static final String TLS_CLIENT_KEYPATH = "tls.client.keyPath";
    //ca证书
    public static final String TLS_CLIENT_CERTPATH = "tls.client.certPath";
    //认证证书
    public static final String TLS_CLIENT_TRUSTCERTPATH = "tls.client.trustCertPath";
    //=====================


    public static TlsMode tlsMode = TlsMode.parse(System.getProperty(TLS_SERVER_MODE, "disabled"));

    //服务端
    public static String tlsServerPath = System.getProperty(TLS_SERVER_KEYPATH, null);
    public static String tlsServerCertPath = System.getProperty(TLS_SERVER_CERTPATH, null);
    public static String tlsServerTrustCertPath = System.getProperty(TLS_SERVER_TRUSTCERTPATH, null);

    //===========
    public static String tlsClientPath = System.getProperty(TLS_CLIENT_KEYPATH, null);
    public static String tlsClientCertPath = System.getProperty(TLS_CLIENT_CERTPATH, null);
    public static String tlsClientTrustCertPath = System.getProperty(TLS_CLIENT_TRUSTCERTPATH, null);

}
