package org.cloudburstmc.protocol.bedrock.codec.v594.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.v504.serializer.ItemStackResponseSerializer_v504;
import org.cloudburstmc.protocol.bedrock.data.inventory.ContainerSlotType;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.response.ItemStackResponse;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.response.ItemStackResponseContainer;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.response.ItemStackResponseSlot;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.response.ItemStackResponseStatus;
import org.cloudburstmc.protocol.bedrock.packet.ItemStackResponsePacket;
import org.cloudburstmc.protocol.common.util.VarInts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ItemStackResponseSerializer_v594 extends ItemStackResponseSerializer_v504 {

    public static final ItemStackResponseSerializer_v594 INSTANCE = new ItemStackResponseSerializer_v594();

    @Override
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, ItemStackResponsePacket packet) {
        helper.writeArray(buffer, packet.getEntries(), (buf, response) -> {
            buf.writeByte(response.getResult().ordinal());
            VarInts.writeInt(buffer, response.getRequestId());

            if (response.getResult() != ItemStackResponseStatus.OK)
                return;

            helper.writeArray(buf, response.getContainers(), (buf2, containerEntry) -> {
                helper.writeContainerSlotType(buf2, containerEntry.getContainer());
                helper.writeArray(buf2, containerEntry.getItems(), this::writeItemEntry);
            });
        });
    }

    @Override
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, ItemStackResponsePacket packet) {
        List<ItemStackResponse> entries = packet.getEntries();
        helper.readArray(buffer, entries, buf -> {
            ItemStackResponseStatus result = ItemStackResponseStatus.values()[buf.readByte()];
            int requestId = VarInts.readInt(buf);

            if (result != ItemStackResponseStatus.OK)
                return new ItemStackResponse(result, requestId, Collections.emptyList());

            List<ItemStackResponseContainer> containerEntries = new ArrayList<>();
            helper.readArray(buf, containerEntries, buf2 -> {
                ContainerSlotType container = helper.readContainerSlotType(buf2);

                List<ItemStackResponseSlot> itemEntries = new ArrayList<>();
                helper.readArray(buf2, itemEntries, byteBuf -> this.readItemEntry(buf2, helper));
                return new ItemStackResponseContainer(container, itemEntries);
            });
            return new ItemStackResponse(result, requestId, containerEntries);
        });
    }

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
        helper.writeString(buffer, "");
        VarInts.writeInt(buffer, itemEntry.getDurabilityCorrection());
    }
}
