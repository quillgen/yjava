import com.riguz.y.lib0.ByteArrayEncoder
import com.riguz.y.lib0.Encoder
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import kotlin.test.assertEquals

@ExperimentalUnsignedTypes
@ExperimentalStdlibApi
class ByteArrayEncoderTest {
    private fun <T> encodeToHex(data: T, encoderFunc: (Encoder, T) -> Unit): String {
        val encoder = ByteArrayEncoder(ByteArrayOutputStream())
        encoderFunc(encoder, data)
        val bytes = encoder.toBytes()
        return bytes.toHexString()
    }

    @Test
    fun writeUint8() {
        val write: (Encoder, UByte) -> Unit = { e, num -> e.writeUint8(num) }
        assertEquals("01", encodeToHex(1u, write))
        assertEquals("7b", encodeToHex(123u, write))
        assertEquals("ff", encodeToHex(255u, write))
    }

    @Test
    fun writeUint16() {
        val write: (Encoder, UShort) -> Unit = { e, num -> e.writeUint16(num) }
        assertEquals("0000", encodeToHex(0u, write))
        assertEquals("0100", encodeToHex(1u, write))
        assertEquals("ff00", encodeToHex(255u, write))
        assertEquals("ffff", encodeToHex(65535u, write))
    }

    @Test
    fun writeUint32() {
        val write: (Encoder, UInt) -> Unit = { e, num -> e.writeUint32(num) }
        assertEquals("00000000", encodeToHex(0u, write))
        assertEquals("01000000", encodeToHex(1u, write))
        assertEquals("ff000000", encodeToHex(255u, write))
        assertEquals("ffff0000", encodeToHex(65535u, write))
        assertEquals("ffffffff", encodeToHex(4294967295u, write))
    }

    @Test
    fun writeUint32BigEndian() {
        val write: (Encoder, UInt) -> Unit = { e, num -> e.writeUint32BigEndian(num) }
        assertEquals("00000000", encodeToHex(0u, write))
        assertEquals("00000001", encodeToHex(1u, write))
        assertEquals("000000ff", encodeToHex(255u, write))
        assertEquals("0000ffff", encodeToHex(65535u, write))
        assertEquals("ffffffff", encodeToHex(4294967295u, write))
    }

    @Test
    fun writeUint64() {
        val write: (Encoder, ULong) -> Unit = { e, num -> e.writeUint64(num) }
        assertEquals("0000000000000000", encodeToHex(0u, write))
        assertEquals("0000000000000001", encodeToHex(1u, write))
        assertEquals("00000000000000ff", encodeToHex(255u, write))
        assertEquals("000000000000ffff", encodeToHex(65535u, write))
        assertEquals("00000000ffffffff", encodeToHex(4294967295u, write))
        assertEquals("ffffffffffffffff", encodeToHex(18446744073709551615u, write))
    }

    @Test
    fun writeFloat32() {
        val write: (Encoder, Float) -> Unit = { e, num -> e.writeFloat32(num) }
        assertEquals("00000000", encodeToHex(0f, write))
        assertEquals("3f800000", encodeToHex(1f, write))
        assertEquals("437f0000", encodeToHex(255f, write))
        assertEquals("477fff00", encodeToHex(65535f, write))
        assertEquals("4f800000", encodeToHex(4294967295f, write))
        assertEquals("42f6e666", encodeToHex(123.45f, write))
        assertEquals("496ffce2", encodeToHex(982990.1f, write))
    }

    @Test
    fun writeFloat64() {
        val write: (Encoder, Double) -> Unit = { e, num -> e.writeFloat64(num) }
        assertEquals("0000000000000000", encodeToHex(0.0, write))
        assertEquals("3ff0000000000000", encodeToHex(1.0, write))
        assertEquals("406fe00000000000", encodeToHex(255.0, write))
        assertEquals("40efffe000000000", encodeToHex(65535.0, write))
        assertEquals("41efffffffe00000", encodeToHex(4294967295.0, write))
        assertEquals("43f0000000000000", encodeToHex(18446744073709552000.0, write))
        assertEquals("405edccccccccccd", encodeToHex(123.45, write))
        assertEquals("412dff9c33333333", encodeToHex(982990.1, write))
    }

    @Test
    fun writeInt64() {
        val write: (Encoder, Long) -> Unit = { e, num -> e.writeInt64(num) }
        assertEquals("0000000000000000", encodeToHex(0, write))
        assertEquals("0000000000000001", encodeToHex(1, write))
        assertEquals("ffffffffffffffff", encodeToHex(-1, write))
        assertEquals("00000000000000ff", encodeToHex(255, write))
        assertEquals("000000000000ffff", encodeToHex(65535, write))
        assertEquals("ffffffffffff0001", encodeToHex(-65535, write))
        assertEquals("8000000000000001", encodeToHex(-9223372036854775807, write))
        assertEquals("7fffffffffffffff", encodeToHex(9223372036854775807, write))
    }

    @Test
    fun writeVarUint8() {
        val write: (Encoder, UByte) -> Unit = { e, num -> e.writeVarUint8(num) }
        assertEquals("00", encodeToHex(0u, write))
        assertEquals("01", encodeToHex(1u, write))
        assertEquals("ff01", encodeToHex(255u, write))
    }

    @Test
    fun writeVarUint16() {
        val write: (Encoder, UShort) -> Unit = { e, num -> e.writeVarUint16(num) }
        assertEquals("00", encodeToHex(0u, write))
        assertEquals("01", encodeToHex(1u, write))
        assertEquals("ff01", encodeToHex(255u, write))
        assertEquals("ffff03", encodeToHex(65535u, write))
    }

    @Test
    fun writeVarUint32() {
        val write: (Encoder, UInt) -> Unit = { e, num -> e.writeVarUint32(num) }
        assertEquals("00", encodeToHex(0u, write))
        assertEquals("01", encodeToHex(1u, write))
        assertEquals("ff01", encodeToHex(255u, write))
        assertEquals("ffff03", encodeToHex(65535u, write))
        assertEquals("ffffffff0f", encodeToHex(4294967295u, write))
    }

    @Test
    fun writeVarUint64() {
        val write: (Encoder, ULong) -> Unit = { e, num -> e.writeVarUint64(num) }
        assertEquals("00", encodeToHex(0u, write))
        assertEquals("01", encodeToHex(1u, write))
        assertEquals("ff01", encodeToHex(255u, write))
        assertEquals("ffff03", encodeToHex(65535u, write))
        assertEquals("ffffffff0f", encodeToHex(4294967295u, write))
    }

    @Test
    fun writeVarInt8() {
        val write: (Encoder, Byte) -> Unit = { e, num -> e.writeVarInt8(num) }
        assertEquals("00", encodeToHex(0, write))
        assertEquals("01", encodeToHex(1, write))
        assertEquals("41", encodeToHex(-1, write))
    }

    @Test
    fun writeVarInt16() {
        val write: (Encoder, Short) -> Unit = { e, num -> e.writeVarInt16(num) }
        assertEquals("00", encodeToHex(0, write))
        assertEquals("01", encodeToHex(1, write))
        assertEquals("bf03", encodeToHex(255, write))
        assertEquals("41", encodeToHex(-1, write))
        assertEquals("ff03", encodeToHex(-255, write))
    }

    @Test
    fun writeVarInt32() {
        val write: (Encoder, Int) -> Unit = { e, num -> e.writeVarInt32(num) }
        assertEquals("00", encodeToHex(0, write))
        assertEquals("01", encodeToHex(1, write))
        assertEquals("bf03", encodeToHex(255, write))
        assertEquals("bfff07", encodeToHex(65535, write))
        assertEquals("41", encodeToHex(-1, write))
        assertEquals("ff03", encodeToHex(-255, write))
        assertEquals("ffff07", encodeToHex(-65535, write))
    }

    @Test
    fun writeVarInt64() {
        val write: (Encoder, Long) -> Unit = { e, num -> e.writeVarInt64(num) }
        assertEquals("00", encodeToHex(0, write))
        assertEquals("01", encodeToHex(1, write))
        assertEquals("bf03", encodeToHex(255, write))
        assertEquals("bfff07", encodeToHex(65535, write))
        assertEquals("bfffffff1f", encodeToHex(4294967295, write))
        assertEquals("41", encodeToHex(-1, write))
        assertEquals("ff03", encodeToHex(-255, write))
        assertEquals("ffff07", encodeToHex(-65535, write))
    }

    @Test
    fun writeUint8Array() {
        val write: (Encoder, UByteArray) -> Unit = { e, str -> e.writeUint8Array(str) }
        val data = ubyteArrayOf(0u, 1u, 128u, 254u)
        assertEquals("000180fe", encodeToHex(data, write))
    }

    @Test
    fun writeVarUint8Array() {
        val write: (Encoder, UByteArray) -> Unit = { e, str -> e.writeVarUint8Array(str) }
        val data = ubyteArrayOf(0u, 1u, 128u, 254u)
        assertEquals("04000180fe", encodeToHex(data, write))
    }

    @Test
    fun writeVarString() {
        val write: (Encoder, String) -> Unit = { e, str -> e.writeVarString(str) }
        assertEquals("0c48656c6c6f20576f726c6421", encodeToHex("Hello World!", write))
        assertEquals("00", encodeToHex("", write))
        assertEquals("12e4bda0e5a5bdefbc8ce4b896e7958cefbc81", encodeToHex("你好，世界！", write))
    }

    @Test
    fun toBytes() {
        val encoder = ByteArrayEncoder(ByteArrayOutputStream())
        encoder.writeUint8(12u)
        encoder.writeVarString("12345，上山打老虎，one two three four five, climb a mountain and hit a tiger")
        encoder.writeInt64(-9223372036854775807)

        val bytes = encoder.toBytes()
        assertEquals(
            "0c533132333435efbc8ce4b88ae5b1b1e68993e88081e8998eefbc8c6f6e652074776f20746872656520666f757220666976652c20636c696d622061206d6f756e7461696e20616e642068697420612074696765728000000000000001",
            bytes.toHexString()
        )

    }
}