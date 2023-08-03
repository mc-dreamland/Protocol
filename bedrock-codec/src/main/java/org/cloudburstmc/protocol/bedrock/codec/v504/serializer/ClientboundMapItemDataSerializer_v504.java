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

package org.cloudburstmc.protocol.bedrock.codec.v504.serializer;

import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.longs.LongList;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.v354.serializer.ClientboundMapItemDataSerializer_v354;
import org.cloudburstmc.protocol.bedrock.data.MapDecoration;
import org.cloudburstmc.protocol.bedrock.data.MapTrackedObject;
import org.cloudburstmc.protocol.bedrock.packet.ClientboundMapItemDataPacket;
import org.cloudburstmc.protocol.common.util.VarInts;

import java.util.List;

public class ClientboundMapItemDataSerializer_v504 extends ClientboundMapItemDataSerializer_v354 {


    @Override
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, ClientboundMapItemDataPacket packet) {
        VarInts.writeLong(buffer, packet.getUniqueMapId());

        int type = 0;
        int[] colors = packet.getColors();
        if (colors != null && colors.length > 0) {
            type |= FLAG_TEXTURE_UPDATE;
        }
        List<MapDecoration> decorations = packet.getDecorations();
        List<MapTrackedObject> trackedObjects = packet.getTrackedObjects();
        if (!decorations.isEmpty() && !trackedObjects.isEmpty()) {
            type |= FLAG_DECORATION_UPDATE;
        }
        LongList trackedEntityIds = packet.getTrackedEntityIds();
        if (!trackedEntityIds.isEmpty()) {
            type |= FLAG_MAP_CREATION;
        }

        VarInts.writeUnsignedInt(buffer, type);
        buffer.writeByte(packet.getDimensionId());
        buffer.writeBoolean(packet.isLocked());
        helper.writeBlockPosition(buffer, packet.getOrigin());

        if ((type & FLAG_MAP_CREATION) != 0) {
            this.writeMapCreation(buffer, helper, packet);
        }

        if ((type & FLAG_ALL) != 0) {
            buffer.writeByte(packet.getScale());
        }

        if ((type & FLAG_DECORATION_UPDATE) != 0) {
            this.writeMapDecorations(buffer, helper, packet);
        }

        if ((type & FLAG_TEXTURE_UPDATE) != 0) {
            this.writeTextureUpdate(buffer, helper, packet);
        }
    }

    @Override
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, ClientboundMapItemDataPacket packet) {
        packet.setUniqueMapId(VarInts.readLong(buffer));
        int type = VarInts.readUnsignedInt(buffer);
        packet.setDimensionId(buffer.readUnsignedByte());
        packet.setLocked(buffer.readBoolean());
        packet.setOrigin(helper.readBlockPosition(buffer));

        if ((type & FLAG_MAP_CREATION) != 0) {
            this.readMapCreation(buffer, helper, packet);
        }

        if ((type & FLAG_ALL) != 0) {
            packet.setScale(buffer.readUnsignedByte());
        }

        if ((type & FLAG_DECORATION_UPDATE) != 0) {
            this.readMapDecorations(buffer, helper, packet);
        }

        if ((type & FLAG_TEXTURE_UPDATE) != 0) {
            this.readTextureUpdate(buffer, helper, packet);
        }
    }
}
