package com.example.scanmart.domains

data class ItemDomain(
    var Title: String = "",
    var ImagePath: String = "",
    var Description: String = "",
    var Price: Double = 0.0,
    var Id: Int = 0,
    var Stars: Double = 0.0,
    var CategoryId: Int = 0,
    var LocationId: Int = 0
)