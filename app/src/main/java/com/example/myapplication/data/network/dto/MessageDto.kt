package com.example.myapplication.data.network.dto
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.Instant
import java.time.format.DateTimeParseException

import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.jsonPrimitive

object FlexibleLongSerializer : KSerializer<Long> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("FlexibleLong", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Long) {
        encoder.encodeLong(value)
    }

    override fun deserialize(decoder: Decoder): Long {
        val jsonDecoder = decoder as? JsonDecoder
            ?: throw SerializationException("This serializer only supports JSON formats")
        val element = jsonDecoder.decodeJsonElement()
        val input = element.jsonPrimitive.content
        return try {
            input.toLong()
        } catch (e: NumberFormatException) {
            try {
                Instant.parse(input).toEpochMilli()
            } catch (ex: DateTimeParseException) {
                0L
            }
        }
    }
}

@Serializable
data class MessageDto(
    val id: String? = null,
    val sender: String? = null,
    val text: String? = null,
    @Serializable(with = FlexibleLongSerializer::class)
    val createdAt: Long? = null
)

@Serializable
data class NewMessageDto(
    val sender: String,
    val text: String,
    val createdAt: Long
)
