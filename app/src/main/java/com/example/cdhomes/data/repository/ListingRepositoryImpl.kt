package com.example.cdhomes.data.repository

import com.example.cdhomes.domain.model.Listing
import com.example.cdhomes.data.local.ListingDao
import com.example.cdhomes.data.remote.ListingApi
import com.example.cdhomes.domain.repository.ListingRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

@Singleton
class ListingRepositoryImpl @Inject constructor(
  private val listingApi: ListingApi,
  private val listingDao: ListingDao,
) : ListingRepository {

  override fun getListings(): Flow<List<Listing>> = flow {
    val localFlow = listingDao.getAllListings().map { list -> list.map { it.toDomain() } }
    emitAll(localFlow)

    val remote = listingApi.getListings()
    listingDao.insertAllListings(remote.items.map { it.toEntity() })
  }

  override fun getListing(id: Int): Flow<Listing?> = flow {
    val localFlow = listingDao.getListingById(id).map { it?.toDomain() }
    emitAll(localFlow)

    val remote = listingApi.getListing(id)
    listingDao.insertAllListings(listOf(remote.toEntity()))
  }
}
