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
import org.cloudburstmc.math.vector.Vector2i;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.v313.serializer.NetworkChunkPublisherUpdateSerializer_v313;
import org.cloudburstmc.protocol.bedrock.packet.NetworkChunkPublisherUpdatePacket;
import org.cloudburstmc.protocol.common.util.TriConsumer;
import org.cloudburstmc.protocol.common.util.VarInts;

import java.util.Collection;
import java.util.function.BiFunction;
import java.util.function.ObjIntConsumer;
import java.util.function.ToLongFunction;

public class NetworkChunkPublisherUpdateSerializer_v504 extends NetworkChunkPublisherUpdateSerializer_v313 {

    @Override
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, NetworkChunkPublisherUpdatePacket packet) {
        super.serialize(buffer, helper, packet);

        writeArray(buffer, packet.getSavedChunks(), ByteBuf::writeIntLE, this::writeSavedChunk, helper);
    }

    @Override
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, NetworkChunkPublisherUpdatePacket packet) {
        super.deserialize(buffer, helper, packet);

        readArray(buffer, packet.getSavedChunks(), ByteBuf::readIntLE, this::readSavedChunk, helper);
    }

    protected void writeSavedChunk(ByteBuf buffer, BedrockCodecHelper helper, Vector2i savedChunk) {
        VarInts.writeInt(buffer, savedChunk.getX());
        VarInts.writeInt(buffer, savedChunk.getY());
    }

    protected Vector2i readSavedChunk(ByteBuf buffer, BedrockCodecHelper helper) {
        return Vector2i.from(VarInts.readInt(buffer), VarInts.readInt(buffer));
    }


    public <T> void writeArray(ByteBuf buffer, Collection<T> array, ObjIntConsumer<ByteBuf> lengthWriter,
                               TriConsumer<ByteBuf, BedrockCodecHelper, T> consumer, BedrockCodecHelper helper) {
        lengthWriter.accept(buffer, array.size());
        for (T val : array) {
            consumer.accept(buffer, helper, val);
        }
    }

    public <T> void readArray(ByteBuf buffer, Collection<T> array, ToLongFunction<ByteBuf> lengthReader,
                              BiFunction<ByteBuf, BedrockCodecHelper, T> function, BedrockCodecHelper helper) {
        long length = lengthReader.applyAsLong(buffer);
        for (int i = 0; i < length; i++) {
            array.add(function.apply(buffer, helper));
        }
    }
}
