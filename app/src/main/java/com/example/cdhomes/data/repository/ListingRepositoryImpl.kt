package com.example.cdhomes.data.repository

import com.example.cdhomes.data.local.ListingDao
import com.example.cdhomes.data.remote.ListingApi
import com.example.cdhomes.domain.model.Listing
import com.example.cdhomes.domain.repository.ListingRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class ListingRepositoryImpl @Inject constructor(
  private val listingDao: ListingDao,
  private val listingApi: ListingApi,
) : ListingRepository {

  override fun getListings(): Flow<List<Listing>> = flow {
    val localList = listingDao.getAllListings().firstOrNull()?.map { it.toDomain() }

    if (localList.isNullOrEmpty()) {
      val remote = listingApi.getListings()
      val entities = remote.items.map { it.toEntity() }
      listingDao.insertAllListings(entities)
      emit(entities.map { it.toDomain() })
    } else {
      emit(localList)
    }

    emitAll(listingDao.getAllListings().map { it.map { listing -> listing.toDomain() } })
  }

  override fun getListing(id: Int): Flow<Listing?> = flow {
    val localItem = listingDao.getListingById(id).firstOrNull()?.toDomain()
    if (localItem != null) {
      emit(localItem)
    } else {
      val remoteItem = listingApi.getListing(id)
      val entity = remoteItem.toEntity()
      listingDao.insertAllListings(listOf(entity))
      emit(entity.toDomain())
    }

    emitAll(listingDao.getListingById(id).map { it?.toDomain() })
  }

  override suspend fun refreshListings() {
    val remote = listingApi.getListings()
    listingDao.insertAllListings(remote.items.map { it.toEntity() })
  }
}
