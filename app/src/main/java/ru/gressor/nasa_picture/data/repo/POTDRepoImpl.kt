package ru.gressor.nasa_picture.data.repo

import com.google.gson.GsonBuilder
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.gressor.nasa_picture.data.api.NasaApi
import ru.gressor.nasa_picture.data.mappers.POTDPojo2Domain
import ru.gressor.nasa_picture.domain.entities.RequestResult
import ru.gressor.nasa_picture.domain.repos.POTDRepo

class POTDRepoImpl : POTDRepo {
    private val mapper = POTDPojo2Domain()

    override fun getPictureOfTheDay(): Single<RequestResult> =
        getNasaApi().getPictureOfTheDayPOJO()
            .subscribeOn(Schedulers.io())
            .map<RequestResult> {
                RequestResult.Success(mapper.map(it))
            }
            .onErrorReturn {
                RequestResult.Error(it)
            }

    private fun getRetrofit() = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .client(
            OkHttpClient.Builder()
                .addInterceptor(ApiKeyInterceptor())
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build()
        )
        .build()

    private fun getNasaApi(): NasaApi = getRetrofit().create(NasaApi::class.java)
}

private const val BASE_URL = "https://api.nasa.gov/"