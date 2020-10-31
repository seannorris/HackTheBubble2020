import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.containers.mp4.demuxer.MP4Demuxer;

import java.io.IOException;
import java.nio.file.Paths;

public class HelloWorld
{
    
    public static void main(String[] args) throws IOException, JCodecException
    {
        var channel = NIOUtils.readableChannel(Paths.get(args[0]).toFile());
        var frameGrab = FrameGrab.createFrameGrab(channel);
        var demuxer = MP4Demuxer.createMP4Demuxer(channel);
        var track = demuxer.getVideoTrack();
        var info = track.getMeta();
        var size = info.getVideoCodecMeta().getSize();
        System.out.println(size.getWidth() + "x" + size.getHeight() + ", " + info.getTotalFrames() + " frames @ " + info.getTotalFrames() / info.getTotalDuration() + "fps");
    }
}
