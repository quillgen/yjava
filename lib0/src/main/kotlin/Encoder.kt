package com.riguz.y.lib0

import java.io.ByteArrayOutputStream

@OptIn(ExperimentalUnsignedTypes::class)
interface Encoder {
    fun writeUint8(number: UByte)
    fun writeUint16(number: UShort)
    fun writeUint32(number: UInt)
    fun writeUint32BigEndian(number: UInt)
    fun writeUint64(number: ULong)
    fun writeFloat32(number: Float)
    fun writeFloat64(number: Double)
    fun writeInt64(number: Long)
    fun writeVarUint8(number: UByte)
    fun writeVarUint16(number: UShort)
    fun writeVarUint32(number: UInt)
    fun writeVarUint64(number: ULong)
    fun writeVarInt8(number: Byte)
    fun writeVarInt16(number: Short)
    fun writeVarInt32(number: Int)
    fun writeVarInt64(number: Long)
    fun writeVarUint8Array(number: UByteArray)
    fun writeVarString(str: String)
    fun toBytes(): UByteArray
}

@ExperimentalUnsignedTypes
class ByteArrayEncoder(var buffer: ByteArrayOutputStream) : Encoder {
    override fun writeUint8(number: UByte) {
        buffer.write(number.toInt());
    }

    override fun writeUint16(number: UShort) {
        TODO("Not yet implemented")
    }

    override fun writeUint32(number: UInt) {
        TODO("Not yet implemented")
    }

    override fun writeUint32BigEndian(number: UInt) {
        TODO("Not yet implemented")
    }

    override fun writeUint64(number: ULong) {
        TODO("Not yet implemented")
    }

    override fun writeFloat32(number: Float) {
        TODO("Not yet implemented")
    }

    override fun writeFloat64(number: Double) {
        TODO("Not yet implemented")
    }

    override fun writeInt64(number: Long) {
        TODO("Not yet implemented")
    }

    override fun writeVarUint8(number: UByte) {
        TODO("Not yet implemented")
    }

    override fun writeVarUint16(number: UShort) {
        TODO("Not yet implemented")
    }

    override fun writeVarUint32(number: UInt) {
        TODO("Not yet implemented")
    }

    override fun writeVarUint64(number: ULong) {
        TODO("Not yet implemented")
    }

    override fun writeVarInt8(number: Byte) {
        TODO("Not yet implemented")
    }

    override fun writeVarInt16(number: Short) {
        TODO("Not yet implemented")
    }

    override fun writeVarInt32(number: Int) {
        TODO("Not yet implemented")
    }

    override fun writeVarInt64(number: Long) {
        TODO("Not yet implemented")
    }

    override fun writeVarUint8Array(number: UByteArray) {
        TODO("Not yet implemented")
    }

    override fun writeVarString(str: String) {
        TODO("Not yet implemented")
    }

    override fun toBytes(): UByteArray {
        return buffer.toByteArray().toUByteArray()
    }

}

