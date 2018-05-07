package com.hnf.guet.comhnfpatent.factory;

import android.content.Context;

import com.hnf.guet.comhnfpatent.util.LogUtils;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

public class SSLHelper {
    private final static String CLIENT_PRI_KEY = "clientbks.bks";
    private final static String TRUSTSTORE_PUB_KEY = "trustsbks.bks";
    private final static String CLIENT_BKS_PASSWORD = "hnf1234";
    private final static String TRUSTSTORE_BKS_PASSWORD = "hnf1234";
    private final static String KEYSTORE_TYPE = "BKS";
    private final static String PROTOCOL_TYPE = "TLS";
    private final static String CERTIFICATE_FORMAT = "X509";

    public static SSLSocketFactory getSSLCertifcation(Context context) {
        SSLSocketFactory sslSocketFactory = null;
        try {
            // 服务器端需要验证的客户端证书，其实就是客户端的keystore
            KeyStore keyStore = KeyStore.getInstance(KEYSTORE_TYPE);// 客户端信任的服务器端证书
            KeyStore trustStore = KeyStore.getInstance(KEYSTORE_TYPE);//读取证书
            InputStream ksIn = context.getAssets().open(CLIENT_PRI_KEY);
            InputStream tsIn = context.getAssets().open(TRUSTSTORE_PUB_KEY);//加载证书
            keyStore.load(ksIn, CLIENT_BKS_PASSWORD.toCharArray());
            trustStore.load(tsIn, TRUSTSTORE_BKS_PASSWORD.toCharArray());
            ksIn.close();
            tsIn.close();
            //初始化SSLContext
            SSLContext sslContext = SSLContext.getInstance(PROTOCOL_TYPE);
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(CERTIFICATE_FORMAT);
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(CERTIFICATE_FORMAT);
            trustManagerFactory.init(trustStore);
            keyManagerFactory.init(keyStore, CLIENT_BKS_PASSWORD.toCharArray());
            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);
            sslSocketFactory = sslContext.getSocketFactory();
            return sslSocketFactory;
        } catch (KeyStoreException e) {

        }//省略各种异常处理，请自行添加
        catch (IOException e) {
            e.printStackTrace();
            LogUtils.e("hahaha","错误一"+e.getMessage());
        } catch (CertificateException e) {
            e.printStackTrace();
            LogUtils.e("hahaha","错误二");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            LogUtils.e("hahaha","错误三");
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
            LogUtils.e("hahaha","错误四");
        } catch (KeyManagementException e) {
            e.printStackTrace();
            LogUtils.e("hahaha","错误五");
        }
        if (sslSocketFactory == null){
            LogUtils.e("呜呜呜","返回值是空的");
        }
        return sslSocketFactory;
    }
}
