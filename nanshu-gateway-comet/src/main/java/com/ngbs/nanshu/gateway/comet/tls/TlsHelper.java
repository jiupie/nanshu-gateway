package com.ngbs.nanshu.gateway.comet.tls;

import io.netty.handler.ssl.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.cert.CertificateException;


/**
 * @author 南顾北衫
 */
public class TlsHelper {
    private static final Logger log = LoggerFactory.getLogger(TlsHelper.class);

    public static SslContext buildSslContext(boolean forClient) throws IOException, CertificateException {

        SslProvider provider = null;
        if (OpenSsl.isAvailable()) {
            provider = SslProvider.OPENSSL;
            log.info("Using OpenSSL provider");
        } else {
            log.error("not use OpenSSL provider");
        }


        if (forClient) {
            //客户端
            SslContextBuilder sslContextBuilder = SslContextBuilder.forClient().sslProvider(provider);
            if (TlsSystemConfig.tlsClientTrustCertPath != null) {
                sslContextBuilder.trustManager(new File(TlsSystemConfig.tlsClientTrustCertPath));
            }
            return sslContextBuilder.keyManager(TlsSystemConfig.tlsClientCertPath != null ? new FileInputStream(TlsSystemConfig.tlsClientCertPath) : null,
                    TlsSystemConfig.tlsClientPath != null ? new FileInputStream(TlsSystemConfig.tlsClientPath) : null).build();
        } else {
            //服务端
            SslContextBuilder sslContextBuilder = SslContextBuilder.forServer(TlsSystemConfig.tlsServerCertPath != null ? new FileInputStream(TlsSystemConfig.tlsServerCertPath) : null,
                    TlsSystemConfig.tlsServerPath != null ? new FileInputStream(TlsSystemConfig.tlsServerPath) : null).sslProvider(provider);

            if (TlsSystemConfig.tlsServerTrustCertPath != null) {
                sslContextBuilder.trustManager(new File(TlsSystemConfig.tlsServerTrustCertPath));
            }

            sslContextBuilder.clientAuth(ClientAuth.REQUIRE);
            return sslContextBuilder.build();
        }
    }
}
