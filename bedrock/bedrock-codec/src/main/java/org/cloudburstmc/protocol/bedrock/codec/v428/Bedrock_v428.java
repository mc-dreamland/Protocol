package org.cloudburstmc.protocol.bedrock.codec.v428;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodec;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.LevelEventSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.LevelSoundEvent1Serializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v313.serializer.LevelSoundEvent2Serializer_v313;
import org.cloudburstmc.protocol.bedrock.codec.v332.serializer.LevelSoundEventSerializer_v332;
import org.cloudburstmc.protocol.bedrock.codec.v388.serializer.AvailableCommandsSerializer_v388;
import org.cloudburstmc.protocol.bedrock.codec.v422.Bedrock_v422;
import org.cloudburstmc.protocol.bedrock.codec.v428.serializer.CameraShakeSerializer_v428;
import org.cloudburstmc.protocol.bedrock.codec.v428.serializer.ClientboundDebugRendererSerializer_v428;
import org.cloudburstmc.protocol.bedrock.codec.v428.serializer.ItemStackResponseSerializer_v428;
import org.cloudburstmc.protocol.bedrock.codec.v428.serializer.PlayerAuthInputSerializer_v428;
import org.cloudburstmc.protocol.bedrock.codec.v428.serializer.StartGameSerializer_v428;
import org.cloudburstmc.protocol.bedrock.data.LevelEvent;
import org.cloudburstmc.protocol.bedrock.data.LevelEventType;
import org.cloudburstmc.protocol.bedrock.data.SoundEvent;
import org.cloudburstmc.protocol.bedrock.data.command.CommandParam;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityData;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityFlag;
import org.cloudburstmc.protocol.bedrock.data.inventory.stackrequestactions.StackRequestActionType;
import org.cloudburstmc.protocol.bedrock.packet.AvailableCommandsPacket;
import org.cloudburstmc.protocol.bedrock.packet.CameraShakePacket;
import org.cloudburstmc.protocol.bedrock.packet.ClientboundDebugRendererPacket;
import org.cloudburstmc.protocol.bedrock.packet.ItemStackResponsePacket;
import org.cloudburstmc.protocol.bedrock.packet.LevelEventPacket;
import org.cloudburstmc.protocol.bedrock.packet.LevelSoundEvent1Packet;
import org.cloudburstmc.protocol.bedrock.packet.LevelSoundEvent2Packet;
import org.cloudburstmc.protocol.bedrock.packet.LevelSoundEventPacket;
import org.cloudburstmc.protocol.bedrock.packet.PlayerAuthInputPacket;
import org.cloudburstmc.protocol.bedrock.packet.StartGamePacket;
import org.cloudburstmc.protocol.common.util.TypeMap;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Bedrock_v428 extends Bedrock_v422 {

    protected static final TypeMap<EntityData> ENTITY_DATA = Bedrock_v422.ENTITY_DATA.toBuilder()
            .shift(60, 1)
            .replace(120, EntityData.FREEZING_EFFECT_STRENGTH)
            .insert(121, EntityData.BUOYANCY_DATA)
            .insert(122, EntityData.GOAT_HORN_COUNT)
            .insert(123, EntityData.BASE_RUNTIME_ID)
            .build();

    protected static final TypeMap<EntityFlag> ENTITY_FLAGS = Bedrock_v422.ENTITY_FLAGS.toBuilder()
            .insert(96, EntityFlag.RAM_ATTACK)
            .build();

    protected static final TypeMap<LevelEventType> LEVEL_EVENTS = Bedrock_v422.LEVEL_EVENTS.toBuilder()
            .insert(LEVEL_EVENT_PARTICLE + 27, LevelEvent.PARTICLE_VIBRATION_SIGNAL)
            .insert(LEVEL_EVENT_BLOCK + 14, LevelEvent.CAULDRON_FILL_POWDER_SNOW)
            .insert(LEVEL_EVENT_BLOCK + 15, LevelEvent.CAULDRON_TAKE_POWDER_SNOW)
            .build();

    protected static final TypeMap<CommandParam> COMMAND_PARAMS = Bedrock_v422.COMMAND_PARAMS.toBuilder()
            .shift(2, 1)
            .shift(57, 6)
            .build();

    protected static final TypeMap<SoundEvent> SOUND_EVENTS = Bedrock_v422.SOUND_EVENTS.toBuilder()
            .replace(318, SoundEvent.AMBIENT_LOOP_WARPED_FOREST)
            .insert(319, SoundEvent.AMBIENT_LOOP_SOULSAND_VALLEY)
            .insert(320, SoundEvent.AMBIENT_LOOP_NETHER_WASTES)
            .insert(321, SoundEvent.AMBIENT_LOOP_BASALT_DELTAS)
            .insert(322, SoundEvent.AMBIENT_LOOP_CRIMSON_FOREST)
            .insert(323, SoundEvent.AMBIENT_ADDITION_WARPED_FOREST)
            .insert(324, SoundEvent.AMBIENT_ADDITION_SOULSAND_VALLEY)
            .insert(325, SoundEvent.AMBIENT_ADDITION_NETHER_WASTES)
            .insert(326, SoundEvent.AMBIENT_ADDITION_BASALT_DELTAS)
            .insert(327, SoundEvent.AMBIENT_ADDITION_CRIMSON_FOREST)
            .insert(328, SoundEvent.SCULK_SENSOR_POWER_ON)
            .insert(329, SoundEvent.SCULK_SENSOR_POWER_OFF)
            .insert(330, SoundEvent.BUCKET_FILL_POWDER_SNOW)
            .insert(331, SoundEvent.BUCKET_EMPTY_POWDER_SNOW)
            .insert(332, SoundEvent.UNDEFINED)
            .build();

    protected static final TypeMap<StackRequestActionType> ITEM_STACK_REQUEST_TYPES = Bedrock_v422.ITEM_STACK_REQUEST_TYPES.toBuilder()
            .shift(9, 1)
            .insert(9, StackRequestActionType.MINE_BLOCK)
            .build();

    public static final BedrockCodec CODEC = Bedrock_v422.CODEC.toBuilder()
            .protocolVersion(428)
            .minecraftVersion("1.16.210")
            .helper(() -> new BedrockCodecHelper_v428(ENTITY_DATA, ENTITY_DATA_TYPES, ENTITY_FLAGS, GAME_RULE_TYPES, ITEM_STACK_REQUEST_TYPES))
            .updateSerializer(StartGamePacket.class, StartGameSerializer_v428.INSTANCE)
            .updateSerializer(PlayerAuthInputPacket.class, PlayerAuthInputSerializer_v428.INSTANCE)
            .updateSerializer(ItemStackResponsePacket.class, ItemStackResponseSerializer_v428.INSTANCE)
            .updateSerializer(CameraShakePacket.class, CameraShakeSerializer_v428.INSTANCE)
            .updateSerializer(LevelSoundEvent1Packet.class, new LevelSoundEvent1Serializer_v291(SOUND_EVENTS))
            .updateSerializer(LevelSoundEvent2Packet.class, new LevelSoundEvent2Serializer_v313(SOUND_EVENTS))
            .updateSerializer(LevelSoundEventPacket.class, new LevelSoundEventSerializer_v332(SOUND_EVENTS))
            .updateSerializer(AvailableCommandsPacket.class, new AvailableCommandsSerializer_v388(COMMAND_PARAMS))
            .updateSerializer(LevelEventPacket.class, new LevelEventSerializer_v291(LEVEL_EVENTS))
            .registerPacket(ClientboundDebugRendererPacket.class, ClientboundDebugRendererSerializer_v428.INSTANCE, 164)
            .build();
}