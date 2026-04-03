package com.drawling.app.domain.usecase.room

import com.drawling.app.domain.repository.RoomRepository
import javax.inject.Inject

class CreateCoupleRoomUseCase @Inject constructor(private val repo: RoomRepository) {
    suspend operator fun invoke() = repo.createCoupleRoom()
}

class CreatePersonalRoomUseCase @Inject constructor(private val repo: RoomRepository) {
    suspend operator fun invoke() = repo.createPersonalRoom()
}

class JoinRoomUseCase @Inject constructor(private val repo: RoomRepository) {
    suspend operator fun invoke(inviteCode: String) = repo.joinRoom(inviteCode)
}

class GetMyRoomsUseCase @Inject constructor(private val repo: RoomRepository) {
    suspend operator fun invoke() = repo.getMyRooms()
}
