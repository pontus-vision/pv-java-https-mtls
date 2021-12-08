package com.pontusvision;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.BasicHttpClientConnectionManager;
import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
import org.apache.hc.client5.http.socket.PlainConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.HttpsSupport;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.config.Registry;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.hc.core5.http.io.support.ClassicRequestBuilder;
import org.apache.hc.core5.ssl.SSLContexts;
import org.apache.hc.core5.ssl.TrustStrategy;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class App {


  public App() {

    // TODO Auto-generated constructor stub

  }


  public static void main(String[] args) {

    System.out.println("PV  2-way / mutual SSL-authentication test");


    try {

      String CERT_ALIAS = "certificate", CERT_PASSWORD = "pa55wordpa55word";

      KeyStore identityKeyStore = KeyStore.getInstance("jks");

      FileInputStream identityKeyStoreFile = new FileInputStream("certs/keystore.jks");

      identityKeyStore.load(identityKeyStoreFile, CERT_PASSWORD.toCharArray());


      KeyStore trustKeyStore = KeyStore.getInstance("jks");

      FileInputStream trustKeyStoreFile = new FileInputStream("certs/truststore.jks");

      trustKeyStore.load(trustKeyStoreFile, (CERT_PASSWORD).toCharArray());

      SSLContext sslContext = SSLContexts.custom()

          // load identity keystore

          .loadKeyMaterial(identityKeyStore, CERT_PASSWORD.toCharArray(), (aliases, sslParameters) -> CERT_ALIAS)

          // load trust keystore

          .loadTrustMaterial(trustKeyStore, (chain, authType) -> true)

          .build();

      SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext,
          new String[]{"TLSv1.3","TLSv1.2", "TLSv1.1"},
          null,
//          (hostname, session) -> true);
//          NoopHostnameVerifier.INSTANCE);
          HttpsSupport.getDefaultHostnameVerifier());

          Registry < ConnectionSocketFactory > socketFactoryRegistry =
              RegistryBuilder.<ConnectionSocketFactory>create()
                  .register("https", sslsf)
                  .register("http", new PlainConnectionSocketFactory())
                  .build();


      BasicHttpClientConnectionManager connectionManager =
          new BasicHttpClientConnectionManager(socketFactoryRegistry);


      CloseableHttpClient httpClient = HttpClients.custom()
          .setConnectionManager(connectionManager).build();


      callEndPoint(httpClient, "https://graphdb-nifi/home/md2_search",

          "{" +
              "\"settings\":{\"start\":0,\"limit\":1000}" +
              ",\"query\":{\"reqId\":22966,\"name\":\"RAIMUNDO CESAR FERREIRA DA SILVA\"," +
                          "\"docCpf\":\"05596491004\",\"email\":\"robelils@zaz.com.br\"}" +
              "}"

      );

    } catch (Exception ex) {

      System.out.println("Boom, we failed: " + ex);

      ex.printStackTrace();

    }

  }

  private static void callEndPoint(CloseableHttpClient aHTTPClient, String aEndPointURL, String aPostParams) {


    try {

      ClassicHttpRequest request = ClassicRequestBuilder.post()
          .setUri(aEndPointURL)
          .addHeader("Accept", "application/json")
          .addHeader("Content-type", "application/json")
          .setEntity(aPostParams)
          .build();
      System.out.println("Calling URL: " + aEndPointURL);


      System.out.println("**POST** request Url: " + request.getRequestUri());

      System.out.println("Parameters : " + aPostParams);

      CloseableHttpResponse response = aHTTPClient.execute(request);

      int responseCode = response.getCode();

      System.out.println("Response Code: " + responseCode);

      System.out.println("Content:-n");

      BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

      String line = "";

      while ((line = rd.readLine()) != null) {

        System.out.println(line);

      }

    } catch (Exception ex) {

      System.out.println("Boom, we failed: " + ex);

      ex.printStackTrace();

    }

  }

}
