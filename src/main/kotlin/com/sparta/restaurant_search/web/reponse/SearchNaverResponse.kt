package com.sparta.restaurant_search.web.reponse

data class SearchNaverResponse (
    val lastBuildDate: String,
    val total: Int,
    val start: Int,
    val display: Int,
    val items: List<SearchLocalItem>
) {
    data class SearchLocalItem(
        val title: String,
        val link: String,
        val telephone: String,
        val address: String,
        val roadAddress: String,
    )
}