import com.riguz.y.lib0.ByteArrayDecoder
import com.riguz.y.lib0.Decoder
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.ByteArrayInputStream
import java.io.EOFException
import kotlin.test.assertEquals

@ExperimentalUnsignedTypes
@OptIn(ExperimentalStdlibApi::class)
class ByteArrayDecoderTest {
    @OptIn(ExperimentalStdlibApi::class)
    private fun <T> decode(hex: String, decoderFunc: (Decoder) -> T): T {
        val bytes = hex.hexToByteArray()
        val decoder = ByteArrayDecoder(ByteArrayInputStream(bytes));
        return decoderFunc(decoder)
    }

    @Test
    fun readUint8Array() {
        val read: (Decoder, Int) -> UByteArray = { d, l -> d.readUint8Array(l) }
        assertEquals("", decode("3f4c5b2a") { d -> read(d, 0) }.toHexString())
        assertEquals("3f", decode("3f4c5b2a") { d -> read(d, 1) }.toHexString())
        assertEquals("3f4c5b2a", decode("3f4c5b2a") { d -> read(d, 4) }.toHexString())
        assertThrows<EOFException> { decode("3f4c5b2a") { d -> read(d, 5) }.toHexString() }
    }

    @Test
    fun readUint8() {
        val decoder = ByteArrayDecoder(ByteArrayInputStream("3f4c5b2a".hexToByteArray()));
        assertEquals(0x3fu, decoder.readUint8())
        assertEquals(0x4cu, decoder.readUint8())
        assertEquals(0x5bu, decoder.readUint8())
        assertEquals(0x2au, decoder.readUint8())
    }

    @Test
    fun readUint16() {
        val read: (Decoder) -> UShort = { d -> d.readUint16() }
        assertEquals(0u, decode("00000000", read))
        assertEquals(1u, decode("01000000", read))
        assertEquals(255u, decode("ff000000", read))
        assertEquals(65535u, decode("ffff0000", read))
    }

    @Test
    fun readUint32() {
        val read: (Decoder) -> UInt = { d -> d.readUint32() }
        assertEquals(0u, decode("00000000", read))
        assertEquals(1u, decode("01000000", read))
        assertEquals(255u, decode("ff000000", read))
        assertEquals(65535u, decode("ffff0000", read))
        assertEquals(4294967295u, decode("ffffffff", read))
    }

    @Test
    fun readUint32BigEndian() {
        val read: (Decoder) -> UInt = { d -> d.readUint32BigEndian() }
        assertEquals(0u, decode("00000000", read))
        assertEquals(1u, decode("00000001", read))
        assertEquals(255u, decode("000000ff", read))
        assertEquals(65535u, decode("0000ffff", read))
        assertEquals(4294967295u, decode("ffffffff", read))
    }

    @Test
    fun readUint64() {
        val read: (Decoder) -> ULong = { d -> d.readUint64() }
        assertEquals(0u, decode("0000000000000000", read))
        assertEquals(1u, decode("0000000000000001", read))
        assertEquals(255u, decode("00000000000000ff", read))
        assertEquals(65535u, decode("000000000000ffff", read))
        assertEquals(4294967295u, decode("00000000ffffffff", read))
        assertEquals(18446744073709551615u, decode("ffffffffffffffff", read))
    }

    @Test
    fun readFloat32() {
        val read: (Decoder) -> Float = { d -> d.readFloat32() }
        assertEquals(0f, decode("00000000", read))
        assertEquals(1f, decode("3f800000", read))
        assertEquals(255f, decode("437f0000", read))
        assertEquals(65535f, decode("477fff00", read))
        assertEquals(4294967295f, decode("4f800000", read))
        assertEquals(123.45f, decode("42f6e666", read))
        assertEquals(982990.1f, decode("496ffce2", read))
    }

    @Test
    fun readFloat64() {
        val read: (Decoder) -> Double = { d -> d.readFloat64() }
        assertEquals(0.0, decode("0000000000000000", read))
        assertEquals(1.0, decode("3ff0000000000000", read))
        assertEquals(255.0, decode("406fe00000000000", read))
        assertEquals(65535.0, decode("40efffe000000000", read))
        assertEquals(4294967295.0, decode("41efffffffe00000", read))
        assertEquals(18446744073709552000.0, decode("43f0000000000000", read))
        assertEquals(123.45, decode("405edccccccccccd", read))
        assertEquals(982990.1, decode("412dff9c33333333", read))
    }

    @Test
    fun readInt64() {
        val read: (Decoder) -> Long = { d -> d.readInt64() }
        assertEquals(0, decode("0000000000000000", read))
        assertEquals(1, decode("0000000000000001", read))
        assertEquals(-1, decode("ffffffffffffffff", read))
        assertEquals(255, decode("00000000000000ff", read))
        assertEquals(65535, decode("000000000000ffff", read))
        assertEquals(-65535, decode("ffffffffffff0001", read))
        assertEquals(-9223372036854775807, decode("8000000000000001", read))
        assertEquals(9223372036854775807, decode("7fffffffffffffff", read))
    }

    @Test
    fun readVarUint8Array() {
        val decoder = ByteArrayDecoder(ByteArrayInputStream("04000180fe".hexToByteArray()));
        val array = decoder.readVarUint8Array()
        assertEquals(ubyteArrayOf(0u, 1u, 128u, 254u).toHexString(), array.toHexString())
    }

    @Test
    fun readVarUint() {
        val read: (Decoder) -> ULong = { d -> d.readVarUint() }
        assertEquals(0u, decode("00", read))
        assertEquals(1u, decode("01", read))
        assertEquals(255u, decode("ff01", read))
        assertEquals(65535u, decode("ffff03", read))
        assertEquals(4294967295u, decode("ffffffff0f", read))
    }

    @Test
    fun readVarInt() {
        val read: (Decoder) -> Long = { d -> d.readVarInt() }
        assertEquals(0, decode("00", read))
        assertEquals(1, decode("01", read))
        assertEquals(255, decode("bf03", read))
        assertEquals(65535, decode("bfff07", read))
        assertEquals(4294967295, decode("bfffffff1f", read))
        assertEquals(-1, decode("41", read))
        assertEquals(-255, decode("ff03", read))
        assertEquals(-65535, decode("ffff07", read))
    }

    @Test
    fun readVarString() {
        val read: (Decoder) -> String = { d -> d.readVarString() }
        assertEquals("Hello World!", decode("0c48656c6c6f20576f726c6421", read))
        assertEquals("", decode("00", read))
        assertEquals("你好，世界！", decode("12e4bda0e5a5bdefbc8ce4b896e7958cefbc81", read))
    }
}