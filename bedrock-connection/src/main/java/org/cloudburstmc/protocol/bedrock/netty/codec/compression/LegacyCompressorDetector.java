package org.cloudburstmc.protocol.bedrock.netty.codec.compression;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.cloudburstmc.protocol.common.util.Zlib;

import java.util.Arrays;

public class LegacyCompressorDetector extends ChannelInboundHandlerAdapter {

    public static final String NAME = "legacy-compressor-detector";
    public static final byte[] MAGIC_VALUES = {(byte) 6, (byte) -63, (byte) 1, (byte) 0};

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf slice = ((ByteBuf) msg).slice();
        byte[] bytes = new byte[4];
        slice.readBytes(bytes);
        if (!Arrays.equals(MAGIC_VALUES, bytes)) {// legacy compression
            ctx.pipeline().replace(NAME, CompressionCodec.NAME, new ZlibCompressionCodec(Zlib.RAW));
        } else {
            ctx.channel().pipeline().remove(NAME);
        }
        super.channelRead(ctx, msg);
    }
}
