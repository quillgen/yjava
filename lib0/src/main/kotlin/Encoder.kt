package com.riguz.y.lib0

import java.io.ByteArrayOutputStream
import java.nio.charset.StandardCharsets

@ExperimentalUnsignedTypes
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
    fun writeUint8Array(array: UByteArray)
    fun writeVarUint8Array(array: UByteArray)
    fun writeVarString(str: String)
    fun toBytes(): UByteArray
}

@ExperimentalUnsignedTypes
class ByteArrayEncoder(private val buffer: ByteArrayOutputStream) : Encoder {
    override fun writeUint8(number: UByte) {
        buffer.write(number.toInt());
    }

    override fun writeUint16(number: UShort) {
        val intValue = number.toInt()
        val lsb = (intValue and 0xFF).toByte()
        val msb = (intValue shr 8 and 0xFF).toByte()
        buffer.write(byteArrayOf(lsb, msb))
    }

    override fun writeUint32(number: UInt) {
        val intValue = number.toInt()
        val b1 = (intValue and 0xFF).toByte()
        val b2 = (intValue shr 8 and 0xFF).toByte()
        val b3 = (intValue shr 16 and 0xFF).toByte()
        val b4 = (intValue shr 24 and 0xFF).toByte()
        buffer.write(byteArrayOf(b1, b2, b3, b4))
    }

    override fun writeUint32BigEndian(number: UInt) {
        val intValue = number.toInt()
        val b1 = (intValue and 0xFF).toByte()
        val b2 = (intValue shr 8 and 0xFF).toByte()
        val b3 = (intValue shr 16 and 0xFF).toByte()
        val b4 = (intValue shr 24 and 0xFF).toByte()
        buffer.write(byteArrayOf(b4, b3, b2, b1))
    }

    override fun writeUint64(number: ULong) {
        val longValue = number.toLong()
        return writeInt64(longValue);
    }

    override fun writeFloat32(number: Float) {
        val intBits = number.toBits()
        for (i in 3 downTo 0) {
            val byte = (intBits ushr (i * 8) and 0xFF).toByte()
            buffer.write(byte.toInt())
        }
    }

    override fun writeFloat64(number: Double) {
        val intBits = number.toBits()
        for (i in 7 downTo 0) {
            val byte = (intBits ushr (i * 8) and 0xFF).toByte()
            buffer.write(byte.toInt())
        }
    }

    override fun writeInt64(number: Long) {
        val longValue = number.toLong()
        for (i in 7 downTo 0) {
            val byte = (longValue ushr (i * 8) and 0xFF).toByte()
            buffer.write(byte.toInt())
        }
    }

    override fun writeVarUint8(number: UByte) {
        return writeVarUint64(number.toULong())
    }

    override fun writeVarUint16(number: UShort) {
        return writeVarUint64(number.toULong())
    }

    override fun writeVarUint32(number: UInt) {
        return writeVarUint64(number.toULong())
    }

    override fun writeVarUint64(number: ULong) {
        var n = number
        do {
            val bits = (n and 0x7Fu).toUByte()
            n = n shr 7
            val byte = if (n > 0uL) bits or 0x80u else bits
            buffer.write(byte.toInt())
        } while (n > 0uL)
    }

    override fun writeVarInt8(number: Byte) {
        return writeVarInt64(number.toLong())
    }

    override fun writeVarInt16(number: Short) {
        return writeVarInt64(number.toLong())
    }

    override fun writeVarInt32(number: Int) {
        return writeVarInt64(number.toLong())
    }

    override fun writeVarInt64(number: Long) {
        var n = number
        val isNegative = n < 0
        if (isNegative) {
            n = -n
        }

        var firstByte = (0b0011_1111L and n).toUByte()
        if (n > 0b0011_1111L) {
            firstByte = firstByte or 0b1000_0000u
        }
        if (isNegative) {
            firstByte = firstByte or 0b0100_0000u
        }
        writeUint8(firstByte)

        n = n shr 6

        while (n > 0) {
            var b = 0u
            if (n > 0b0111_1111L) {
                b = b or 0b1000_0000u
            }
            b = b or (0b0111_1111L and n).toUInt()
            writeUint8(b.toUByte())
            n = n shr 7
        }
    }

    override fun writeUint8Array(array: UByteArray) {
        buffer.write(array.toByteArray())
    }

    override fun writeVarUint8Array(array: UByteArray) {
        writeVarUint32(array.size.toUInt())
        writeUint8Array(array)
    }

    override fun writeVarString(str: String) {
        val utf8Bytes = str.toByteArray(StandardCharsets.UTF_8)
        return writeVarUint8Array(utf8Bytes.toUByteArray())
    }

    override fun toBytes(): UByteArray {
        return buffer.toByteArray().toUByteArray()
    }
}

