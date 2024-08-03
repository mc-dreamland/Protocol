package org.cloudburstmc.protocol.common.util;

import com.github.caoli5288.igzip.IGzip;
import com.nukkitx.natives.zlib.Deflater;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.ByteBuffer;

public class IGzipDeflater implements Deflater {

    private final IGzip iGzip;
    private boolean finish;
    private ByteBuf input;

    public IGzipDeflater(int level, boolean nowrap) {
        iGzip = new IGzip(level, nowrap ? IGzip.FMT_Z_NO_HEADER : IGzip.FMT_Z, 0);
    }

    @Override
    public void setLevel(int level) {
        iGzip.setLevel(level);
    }

    public IGzipDeflater level(int level) {
        iGzip.setLevel(level);
        return this;
    }

    @Override
    public void setInput(ByteBuffer input) {
        this.input = Unpooled.wrappedBuffer(input);
    }

    @Override
    public int deflate(ByteBuffer output) {
        return deflate(Unpooled.wrappedBuffer(output));
    }

    public int deflate(ByteBuf output) {
        int compressed = iGzip.compress(input.memoryAddress() + input.readerIndex(),
                input.readableBytes(),
                output.memoryAddress() + output.writerIndex(),
                output.writableBytes());
        finish = true;
        return compressed;
    }

    @Override
    public int getAdler() {
        return 0;
    }

    @Override
    public void reset() {
        // no-op
    }

    @Override
    public boolean finished() {
        return finish;
    }

    @Override
    public void free() {
        iGzip.close();
    }

    public void setInput(ByteBuf input) {
        this.input = input;
        finish = false;
    }
}
