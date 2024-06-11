package org.cloudburstmc.protocol.bedrock.netty.codec.compression;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.cloudburstmc.protocol.common.util.Zlib;

import java.util.Arrays;

public class LegacyCompressorDetector extends ChannelInboundHandlerAdapter {

    public static final String NAME = "legacy-compressor-detector";
    public static final byte[] MAGIC_VALUES = {(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff};

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf slice = ((ByteBuf) msg).slice();
        byte[] bytes = new byte[4];
        System.out.println("MAGIC_VALUES： " + Arrays.toString(bytes));
        slice.readBytes(bytes);
        if (!Arrays.equals(MAGIC_VALUES, bytes)) {// legacy compression
            ctx.pipeline().addLast(CompressionCodec.NAME, new ZlibCompressionCodec(Zlib.DEFAULT));
        }
        ctx.channel().pipeline().remove(NAME);
        super.channelRead(ctx, msg);
    }
}
