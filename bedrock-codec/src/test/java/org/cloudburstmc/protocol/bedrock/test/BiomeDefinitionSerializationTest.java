package org.cloudburstmc.protocol.bedrock.test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodec;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketDefinition;
import org.cloudburstmc.protocol.bedrock.codec.v819.Bedrock_v819;
import org.cloudburstmc.protocol.bedrock.codec.v827.Bedrock_v827;
import org.cloudburstmc.protocol.bedrock.data.biome.BiomeDefinitionData;
import org.cloudburstmc.protocol.bedrock.data.biome.BiomeDefinitions;
import org.cloudburstmc.protocol.bedrock.packet.BiomeDefinitionListPacket;
import org.junit.jupiter.api.Test;

import java.awt.Color;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class BiomeDefinitionSerializationTest {

    @Test
    public void testV819RoundTripPreservesDimensionAndVanilla() {
        BiomeDefinitionData definition = new BiomeDefinitionData("test_id", 1.0f, 0.5f,
                0.1f, 0.2f, 0.3f, 0.4f, 0.0f, 1.5f, 0.8f,
                new Color(12, 34, 56, 78), true, 2, "minecraft:test_vanilla",
                Arrays.asList("tag_a", "tag_b"), null);

        BiomeDefinitionData actual = roundTrip(Bedrock_v819.CODEC, "test:custom_biome", definition);

        assertEquals(2, actual.getDimension());
        assertEquals("minecraft:test_vanilla", actual.getVanilla());
        assertEquals("test_id", actual.getId());
    }

    @Test
    public void testV827RoundTripPreservesDimensionAndVanillaForVanillaBiome() {
        BiomeDefinitionData definition = new BiomeDefinitionData(null, 1.0f, 0.5f,
                0.1f, 0.2f, 0.3f, 0.4f, 0.0f, 1.5f, 0.8f,
                new Color(90, 80, 70, 60), false, 1, "minecraft:plains",
                Arrays.asList("overworld"), null);

        BiomeDefinitionData actual = roundTrip(Bedrock_v827.CODEC, "minecraft:plains", definition);

        assertEquals(1, actual.getDimension());
        assertEquals("minecraft:plains", actual.getVanilla());
        assertNull(actual.getId());
    }

    private BiomeDefinitionData roundTrip(BedrockCodec codec, String biomeName, BiomeDefinitionData definition) {
        BedrockPacketDefinition<BiomeDefinitionListPacket> packetDefinition = codec.getPacketDefinition(BiomeDefinitionListPacket.class);
        BedrockCodecHelper helper = codec.createHelper();

        Map<String, BiomeDefinitionData> definitions = new LinkedHashMap<>();
        definitions.put(biomeName, definition);

        BiomeDefinitionListPacket packet = new BiomeDefinitionListPacket();
        packet.setBiomes(new BiomeDefinitions(definitions));

        ByteBuf buffer = Unpooled.buffer();
        packetDefinition.getSerializer().serialize(buffer, helper, packet);

        BiomeDefinitionListPacket deserializedPacket = new BiomeDefinitionListPacket();
        packetDefinition.getSerializer().deserialize(buffer, helper, deserializedPacket);

        BiomeDefinitionData actual = deserializedPacket.getBiomes().getDefinitions().get(biomeName);
        assertNotNull(actual);
        return actual;
    }
}
