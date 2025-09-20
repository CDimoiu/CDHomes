package com.example.cdhomes.di

import android.content.Context
import androidx.room.Room
import com.example.cdhomes.data.local.ListingDatabase
import com.example.cdhomes.data.remote.ListingApi
import com.example.cdhomes.data.repository.ListingRepositoryImpl
import com.example.cdhomes.domain.repository.ListingRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
import java.util.concurrent.TimeUnit
import kotlin.jvm.java
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

  @Provides
  @Singleton
  fun provideOkHttpClient(): OkHttpClient {
    val logging = HttpLoggingInterceptor().apply {
      level = HttpLoggingInterceptor.Level.BODY
    }
    return OkHttpClient.Builder()
      .addInterceptor(logging)
      .connectTimeout(30, TimeUnit.SECONDS)
      .readTimeout(30, TimeUnit.SECONDS)
      .build()
  }

  @Provides
  @Singleton
  fun provideRetrofit(client: OkHttpClient): Retrofit = Retrofit.Builder()
    .baseUrl("https://gsl-apps-technical-test.dignp.com/")
    .client(client)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

  @Provides
  @Singleton
  fun provideApi(retrofit: Retrofit): ListingApi = retrofit.create(ListingApi::class.java)


  @Provides
  @Singleton
  fun provideDatabase(@ApplicationContext context: Context): ListingDatabase =
    Room.databaseBuilder(context, ListingDatabase::class.java, "listings.db").build()

  @Provides
  fun provideDao(database: ListingDatabase) = database.listingDao()

  @Provides
  fun provideRepository(repositoryImpl: ListingRepositoryImpl): ListingRepository =
    repositoryImpl
}
