/*
 * Copyright (c) 2019-2023 GeyserMC. http://geysermc.org
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 * @author GeyserMC
 * @link https://github.com/GeyserMC/Geyser
 */

package org.cloudburstmc.protocol.bedrock.codec.v504;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodec;
import org.cloudburstmc.protocol.bedrock.codec.EntityDataTypeMap;
import org.cloudburstmc.protocol.bedrock.codec.v407.Bedrock_v407;
import org.cloudburstmc.protocol.bedrock.codec.v503.BedrockCodecHelper_v503;
import org.cloudburstmc.protocol.bedrock.codec.v503.Bedrock_v503;
import org.cloudburstmc.protocol.bedrock.codec.v504.serializer.*;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityDataFormat;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityDataTypes;
import org.cloudburstmc.protocol.bedrock.data.inventory.ContainerSlotType;
import org.cloudburstmc.protocol.bedrock.packet.*;
import org.cloudburstmc.protocol.bedrock.transformer.BooleanTransformer;
import org.cloudburstmc.protocol.common.util.TypeMap;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Bedrock_v504 extends Bedrock_v503 {

    protected static final TypeMap<ContainerSlotType> CONTAINER_SLOT_TYPES = Bedrock_v407.CONTAINER_SLOT_TYPES.toBuilder()
            .shift(15, 1)
            .insert(15, ContainerSlotType.NULL)
            .build();
    protected static final EntityDataTypeMap ENTITY_DATA = Bedrock_v503.ENTITY_DATA.toBuilder()
            .insert(EntityDataTypes.PLAYER_LAST_DEATH_DIMENSION , 128, EntityDataFormat.INT)
            .insert(EntityDataTypes.PLAYER_HAS_DIED, 129, EntityDataFormat.BYTE, BooleanTransformer.INSTANCE)
            .build();

    public static final BedrockCodec CODEC = Bedrock_v503.CODEC.toBuilder()
            .protocolVersion(504)
            .minecraftVersion("1.18.32")
            .helper(() -> new BedrockCodecHelper_v503(ENTITY_DATA, GAME_RULE_TYPES, ITEM_STACK_REQUEST_TYPES, CONTAINER_SLOT_TYPES))
            .updateSerializer(StartGamePacket.class, new StartGameSerializer_v504())
            .updateSerializer(NetworkChunkPublisherUpdatePacket.class, new NetworkChunkPublisherUpdateSerializer_v504())
            .updateSerializer(ClientboundMapItemDataPacket.class, new ClientboundMapItemDataSerializer_v504())
            .updateSerializer(MapInfoRequestPacket.class, new MapInfoRequestSerializer_v504())
            .registerPacket(FeatureRegistryPacket::new, new FeatureRegistrySerializer_v504(), 191)
            .registerPacket(NeteasePythonRpcPacket::new, new NeteasePythonRpcSerializer_v504(), 200)
            .registerPacket(ConfirmSkinPacket::new, new ConfirmSkinSerializer_v504(), 228)
            .build();
}
