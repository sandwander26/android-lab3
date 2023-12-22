package hse.course.android_lab3.api


import hse.course.android_lab3.model.NewsDataIoResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsDataIoApi {

    @GET("api/1/news")
    fun getNewsData(
        @Query("apikey") key: String,
        @Query("q") query: String,
        @Query("language") language: String
    ): Call<NewsDataIoResponse>

    companion object Factory {

        fun create(): NewsDataIoApi {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://newsdata.io/")
                .build()

            return retrofit.create(NewsDataIoApi::class.java);
        }
    }
}