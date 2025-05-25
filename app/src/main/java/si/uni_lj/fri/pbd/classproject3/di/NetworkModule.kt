package si.uni_lj.fri.pbd.classproject3.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import si.uni_lj.fri.pbd.classproject3.rest.RestAPI
import si.uni_lj.fri.pbd.classproject3.rest.ServiceGenerator
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofitInstance(): Retrofit {
        return ServiceGenerator.retrofit
    }

    @Provides
    @Singleton
    fun provideRestApi(retrofit: Retrofit): RestAPI {
        return retrofit.create(RestAPI::class.java)
    }
}
