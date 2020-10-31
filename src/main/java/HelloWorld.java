import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.model.Picture;
import org.jcodec.containers.mp4.demuxer.MP4Demuxer;
import org.jcodec.scale.AWTUtil;

import java.io.IOException;
import java.nio.file.Paths;

public class HelloWorld
{
    
    public static void main(String[] args) throws IOException, JCodecException, InterruptedException
    {
        if(args.length < 1)
        {
            System.out.println("Argument missing: path to video file.");
            return;
        }
        var video = Paths.get(args[0]).toFile();
        
        var channel = NIOUtils.readableChannel(video);
        var frameGrab = FrameGrab.createFrameGrab(channel);
        var demuxer = MP4Demuxer.createMP4Demuxer(channel);
        var track = demuxer.getVideoTrack();
        var info = track.getMeta();
        var size = info.getVideoCodecMeta().getSize();
        info.getVideoCodecMeta().getPixelAspectRatio();
        
        System.out.println(size.getWidth() + "x" + size.getHeight() + ", " + info.getTotalFrames() + " frames @ " + info.getTotalFrames() / info.getTotalDuration() + "fps");
        var timeBetweenFrames = (info.getTotalDuration() / info.getTotalFrames()) * 1000;
        var skipDelta = 2 * timeBetweenFrames;
        
        var aspectRatio = 2;
        var width = args.length < 2 ? 160 : Integer.parseInt(args[1]);
        var regionWidth = Math.max(size.getWidth() / width, 1);
        var regionHeight = regionWidth * aspectRatio;
        
        var skipped = 0;
        long lastFrameTime = -1;
        Picture frame;
        while((frame = frameGrab.getNativeFrame()) != null)
        {
            var image = AWTUtil.toBufferedImage(frame);
            if(lastFrameTime != -1)
            {
                if(System.currentTimeMillis() - lastFrameTime < timeBetweenFrames)
                {
                    Thread.sleep(Math.max(System.currentTimeMillis() + (long)timeBetweenFrames - lastFrameTime, 0));
                }
                else if(System.currentTimeMillis() - lastFrameTime > skipDelta)
                {
                    lastFrameTime += timeBetweenFrames;
                    skipped++;
                    continue;
                }
                lastFrameTime += timeBetweenFrames;
            }
            else
            {
                lastFrameTime = System.currentTimeMillis();
            }
    
            System.out.println(GetSetPixels.toAscii(image, regionWidth, regionHeight));
        }
        System.out.println("Skipped " + skipped + " frames.");
    }
}
