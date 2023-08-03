package org.cloudburstmc.protocol.bedrock.codec.v504.serializer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.SneakyThrows;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.packet.NeteaseCustomPacket;

public class NeteaseCustomSerializer_v504 implements BedrockPacketSerializer<NeteaseCustomPacket> {

    @SneakyThrows
    @Override
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, NeteaseCustomPacket packet) {
        byte[] msgPackData = packet.getMsgPackBytes();
        helper.writeByteArray(buffer, msgPackData);
        buffer.writeBytes(Unpooled.wrappedBuffer(new byte[]{8, -44, -108, 0}));
    }

    @Override
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, NeteaseCustomPacket packet) {
        try {
            byte[] bytes = helper.readByteArray(buffer);
            packet.init(bytes);
            packet.setUnKnowId(buffer.readUnsignedInt());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
