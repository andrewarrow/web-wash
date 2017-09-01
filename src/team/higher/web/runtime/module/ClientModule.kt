package team.higher.web.runtime.module

import team.higher.web.client.*
import team.higher.web.runtime.config.ServerConfiguration
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.time.OffsetDateTime
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Singleton
@Module(includes = arrayOf(AppModule::class))
class ClientModule {

  @Provides
  @Singleton
  fun provideRetrofit(http: OkHttpClient, config: ServerConfiguration): Retrofit {
    return Retrofit.Builder()
      .baseUrl(config.apiUrl)
      .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
      .addConverterFactory(GsonConverterFactory.create(GsonBuilder()
        .enableComplexMapKeySerialization()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .serializeNulls()
        .setPrettyPrinting()
        .setVersion(1.0)
        .create())
      )
      .client(http)
      .build()
  }

  @Provides
  @Singleton
  fun provideOkHttp(): OkHttpClient {
    val logging = HttpLoggingInterceptor { message -> println(message) }
    logging.level = HttpLoggingInterceptor.Level.BODY
    return OkHttpClient.Builder()
      .addInterceptor(logging)
      .connectTimeout(30, TimeUnit.SECONDS)
      .writeTimeout(120, TimeUnit.SECONDS)
      .readTimeout(60, TimeUnit.SECONDS)
      .protocols(Arrays.asList(Protocol.HTTP_1_1))
      .build()
  }

  @Provides
  @Singleton
  fun provideUserClient(retrofit: Retrofit): UserClient
    = retrofit.create(UserClient::class.java)

  @Provides
  @Singleton
  fun provideWashClient(retrofit: Retrofit): WashClient
    = retrofit.create(WashClient::class.java)
}

