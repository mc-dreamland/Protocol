package org.cloudburstmc.protocol.bedrock.codec.v594;

import org.cloudburstmc.protocol.bedrock.codec.BedrockCodec;
import org.cloudburstmc.protocol.bedrock.codec.EntityDataTypeMap;
import org.cloudburstmc.protocol.bedrock.codec.v575.BedrockCodecHelper_v575;
import org.cloudburstmc.protocol.bedrock.codec.v575.Bedrock_v575;
import org.cloudburstmc.protocol.bedrock.codec.v589.Bedrock_v589;
import org.cloudburstmc.protocol.bedrock.codec.v594.serializer.AgentAnimationSerializer_v594;
import org.cloudburstmc.protocol.bedrock.codec.v594.serializer.AvailableCommandsSerializer_v594;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityDataFormat;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityDataTypes;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityFlag;
import org.cloudburstmc.protocol.bedrock.packet.AgentAnimationPacket;
import org.cloudburstmc.protocol.bedrock.packet.AvailableCommandsPacket;
import org.cloudburstmc.protocol.bedrock.packet.ScriptCustomEventPacket;
import org.cloudburstmc.protocol.bedrock.transformer.BooleanTransformer;
import org.cloudburstmc.protocol.bedrock.transformer.FlagTransformer;
import org.cloudburstmc.protocol.common.util.TypeMap;

public class Bedrock_v594 extends Bedrock_v589 {

    protected static final TypeMap<EntityFlag> ENTITY_FLAGS = Bedrock_v575.ENTITY_FLAGS
            .toBuilder()
            .insert(114, EntityFlag.CRAWLING)
            .build();

    protected static final EntityDataTypeMap ENTITY_DATA = Bedrock_v589.ENTITY_DATA
            .toBuilder()
            .insert(EntityDataTypes.PLAYER_LAST_DEATH_DIMENSION , 128, EntityDataFormat.INT)
            .insert(EntityDataTypes.PLAYER_HAS_DIED, 129, EntityDataFormat.BYTE, BooleanTransformer.INSTANCE)
            .insert(EntityDataTypes.COLLISION_BOX, 130, EntityDataFormat.VECTOR3F)
            .update(EntityDataTypes.FLAGS, new FlagTransformer(ENTITY_FLAGS, 0))
            .update(EntityDataTypes.FLAGS_2, new FlagTransformer(ENTITY_FLAGS, 1))
            .build();

    public static final BedrockCodec CODEC = Bedrock_v589.CODEC.toBuilder()
            .raknetProtocolVersion(11)
            .protocolVersion(594)
            .minecraftVersion("1.20.10")
            .helper(() -> new BedrockCodecHelper_v575(ENTITY_DATA, GAME_RULE_TYPES, ITEM_STACK_REQUEST_TYPES, CONTAINER_SLOT_TYPES, PLAYER_ABILITIES, TEXT_PROCESSING_ORIGINS))
            .deregisterPacket(ScriptCustomEventPacket.class)
            .updateSerializer(AvailableCommandsPacket.class, new AvailableCommandsSerializer_v594(COMMAND_PARAMS)) // TODO: chained command deserialization needs more work
            .registerPacket(AgentAnimationPacket::new, new AgentAnimationSerializer_v594(), 304)
            .build();
}
