package com.drawling.app.network

import android.util.Log
import com.drawling.app.BuildConfig
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import org.json.JSONObject
import java.net.URI
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SocketManager @Inject constructor() {
    private var socket: Socket? = null
    private val _strokeEvents = MutableSharedFlow<JSONObject>(extraBufferCapacity = 64)
    val strokeEvents: SharedFlow<JSONObject> = _strokeEvents
    private val _partnerJoined = MutableSharedFlow<String>(extraBufferCapacity = 8)
    val partnerJoined: SharedFlow<String> = _partnerJoined

    fun connect(token: String) {
        try {
            val options = IO.Options.builder().setAuth(mapOf("token" to token)).setReconnection(true).setReconnectionAttempts(5).build()
            socket = IO.socket(URI.create(BuildConfig.SOCKET_URL), options)
            socket?.apply {
                on(Socket.EVENT_CONNECT) { Log.d("Socket", "Connected") }
                on(Socket.EVENT_DISCONNECT) { Log.d("Socket", "Disconnected") }
                on("stroke") { args -> (args[0] as? JSONObject)?.let { _strokeEvents.tryEmit(it) } }
                on("partner:joined") { args -> (args[0] as? String)?.let { _partnerJoined.tryEmit(it) } }
                connect()
            }
        } catch (e: Exception) { Log.e("Socket", "Error", e) }
    }
    fun joinRoom(roomId: String) { socket?.emit("room:join", roomId) }
    fun sendStroke(stroke: JSONObject) { socket?.emit("stroke", stroke) }
    fun sendClear() { socket?.emit("canvas:clear") }
    fun disconnect() { socket?.disconnect(); socket = null }
    fun isConnected() = socket?.connected() == true
}
