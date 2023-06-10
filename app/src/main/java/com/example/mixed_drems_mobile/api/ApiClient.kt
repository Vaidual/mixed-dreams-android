package com.example.mixed_drems_mobile.api

import com.example.mixed_drems_mobile.R
import com.example.mixed_drems_mobile.models.ErrorResponse
import com.example.mixed_drems_mobile.utils.MainApplication
import com.example.mixed_drems_mobile.utils.SharedPreferencesHelper
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.conn.ssl.NoopHostnameVerifier
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.conn.ssl.SSLConnectionSocketFactory
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.conn.ssl.TrustSelfSignedStrategy
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.ssl.SSLContextBuilder
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.call.receive
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.accept
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.http.ContentType.Application.Json
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.post
import io.ktor.client.request.request
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.readText
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import java.io.FileInputStream
import java.security.KeyStore
import java.security.SecureRandom
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

class TrustAllX509TrustManager : X509TrustManager {
    override fun getAcceptedIssuers(): Array<X509Certificate?> = arrayOfNulls(0)

    override fun checkClientTrusted(certs: Array<X509Certificate?>?, authType: String?) {}

    override fun checkServerTrusted(certs: Array<X509Certificate?>?, authType: String?) {}
}

//val cf = CertificateFactory.getInstance("X.509")
//val cert = context.resources.openRawResource(R.raw.localhost_android)
//try {
//    val ca = cf.generateCertificate(cert)
//    cert.close();
//
//    val keyStoreType = KeyStore.getDefaultType()
//    val keyStore = KeyStore.getInstance(keyStoreType)
//    keyStore.load(null, null)
//    keyStore.setCertificateEntry("ca", ca)
//
//    val tmfAlgo = TrustManagerFactory.getDefaultAlgorithm()
//    val tmf = TrustManagerFactory.getInstance(tmfAlgo)
//    tmf.init(keyStore)
//
//    val sslContext = SSLContext.getInstance("TLS")
//    sslContext.init(null, tmf.trustManagers, null)
//
//    sslSocketFactory(sslContext.socketFactory, tmf.trustManagers[0] as X509TrustManager)
//} finally {
//    cert.close()
//}

object ApiClient {

    var client = HttpClient(Android) {

        engine {
            sslManager = { httpsURLConnection ->
                httpsURLConnection.sslSocketFactory =
                    SSLContext.getInstance("TLS").apply {
                        init(
                            null,
                            arrayOf(TrustAllX509TrustManager()),
                            SecureRandom()
                        )
                    }.socketFactory
                httpsURLConnection.hostnameVerifier = HostnameVerifier { _, _ -> true }
            }
        }

        // For Logging
        install(Logging) {
            level = LogLevel.ALL
        }

        // JSON Response properties
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                isLenient = true
            })
        }

        // Default request for POST, PUT, DELETE,etc...
        install(DefaultRequest) {
            header(HttpHeaders.ContentType, Json)
            //add this accept() for accept Json Body or Raw Json as Request Body
            accept(Json)
        }
    }
}


class ApiResponse<T>(
    val isSuccess: Boolean,
    val data: T?,
    val error: ErrorResponse?,
) {

}

suspend inline fun <reified T : Any> HttpClient.postWithApiResponse(
    builder: HttpRequestBuilder.() -> Unit
): ApiResponse<T> {
    return try {
        val response = request(builder)
        if (response.status.isSuccess()) {
            ApiResponse(isSuccess = true, data = response.body<T>(), error = null)
        } else {
            if (response.status == HttpStatusCode.Unauthorized) {
                SharedPreferencesHelper.removeTokens()
            }
            ApiResponse(isSuccess = false, data = null, error = response.body<ErrorResponse>())
        }
    } catch (exception: ResponseException) {

        println(exception.message)
        ApiResponse(
            isSuccess = false,
            data = null,
            error = ErrorResponse(exception.message ?: "Unknown error", 500, 1)
        )
    } catch (exception: Exception) {
        println(exception.message)
        ApiResponse(
            isSuccess = false,
            data = null,
            error = ErrorResponse(exception.message ?: "Unknown error", 500, 1)
        )
    }
}