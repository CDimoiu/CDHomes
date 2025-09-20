package com.example.cdhomes.data.repository

import com.example.cdhomes.data.local.ListingDao
import com.example.cdhomes.data.remote.ListingApi
import com.example.cdhomes.domain.model.Listing
import com.example.cdhomes.domain.repository.ListingRepository
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ListingRepositoryImpl @Inject constructor(
  private val listingDao: ListingDao,
  private val listingApi: ListingApi,
) : ListingRepository {

  override fun getListings(): Flow<List<Listing>> {
    val localFlow = listingDao.getAllListings().map { listings -> listings.map { it.toDomain() } }

    CoroutineScope(Dispatchers.IO).launch {
      val remote = listingApi.getListings()
      listingDao.insertAllListings(remote.items.map { it.toEntity() })
    }

    return localFlow
  }

  override fun getListing(id: Int): Flow<Listing?> {
    val localFlow = listingDao.getListingById(id).map { it?.toDomain() }

    CoroutineScope(Dispatchers.IO).launch {
      val remote = listingApi.getListing(id)
      listingDao.insertAllListings(listOf(remote.toEntity()))
    }

    return localFlow
  }
}
