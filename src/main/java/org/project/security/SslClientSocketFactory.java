package org.project.security;

import javax.net.ssl.*;
import java.io.*;
import java.net.Socket;
import java.rmi.server.RMIClientSocketFactory;
import java.security.*;
import java.security.cert.CertificateException;

public class SslClientSocketFactory implements RMIClientSocketFactory, Serializable {

    private SSLSocketFactory sf = null;

    public SslClientSocketFactory(String filename, String password) throws FileNotFoundException, IOException, NoSuchAlgorithmException, CertificateException, KeyStoreException, UnrecoverableKeyException, KeyManagementException {
        KeyStore ks = KeyStore.getInstance("jks");
        ks.load(new FileInputStream(new File(filename + ".ks")), password.toCharArray());
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(ks, password.toCharArray());
        KeyStore ts = KeyStore.getInstance("jks");
        ts.load(new FileInputStream(new File(filename + ".ts")), password.toCharArray());
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(ts);
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), new SecureRandom());
        sf = sslContext.getSocketFactory();
    }

    @Override
    public Socket createSocket(String host, int port) throws IOException {
        SSLSocket sslSock = (SSLSocket) sf.createSocket(host, port);
        return sslSock;
    }

}
