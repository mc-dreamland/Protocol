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
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.MapInfoRequestSerializer_v291;
import org.cloudburstmc.protocol.bedrock.data.map.MapPixel;
import org.cloudburstmc.protocol.bedrock.packet.MapInfoRequestPacket;

public class MapInfoRequestSerializer_v504 extends MapInfoRequestSerializer_v291 {

    @Override
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, MapInfoRequestPacket packet) {
        super.serialize(buffer, helper, packet);

        helper.writeArray(buffer, packet.getPixels(), ByteBuf::writeIntLE, (buf, aHelper, pixel) -> {
            buf.writeIntLE(pixel.getPixel());
            buf.writeShortLE(pixel.getIndex());
        });
    }

    @Override
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, MapInfoRequestPacket packet) {
        super.deserialize(buffer, helper, packet);

        helper.readArray(buffer, packet.getPixels(), ByteBuf::readUnsignedIntLE, (buf, aHelper) -> {
            int pixel = buf.readIntLE();
            int index = buf.readUnsignedShortLE();
            return new MapPixel(pixel, index);
        });
    }
}
