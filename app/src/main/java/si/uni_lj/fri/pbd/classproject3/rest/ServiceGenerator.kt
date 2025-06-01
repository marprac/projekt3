package si.uni_lj.fri.pbd.classproject3.rest

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import si.uni_lj.fri.pbd.classproject3.Constants

object ServiceGenerator {
        private lateinit var sBuilder: Retrofit.Builder
        private lateinit var sHttpClient: OkHttpClient.Builder
        private lateinit var sRetrofit: Retrofit
    private fun init() {
        sHttpClient = OkHttpClient.Builder()
        sBuilder = Retrofit.Builder().baseUrl(Constants.BASE_URL).addConverterFactory(
            GsonConverterFactory.create()) // TODO: add converter

        /// DONE: create Interceptor and add it to client, what does this do?
        val logger = HttpLoggingInterceptor().apply{
            level = HttpLoggingInterceptor.Level.BODY
        }
        sHttpClient.addInterceptor(logger)

        sRetrofit = sBuilder.client(sHttpClient.build()).build()
    }

    fun <S> createService(serviceClass: Class<S>): S {
        return sRetrofit.create(serviceClass)
    }

    init {
        init()
    }
}