package org.project.security;

import javax.net.ssl.*;
import java.io.*;
import java.net.ServerSocket;
import java.rmi.server.RMIServerSocketFactory;
import java.security.*;
import java.security.cert.CertificateException;

public class SslServerSocketFactory implements RMIServerSocketFactory, Serializable {


    //System.setSecurityManager(new SecurityManager());
    // keytool -genkey -alias registry -keyalg RSA -keystore registry.ks
    // password: registrypw
    // keytool -export -alias registry -keystore registry.ks -file registry.crt


    // keytool -genkey -alias client -keyalg RSA -keystore client.ks

    // password: clientpw
    // keytool -import -alias registry -keystore client.ts -file registry.crt
    //
    // keytool -export -alias client -keystore client.ks -file client.crt
    // keytool -import -alias client -keystore registry.ts -file client.crt



    private SSLServerSocketFactory ssf = null;

    public SslServerSocketFactory(String filename, String password) throws FileNotFoundException, IOException, NoSuchAlgorithmException, CertificateException, KeyStoreException, UnrecoverableKeyException, KeyManagementException {
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
        ssf = sslContext.getServerSocketFactory();
    }

    @Override
    public ServerSocket createServerSocket(int port) throws IOException {
        SSLServerSocket sslSock = (SSLServerSocket) ssf.createServerSocket(port);
        sslSock.setNeedClientAuth(true);
        return sslSock;
    }

}
