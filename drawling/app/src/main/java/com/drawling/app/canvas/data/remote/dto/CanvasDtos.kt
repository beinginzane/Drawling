package com.drawling.app.canvas.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CanvasStateDto(
    @SerializedName("strokes") val strokes: List<StrokeDto>
)

data class StrokeDto(
    @SerializedName("id") val id: String,
    @SerializedName("points") val points: List<PointDto>,
    @SerializedName("color") val color: String,
    @SerializedName("strokeWidth") val strokeWidth: Float,
    @SerializedName("isEraser") val isEraser: Boolean
)

data class PointDto(
    @SerializedName("x") val x: Float,
    @SerializedName("y") val y: Float
)
