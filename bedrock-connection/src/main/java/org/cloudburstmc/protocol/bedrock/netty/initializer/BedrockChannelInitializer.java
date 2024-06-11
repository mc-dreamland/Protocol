package org.cloudburstmc.protocol.bedrock.netty.initializer;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import org.cloudburstmc.netty.channel.raknet.config.RakChannelOption;
import org.cloudburstmc.protocol.bedrock.BedrockPeer;
import org.cloudburstmc.protocol.bedrock.BedrockSession;
import org.cloudburstmc.protocol.bedrock.netty.codec.FrameIdCodec;
import org.cloudburstmc.protocol.bedrock.netty.codec.batch.BedrockBatchDecoder;
import org.cloudburstmc.protocol.bedrock.netty.codec.batch.BedrockBatchEncoder;
import org.cloudburstmc.protocol.bedrock.netty.codec.compression.CompressionCodec;
import org.cloudburstmc.protocol.bedrock.netty.codec.compression.LegacyCompressorDetector;
import org.cloudburstmc.protocol.bedrock.netty.codec.compression.ZlibCompressionCodec;
import org.cloudburstmc.protocol.bedrock.netty.codec.packet.BedrockPacketCodec;
import org.cloudburstmc.protocol.bedrock.netty.codec.packet.BedrockPacketCodec_v1;
import org.cloudburstmc.protocol.bedrock.netty.codec.packet.BedrockPacketCodec_v2;
import org.cloudburstmc.protocol.bedrock.netty.codec.packet.BedrockPacketCodec_v3;
import org.cloudburstmc.protocol.common.util.Zlib;

public abstract class BedrockChannelInitializer<T extends BedrockSession> extends ChannelInitializer<Channel> {

    public static final int RAKNET_MINECRAFT_ID = 0xFE;
    private static final FrameIdCodec RAKNET_FRAME_CODEC = new FrameIdCodec(RAKNET_MINECRAFT_ID);
    private static final BedrockBatchDecoder BATCH_DECODER = new BedrockBatchDecoder();

    @Override
    protected final void initChannel(Channel channel) throws Exception {
        this.preInitChannel(channel);

        channel.pipeline()
                .addLast(BedrockBatchDecoder.NAME, BATCH_DECODER)
                .addLast(BedrockBatchEncoder.NAME, new BedrockBatchEncoder());

        this.initPacketCodec(channel);

        channel.pipeline().addLast(BedrockPeer.NAME, this.createPeer(channel));

        this.postInitChannel(channel);
    }

    protected void preInitChannel(Channel channel) throws Exception {
        channel.pipeline().addLast(FrameIdCodec.NAME, RAKNET_FRAME_CODEC);

        int rakVersion = channel.config().getOption(RakChannelOption.RAK_PROTOCOL_VERSION);

        switch (rakVersion) {
            case 7:
            case 9:
                channel.pipeline().addLast(CompressionCodec.NAME, new ZlibCompressionCodec(Zlib.DEFAULT));
                break;
            case 10: // Zlib Raw
                channel.pipeline().addLast(CompressionCodec.NAME, new ZlibCompressionCodec(Zlib.RAW));
                break;
            case 8:
            case 11: // No compression on initial packet request
                channel.pipeline().addLast(LegacyCompressorDetector.NAME, new LegacyCompressorDetector());
                break;
            default:
                throw new UnsupportedOperationException("Unsupported RakNet protocol version: " + rakVersion);
        }
    }

    protected void postInitChannel(Channel channel) throws Exception {
    }

    protected void initPacketCodec(Channel channel) throws Exception {
        int rakVersion = channel.config().getOption(RakChannelOption.RAK_PROTOCOL_VERSION);

        switch (rakVersion) {
            case 11:
            case 10:
            case 9: // Merged & Varint-ified
            case 8: // Split-screen support
                channel.pipeline().addLast(BedrockPacketCodec.NAME, new BedrockPacketCodec_v3());
                break;
            case 7: // Single byte packet ID
                channel.pipeline().addLast(BedrockPacketCodec.NAME, new BedrockPacketCodec_v1());
                break;
            default:
                throw new UnsupportedOperationException("Unsupported RakNet protocol version: " + rakVersion);
        }
    }

    protected BedrockPeer createPeer(Channel channel) {
        return new BedrockPeer(channel, this::createSession);
    }

    protected final T createSession(BedrockPeer peer, int subClientId) {
        T session = this.createSession0(peer, subClientId);
        this.initSession(session);
        return session;
    }

    protected abstract T createSession0(BedrockPeer peer, int subClientId);

    protected abstract void initSession(T session);
}
