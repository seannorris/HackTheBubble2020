import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.model.Picture;
import org.jcodec.containers.mp4.demuxer.MP4Demuxer;
import org.jcodec.scale.AWTUtil;

import javax.swing.JFrame;
import java.io.IOException;
import java.nio.file.Paths;

public class HelloWorld
{
    public static void main(String[] args) throws IOException, JCodecException, InterruptedException
    {
        if(args.length < 1)
        {
            System.out.println("Argument missing: path to video file.");
        }
        var video = Paths.get(args[0]).toFile();
        
        var channel = NIOUtils.readableChannel(video);
        var frameGrab = FrameGrab.createFrameGrab(channel);
        var demuxer = MP4Demuxer.createMP4Demuxer(channel);
        var track = demuxer.getVideoTrack();
        var info = track.getMeta();
        var size = info.getVideoCodecMeta().getSize();
        
        var window = new JFrame(video.getName());
        
        System.out.println(size.getWidth() + "x" + size.getHeight() + ", " + info.getTotalFrames() + " frames @ " + info.getTotalFrames() / info.getTotalDuration() + "fps");
        var timeBetweenFrames = (info.getTotalDuration() / info.getTotalFrames()) * 1000;
        var skipDelta = 2 * timeBetweenFrames;
        
        var skipped = -1;
        Picture frame;
        long lastFrameTime = -1;
        while((frame = frameGrab.getNativeFrame()) != null)
        {
            var image = AWTUtil.toBufferedImage(frame);
            if(lastFrameTime != -1 && System.currentTimeMillis() - lastFrameTime < timeBetweenFrames)
            {
                Thread.sleep(Math.max(System.currentTimeMillis() + (long)timeBetweenFrames - lastFrameTime, 0));
                lastFrameTime += timeBetweenFrames;
            }
            else
            {
                lastFrameTime = System.currentTimeMillis();
                skipped++;
            }
    
            System.out.println(GetSetPixels.toAscii(image));
            System.out.println();
        }
        System.out.println(skipped);
    }
}
