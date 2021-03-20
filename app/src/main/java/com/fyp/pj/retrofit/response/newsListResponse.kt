package com.fyp.pj.retrofit.response

data class newsListResponse(
    var news_list: List<News>
)

data class News(
    val inbox_message_id: String,
    val title: String
)

