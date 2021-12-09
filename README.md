# pv-java-https-mtls
Java client to connect to an https server using MTLS (mutal authentication)

This is a simple java client that shows how to use mutual TLS authentication. 

The environment variables currently in use are the following:
 * PV_TRUSTSTORE_FILE - the name of the truststore that has the server's CA or cert chain; this is used for the client to ensure that the server's certs are valid
 * PV_IDENTITY_TRUSTSTORE_PASS_FILE - the name of a file that has the truststore's password
 * PV_IDENTITY_KEYSTORE_FILE - the name of the keystore that has the client-side certificate(s); in MTLS, the server will use this to authententicate the connection.  Only certificates (or CAs that signed certificates) listed in the server are allowed to connect.
 * PV_IDENTITY_KEYSTORE_PASS_FILE - the name of a file that has the keystore's password
 * PV_IDENTITY_KEYSTORE_CERT_ALIAS - the alias of the certificate inside the PV_IDENTITY_KEYSTORE_FILE.  This points to the certificate that will be sent to the server to complete the authentication

