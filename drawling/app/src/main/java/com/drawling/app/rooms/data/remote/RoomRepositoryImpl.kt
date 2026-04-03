package com.drawling.app.rooms.data.remote

import com.drawling.app.network.ApiService
import com.drawling.app.rooms.data.remote.dto.JoinRoomRequestDto
import com.drawling.app.rooms.domain.model.Room
import com.drawling.app.rooms.domain.model.RoomType
import com.drawling.app.rooms.domain.repository.RoomRepository
import com.drawling.app.utils.Resource
import javax.inject.Inject

class RoomRepositoryImpl @Inject constructor(
    private val api: ApiService
) : RoomRepository {

    override suspend fun getMyRooms(): Resource<List<Room>> = try {
        val response = api.getMyRooms()
        if (response.isSuccessful) Resource.Success(response.body()!!.map { it.toDomain() })
        else Resource.Error(response.message())
    } catch (e: Exception) { Resource.Error(e.localizedMessage ?: "Error") }

    override suspend fun createCoupleRoom(): Resource<Pair<Room, String>> = try {
        val response = api.createCoupleRoom()
        if (response.isSuccessful && response.body() != null) {
            val body = response.body()!!
            Resource.Success(Pair(body.room.toDomain(), body.inviteCode))
        } else Resource.Error(response.message())
    } catch (e: Exception) { Resource.Error(e.localizedMessage ?: "Error") }

    override suspend fun createPersonalRoom(): Resource<Room> = try {
        val response = api.createPersonalRoom()
        if (response.isSuccessful) Resource.Success(response.body()!!.toDomain())
        else Resource.Error(response.message())
    } catch (e: Exception) { Resource.Error(e.localizedMessage ?: "Error") }

    override suspend fun joinRoom(inviteCode: String): Resource<Room> = try {
        val response = api.joinRoom(JoinRoomRequestDto(inviteCode))
        if (response.isSuccessful) Resource.Success(response.body()!!.toDomain())
        else Resource.Error(response.message())
    } catch (e: Exception) { Resource.Error(e.localizedMessage ?: "Error") }

    override suspend fun getRoomById(roomId: String): Resource<Room> = try {
        val response = api.getRoomById(roomId)
        if (response.isSuccessful) Resource.Success(response.body()!!.toDomain())
        else Resource.Error(response.message())
    } catch (e: Exception) { Resource.Error(e.localizedMessage ?: "Error") }

    private fun com.drawling.app.rooms.data.remote.dto.RoomDto.toDomain() = Room(
        id = id, type = if (type == "couple") RoomType.COUPLE else RoomType.PERSONAL,
        ownerId = ownerId, partnerId = partnerId, inviteCode = inviteCode, createdAt = createdAt
    )
}
