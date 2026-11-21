package org.cloudburstmc.protocol.bedrock.codec.v860;

import org.cloudburstmc.protocol.bedrock.codec.BedrockCodec;
import org.cloudburstmc.protocol.bedrock.codec.v819.serializer.BiomeDefinitionListSerializer_v819;
import org.cloudburstmc.protocol.bedrock.codec.v859.Bedrock_v859;
import org.cloudburstmc.protocol.bedrock.codec.v860.serializer.BiomeDefinitionListSerializer_v860;
import org.cloudburstmc.protocol.bedrock.packet.BiomeDefinitionListPacket;

public class Bedrock_v860 extends Bedrock_v859 {

    public static final BedrockCodec CODEC = Bedrock_v859.CODEC.toBuilder()
            .protocolVersion(860)
            .minecraftVersion("1.21.124")
            .updateSerializer(BiomeDefinitionListPacket.class, BiomeDefinitionListSerializer_v860.INSTANCE)
            .build();
}
