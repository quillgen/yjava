package com.riguz.y.lib0

import java.io.ByteArrayInputStream
import java.io.EOFException
import java.nio.charset.StandardCharsets

@ExperimentalUnsignedTypes
interface Decoder {
    fun readUint8Array(len: Int): UByteArray
    fun readUint8(): UByte
    fun readUint16(): UShort
    fun readUint32(): UInt
    fun readUint32BigEndian(): UInt
    fun readUint64(): ULong
    fun readFloat32(): Float
    fun readFloat64(): Double
    fun readInt64(): Long
    fun readVarUint8Array(): UByteArray
    fun readVarUint(): ULong
    fun readVarInt(): Long
    fun readVarString(): String
}

@ExperimentalUnsignedTypes
class ByteArrayDecoder(private val buffer: ByteArrayInputStream) : Decoder {
    private fun requireByte(): Int {
        val b = buffer.read()
        if (b == -1) {
            throw EOFException("End of stream reached while reading")
        }
        return b
    }

    private fun requireNBytes(len: Int): ByteArray {
        val bytes = buffer.readNBytes(len)
        if (bytes.size != len) {
            throw EOFException("End of stream reached while reading")
        }
        return bytes
    }

    override fun readUint8Array(len: Int): UByteArray {
        return requireNBytes(len).toUByteArray()
    }

    override fun readUint8(): UByte {
        return requireByte().toUByte()
    }

    override fun readUint16(): UShort {
        val b1 = requireByte()
        val b2 = requireByte()

        return ((b2 shl 8) or b1).toUShort()
    }

    override fun readUint32(): UInt {
        val b1 = requireByte()
        val b2 = requireByte()
        val b3 = requireByte()
        val b4 = requireByte()
        return ((b4 shl 24) or (b3 shl 16) or (b2 shl 8) or b1).toUInt()
    }

    override fun readUint32BigEndian(): UInt {
        val b1 = requireByte()
        val b2 = requireByte()
        val b3 = requireByte()
        val b4 = requireByte()
        return ((b1 shl 24) or (b2 shl 16) or (b3 shl 8) or b4).toUInt()
    }

    override fun readUint64(): ULong {
        val b1 = requireByte().toLong()
        val b2 = requireByte().toLong()
        val b3 = requireByte().toLong()
        val b4 = requireByte().toLong()
        val b5 = requireByte().toLong()
        val b6 = requireByte().toLong()
        val b7 = requireByte().toLong()
        val b8 = requireByte().toLong()
        return ((b1 shl 56) or
                (b2 shl 48) or
                (b3 shl 40) or
                (b4 shl 32) or
                (b5 shl 24) or
                (b6 shl 16) or
                (b7 shl 8)
                or b8).toULong()
    }

    override fun readFloat32(): Float {
        val bits = readUint32BigEndian().toInt()
        return Float.fromBits(bits)
    }

    override fun readFloat64(): Double {
        val bits = readUint64().toLong()
        return Double.fromBits(bits)
    }

    override fun readInt64(): Long {
        return readUint64().toLong()
    }

    override fun readVarUint8Array(): UByteArray {
        val len = readVarUint()
        return readUint8Array(len.toInt())
    }

    override fun readVarUint(): ULong {
        var result = 0uL
        var shift = 0
        while (true) {
            val byte = requireByte()
            val b = byte.toULong()
            result = result or ((b and 0x7fuL) shl shift)

            if ((b and 0x80uL) == 0uL) {
                break
            }

            shift += 7
            if (shift >= 64) {
                throw IllegalArgumentException("Variable length quantity is too long")
            }
        }
        return result
    }

    override fun readVarInt(): Long {
        val firstByte = readUint8()
        var num = (firstByte and 0b0011_1111u).toLong()
        var isNegative = false
        if ((firstByte and 0b0100_0000u) > 0u) {
            isNegative = true
        }
        if ((firstByte and 0b1000_0000u).toUInt() == 0u) {
            return if (isNegative) -num else num
        }
        var len = 6
        while (true) {
            val byte = requireByte()
            num = num or ((byte.toLong() and 0b0111_1111L) shl len)
            len += 7

            if (byte.toUByte() < 0b1000_0000u) {
                return if (isNegative) -num else num
            }
            if (len > 70) {
                throw IllegalArgumentException("Variable length quantity is too long")
            }
        }
    }

    override fun readVarString(): String {
        val bytes = readVarUint8Array()

        return String(bytes.toByteArray(), StandardCharsets.UTF_8)
    }
}