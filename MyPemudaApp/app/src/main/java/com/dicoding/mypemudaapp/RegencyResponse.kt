package com.dicoding.mypemudaapp

data class RegencyResponse(
	val data: List<DataItem?>?,
	val message: String?,
	val status: String?
)

data class DataItem(
	val regencyName: String?,
	val idRegency: Int?
)

