package com.ngbs.nanshu.gateway.comet.tls;

/**
 * For server, three SSL modes are supported: disabled, permissive and enforcing.
 * <ol>
 *     <li><strong>disabled:</strong> SSL is not supported; any incoming SSL handshake will be rejected, causing connection closed.</li>
 *     <li><strong>enforcing:</strong> SSL is required, aka, non SSL connection will be rejected.</li>
 * </ol>
 */
public enum TlsMode {

    DISABLED("disabled"),
    ENFORCING("enforcing");

    private final String name;

    TlsMode(String name) {
        this.name = name;
    }

    public static TlsMode parse(String mode) {
        for (TlsMode tlsMode : TlsMode.values()) {
            if (tlsMode.name.equals(mode)) {
                return tlsMode;
            }
        }

        return ENFORCING;
    }

    public String getName() {
        return name;
    }
}
