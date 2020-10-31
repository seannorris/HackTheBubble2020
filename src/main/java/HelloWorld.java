import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.containers.mp4.demuxer.MP4Demuxer;
import org.jcodec.scale.AWTUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;

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
        
        for(var x = 0; x < info.getTotalFrames(); x++)
        {
            var frame = frameGrab.getNativeFrame();
            var bufferedImage = AWTUtil.toBufferedImage(frame);
            
            GetSetPixels.drawFrame(bufferedImage);
            System.out.println();
        }
    }
}
