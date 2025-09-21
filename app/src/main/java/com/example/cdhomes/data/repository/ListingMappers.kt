package com.example.cdhomes.data.repository

import com.example.cdhomes.data.local.ListingEntity
import com.example.cdhomes.data.remote.ListingDto
import com.example.cdhomes.domain.model.Listing

fun ListingDto.toEntity() = ListingEntity(
  id = id,
  city = city,
  price = price,
  area = area,
  propertyType = propertyType,
  offerType = offerType,
  professional = professional,
  bedrooms = bedrooms,
  rooms = rooms,
  url = url
)

fun ListingEntity.toDomain() = Listing(
  id = id,
  city = city,
  price = price,
  area = area,
  propertyType = propertyType,
  offerType = offerType,
  professional = professional,
  bedrooms = bedrooms,
  rooms = rooms,
  url = url
)
