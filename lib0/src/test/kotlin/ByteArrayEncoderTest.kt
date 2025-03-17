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
        assertEquals("01", encodeToHex<UByte>(1u) { e, num -> e.writeUint8(num) })
        assertEquals("7b", encodeToHex<UByte>(123u) { e, num -> e.writeUint8(num) })
        assertEquals("ff", encodeToHex<UByte>(255u) { e, num -> e.writeUint8(num) })
    }

    @Test
    fun writeUint16() {
    }

    @Test
    fun writeUint32() {
    }

    @Test
    fun writeUint32BigEndian() {
    }

    @Test
    fun writeUint64() {
    }

    @Test
    fun writeFloat32() {
    }

    @Test
    fun writeFloat64() {
    }

    @Test
    fun writeInt64() {
    }

    @Test
    fun writeVarUint8() {
    }

    @Test
    fun writeVarUint16() {
    }

    @Test
    fun writeVarUint32() {
    }

    @Test
    fun writeVarUint64() {
    }

    @Test
    fun writeVarInt8() {
    }

    @Test
    fun writeVarInt16() {
    }

    @Test
    fun writeVarInt32() {
    }

    @Test
    fun writeVarInt64() {
    }

    @Test
    fun writeVarUint8Array() {
    }

    @Test
    fun writeVarString() {
    }

    @Test
    fun toBytes() {
    }
}