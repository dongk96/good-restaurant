package com.sparta.restaurant_search.web.reponse

data class SearchKakaoResponse(
//    val id: Long,
//    val total: Int,
//    val start: Int,
//    val display: Int,
    val documents: List<SearchItem>
) {
    data class SearchItem (
        val id: Long,
        val place_name: String,
        val category_name: String,
        val category_group_code: String,
        val address_name: String,
        val road_address_name: String,
        val place_url: String
    )
}
