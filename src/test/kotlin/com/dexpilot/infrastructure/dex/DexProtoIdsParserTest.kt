package com.dexpilot.infrastructure.dex

import com.dexpilot.domain.dex.model.DexHeader
import com.dexpilot.domain.dex.model.DexStringData
import com.dexpilot.domain.dex.model.DexStringSummary
import com.dexpilot.domain.dex.model.DexTypeData
import com.dexpilot.domain.dex.model.DexTypeSummary
import com.dexpilot.infrastructure.dex.DexParseErrorCode
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.nio.file.Paths

class DexProtoIdsParserTest {

    private val parser = DexProtoIdsParser()

    // ── helpers ──────────────────────────────────────────────────────────────

    private fun loadFixture(name: String): ByteArray =
        Paths.get("src/test/resources/fixtures/valid/$name").toFile().readBytes()

    private fun stubHeader(
        protoIdsSize: Long,
        protoIdsOff: Long,
        stringIdsSize: Long = 2L,
        typeIdsSize: Long = 1L,
    ) = DexHeader(
        magicText = "dex\n040\u0000",
        version = "040",
        checksum = 0L,
        signatureHex = "0".repeat(40),
        fileSize = 0L,
        headerSize = 112L,
        endianTag = 0x12345678L,
        linkSize = 0L,
        linkOff = 0L,
        mapOff = 0L,
        stringIdsSize = stringIdsSize,
        stringIdsOff = 0L,
        typeIdsSize = typeIdsSize,
        typeIdsOff = 0L,
        protoIdsSize = protoIdsSize,
        protoIdsOff = protoIdsOff,
        fieldIdsSize = 0L,
        fieldIdsOff = 0L,
        methodIdsSize = 0L,
        methodIdsOff = 0L,
        classDefsSize = 0L,
        classDefsOff = 0L,
        dataSize = 0L,
        dataOff = 0L,
    )

    private fun stubStringSummary() = DexStringSummary(
        declaredCount = 2,
        sample = listOf(
            DexStringData(index = 0, offset = 0, declaredUtf16Size = 1, value = "V"),
            DexStringData(index = 1, offset = 0, declaredUtf16Size = 18, value = "Ljava/lang/Object;"),
        ),
    )

    private fun stubTypeSummary() = DexTypeSummary(
        declaredCount = 1,
        sample = listOf(
            DexTypeData(index = 0, descriptorIndex = 1, descriptor = "Ljava/lang/Object;"),
        ),
    )

    // ── testes ────────────────────────────────────────────────────────────────

    @Test
    fun `retorna summary vazio quando proto_ids_size e zero`() {
        val bytes = loadFixture("minimal-type-ids.dex")
        val header = DexHeaderParser().parse(bytes)

        val result = parser.parse(bytes, header, stringSummary = null, typeSummary = null)

        result.declaredCount shouldBe 0
        result.sample shouldBe emptyList()
    }

    @Test
    fun `parse proto_ids da fixture minimal-proto-ids dex`() {
        val bytes = loadFixture("minimal-proto-ids.dex")
        val header = DexHeaderParser().parse(bytes)
        val stringSummary = stubStringSummary()
        val typeSummary = stubTypeSummary()

        val result = parser.parse(bytes, header, stringSummary, typeSummary)

        result.declaredCount shouldBe 1
        result.sample.size shouldBe 1

        val proto = result.sample[0]
        proto.index shouldBe 0
        proto.shortyIndex shouldBe 0
        proto.returnTypeIndex shouldBe 0
        proto.parametersOffset shouldBe 0
        proto.shortyDescriptor shouldBe "V"
        proto.returnTypeDescriptor shouldBe "Ljava/lang/Object;"
    }

    @Test
    fun `retorna fallback quando string nao esta na amostra`() {
        val bytes = loadFixture("minimal-proto-ids.dex")
        val header = DexHeaderParser().parse(bytes)

        val result = parser.parse(bytes, header, stringSummary = null, typeSummary = null)

        val proto = result.sample[0]
        proto.shortyDescriptor shouldBe "<string_not_sampled:0>"
        proto.returnTypeDescriptor shouldBe "<type_not_sampled:0>"
    }

    @Test
    fun `lanca excecao quando proto_ids_off esta fora dos limites`() {
        val bytes = ByteArray(200) { 0 }
        val header = stubHeader(protoIdsSize = 1L, protoIdsOff = 999L)

        val ex = shouldThrow<DexParseException> {
            parser.parse(bytes, header, stringSummary = null, typeSummary = null)
        }
        ex.code shouldBe DexParseErrorCode.INVALID_PROTO_IDS
    }

    @Test
    fun `lanca excecao quando tabela proto_ids excede o arquivo`() {
        val bytes = ByteArray(140) { 0 }
        val header = stubHeader(protoIdsSize = 10L, protoIdsOff = 124L)

        val ex = shouldThrow<DexParseException> {
            parser.parse(bytes, header, stringSummary = null, typeSummary = null)
        }
        ex.code shouldBe DexParseErrorCode.INVALID_PROTO_IDS
    }

    @Test
    fun `lanca excecao quando shorty_idx esta fora do range de string_ids`() {
        val bytes = loadFixture("minimal-proto-ids.dex")
        // stringIdsSize=0 força shorty_idx=0 a ser inválido
        val header = DexHeaderParser().parse(bytes).copy(stringIdsSize = 0L)

        val ex = shouldThrow<DexParseException> {
            parser.parse(bytes, header, stringSummary = null, typeSummary = null)
        }
        ex.code shouldBe DexParseErrorCode.INVALID_PROTO_IDS
    }

    @Test
    fun `lanca excecao quando return_type_idx esta fora do range de type_ids`() {
        val bytes = loadFixture("minimal-proto-ids.dex")
        // typeIdsSize=0 força return_type_idx=0 a ser inválido
        val header = DexHeaderParser().parse(bytes).copy(typeIdsSize = 0L)

        val ex = shouldThrow<DexParseException> {
            parser.parse(bytes, header, stringSummary = null, typeSummary = null)
        }
        ex.code shouldBe DexParseErrorCode.INVALID_PROTO_IDS
    }
}
