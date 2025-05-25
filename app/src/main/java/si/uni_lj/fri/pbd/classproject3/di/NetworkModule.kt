package si.uni_lj.fri.pbd.classproject3.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
import retrofit2.Retrofit
import si.uni_lj.fri.pbd.classproject3.rest.RestAPI
import si.uni_lj.fri.pbd.classproject3.rest.ServiceGenerator

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun retrofit(): Retrofit = ServiceGenerator.retrofit

    @Provides @Singleton
    fun restApi(retrofit: Retrofit): RestAPI = retrofit.create(RestAPI::class.java)
}