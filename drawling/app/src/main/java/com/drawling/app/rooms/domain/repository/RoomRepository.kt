package com.drawling.app.rooms.domain.repository

import com.drawling.app.rooms.domain.model.Room
import com.drawling.app.utils.Resource

interface RoomRepository {
    suspend fun getMyRooms(): Resource<List<Room>>
    suspend fun createCoupleRoom(): Resource<Pair<Room, String>>
    suspend fun createPersonalRoom(): Resource<Room>
    suspend fun joinRoom(inviteCode: String): Resource<Room>
    suspend fun getRoomById(roomId: String): Resource<Room>
}
