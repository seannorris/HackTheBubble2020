import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.model.Picture;
import org.jcodec.containers.mp4.demuxer.MP4Demuxer;
import org.jcodec.scale.AWTUtil;
import swing.MainWindow;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class AsciiConverter
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
        var demuxer = MP4Demuxer.createMP4Demuxer(channel);
        var track = demuxer.getVideoTrack();
        var info = track.getMeta();
        var framerate = info.getTotalFrames() / info.getTotalDuration();
        var buffer = new Buffer(video, Math.min((int)Math.ceil(framerate * 10), info.getTotalFrames()));
        var size = info.getVideoCodecMeta().getSize();
        info.getVideoCodecMeta().getPixelAspectRatio();
        
        System.out.println(size.getWidth() + "x" + size.getHeight() + ", " + info.getTotalFrames() + " frames @ " + framerate + "fps");
        var timeBetweenFrames = (info.getTotalDuration() / info.getTotalFrames()) * 1000;
        var skipDelta = 2 * timeBetweenFrames;
        
        var aspectRatio = 2;
        var width = args.length < 2 ? 50 : Integer.parseInt(args[1]);
        var regionWidth = Math.max(size.getWidth() / width, 1);
        var regionHeight = regionWidth * aspectRatio;
        
        var window = new MainWindow(size.getWidth() / regionWidth, size.getHeight() / regionHeight);
        
        buffer.fill();
        
        var skipped = 0;
        double lastFrameTime = -1;
        BufferedImage frame;
        while((frame = buffer.nextFrame()) != null)
        {
            if(lastFrameTime != -1)
            {
                if(System.currentTimeMillis() - lastFrameTime < timeBetweenFrames)
                    Thread.sleep(Math.max(System.currentTimeMillis() + (long)(timeBetweenFrames - lastFrameTime), 0));
                else if(System.currentTimeMillis() - lastFrameTime > skipDelta)
                {
                    lastFrameTime += timeBetweenFrames;
                    skipped++;
                    continue;
                }
                lastFrameTime += timeBetweenFrames;
            }
            else
                lastFrameTime = System.currentTimeMillis();
    
           GetSetPixels.toAscii(frame, regionWidth, regionHeight, window.getGrid());
        }
        System.out.println("Dropped " + skipped + " frames.");
    }
    
    private static class Buffer implements Runnable
    {
        private final FrameGrab frameGrab;
        private final BufferedImage[] buffer;
        private final Thread thread;
        
        private final Object sync1 = new Object();
        private final Object sync2 = new Object();
        
        private int head = 0;
        private int tail = 0;
        private Integer size = 0;
        
        public Buffer(File video, int bufferSize) throws IOException, JCodecException
        {
            var channel = NIOUtils.readableChannel(video);
            frameGrab = FrameGrab.createFrameGrab(channel);
            buffer = new BufferedImage[bufferSize];
            thread = new Thread(this);
            thread.start();
        }
    
        public Thread getThread()
        {
            return thread;
        }
    
        @Override
        public void run()
        {
            try
            {
                Picture frame;
                while((frame = frameGrab.getNativeFrame()) != null)
                    addToArray(AWTUtil.toBufferedImage(frame));
                
                addToArray(null);
            }
            catch(IOException | InterruptedException e)
            {
                throw new RuntimeException(e);
            }
        }
        
        public final Object fullSync = new Object();
        public void fill() throws InterruptedException
        {
            synchronized(fullSync)
            {
                while(size < buffer.length)
                    fullSync.wait();
            }
        }
        
        private void addToArray(BufferedImage image) throws InterruptedException
        {
            synchronized(sync1)
            {
                while(size == buffer.length)
                {
                    synchronized(fullSync)
                    {
                        fullSync.notify();
                    }
                    System.gc();
                    sync1.wait();
                }
            }
            
            buffer[head] = image;
            head = (head + 1) % buffer.length;
            size++;
            synchronized(sync2)
            {
                sync2.notify();
            }
        }
        
        public BufferedImage nextFrame() throws InterruptedException
        {
            synchronized(sync2)
            {
                while(size <= 0)
                    sync2.wait();
            }
            
            var out = buffer[tail];
            buffer[tail] = null;
            tail = (tail + 1) % buffer.length;
            size--;
            synchronized(sync1)
            {
                sync1.notify();
            }
            return out;
        }
    }
}
