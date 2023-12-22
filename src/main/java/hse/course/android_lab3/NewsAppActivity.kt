package hse.course.android_lab3

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hse.course.android_lab3.adapter.NewsAdapter
import hse.course.android_lab3.api.NewsDataIoApi
import hse.course.android_lab3.model.NewsDataIoResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsAppActivity : AppCompatActivity() {

    private lateinit var newsRecyclerView: RecyclerView
    private lateinit var infoMessageTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        setContentView(R.layout.news_app_activity_layout)

        infoMessageTextView = findViewById(R.id.info_message_textview)
        infoMessageTextView.visibility = TextView.INVISIBLE

        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL

        newsRecyclerView = findViewById(R.id.news_recycler_view)
        newsRecyclerView.layoutManager = linearLayoutManager
        newsRecyclerView.adapter = NewsAdapter(ArrayList())

        val searchButton: Button = findViewById(R.id.search_button)
        searchButton.setOnClickListener {

            val newsTopicEditText: EditText = findViewById(R.id.news_topic_edit_text)
            val topic: String = newsTopicEditText.text.toString()

            if (topic.isEmpty()) {
                infoMessageTextView.text = getString(R.string.empty_search_message)
                infoMessageTextView.visibility = TextView.VISIBLE
                return@setOnClickListener
            }

            val newsDataIoApi = NewsDataIoApi.create()
            newsDataIoApi.getNewsData(
                getString(R.string.API_KEY),
                topic,
                getString(R.string.LANGUAGE)
            )
                .enqueue(object : Callback<NewsDataIoResponse> {

                    override fun onResponse(
                        call: Call<NewsDataIoResponse>,
                        response: Response<NewsDataIoResponse>
                    ) {
                        val newsDataIoResponse: NewsDataIoResponse? = response.body()

                        if (newsDataIoResponse?.results != null && newsDataIoResponse.results.size != 0) {
                            newsRecyclerView.adapter = NewsAdapter(newsDataIoResponse.results)
                            infoMessageTextView.text = getString(
                                R.string.results_found_message,
                                newsDataIoResponse.results.size
                            )
                            infoMessageTextView.visibility = TextView.VISIBLE
                        } else {
                            infoMessageTextView.text = getString(R.string.results_not_found_message)
                            infoMessageTextView.visibility = TextView.VISIBLE
                        }
                    }

                    override fun onFailure(
                        call: Call<NewsDataIoResponse>,
                        t: Throwable
                    ) {
                        infoMessageTextView.text =
                            getString(R.string.error_during_search_message, t.message)
                    }
                })
        }
    }
}



