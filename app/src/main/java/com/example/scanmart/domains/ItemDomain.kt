package com.example.scanmart.domains

data class ItemDomain(
    var title: String = "",
    var imagePath: String = "",
    var description: String = "",
    var price: Double = 0.0,
    var id: Int = 0,
    var stars: Double = 0.0,
    var categoryId: Int = 0,
    var locationId: Int = 0

)