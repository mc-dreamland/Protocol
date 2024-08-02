package org.cloudburstmc.protocol.bedrock.codec.v504.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.v422.serializer.ItemStackResponseSerializer_v422;
import org.cloudburstmc.protocol.bedrock.codec.v428.serializer.ItemStackResponseSerializer_v428;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.response.ItemStackResponseSlot;
import org.cloudburstmc.protocol.common.util.VarInts;

public class ItemStackResponseSerializer_v504 extends ItemStackResponseSerializer_v428 {

    public static final ItemStackResponseSerializer_v504 INSTANCE = new ItemStackResponseSerializer_v504();

    @Override
    protected ItemStackResponseSlot readItemEntry(ByteBuf buffer, BedrockCodecHelper helper) {
        return new ItemStackResponseSlot(
                buffer.readUnsignedByte(),
                buffer.readUnsignedByte(),
                buffer.readUnsignedByte(),
                VarInts.readInt(buffer),
                helper.readString(buffer),
                VarInts.readInt(buffer));
    }

    @Override
    protected void writeItemEntry(ByteBuf buffer, BedrockCodecHelper helper, ItemStackResponseSlot itemEntry) {
        buffer.writeByte(itemEntry.getSlot());
        buffer.writeByte(itemEntry.getHotbarSlot());
        buffer.writeByte(itemEntry.getCount());
        VarInts.writeInt(buffer, itemEntry.getStackNetworkId());
        VarInts.writeInt(buffer, itemEntry.getDurabilityCorrection());
        helper.writeString(buffer, itemEntry.getCustomName());
    }
}
