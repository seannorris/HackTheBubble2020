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
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;

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
        var size = info.getVideoCodecMeta().getSize();
        
        System.out.println(size.getWidth() + "x" + size.getHeight() + ", " + info.getTotalFrames() + " frames @ " + framerate + "fps");
        var timeBetweenFrames = (info.getTotalDuration() / info.getTotalFrames()) * 1000;
        var skipDelta = 2 * timeBetweenFrames;
        
        var aspectRatio = 2;
        var width = args.length < 2 ? 75 : Integer.parseInt(args[1]);
        var regionWidth = Math.max(size.getWidth() / width, 1);
        var regionHeight = regionWidth * aspectRatio;
        
        var buffer = new Buffer(video, Math.min((int)Math.ceil(framerate * 3), info.getTotalFrames()), regionWidth, regionHeight);
        buffer.fill();
        System.out.print("\u001b[2J");
        
        var skipped = 0;
        double lastFrameTime = -1;
        String frame;
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
            
            System.out.print(frame);
           //Thread.sleep(10);
        }
        System.out.println("Dropped " + skipped + " frames.");
    }
    
    private static class Decoder implements Callable<String>
    {
        private final Picture picture;
        private final int regionWidth;
        private final int regionHeight;
        
        public Decoder(Picture picture, int regionWidth, int regionHeight)
        {
            this.picture = picture;
            this.regionWidth = regionWidth;
            this.regionHeight = regionHeight;
        }
        
        @Override
        public String call()
        {
            return GetSetPixels.toAscii(AWTUtil.toBufferedImage(picture), regionWidth, regionHeight);
        }
    }
    
    private static class Buffer implements Runnable
    {
        private final FrameGrab frameGrab;
        private final String[] buffer;
        private final Thread thread;
        
        private final int regionWidth;
        private final int regionHeight;
        
        private final Object sync1 = new Object();
        
        private int head = 0;
        private int tail = 0;
        private Integer size = 0;
        
        public Buffer(File video, int bufferSize, int regionWidth, int regionHeight) throws IOException, JCodecException
        {
            var channel = NIOUtils.readableChannel(video);
            frameGrab = FrameGrab.createFrameGrab(channel);
            buffer = new String[bufferSize];
            thread = new Thread(this);
            thread.start();
            this.regionWidth = regionWidth;
            this.regionHeight = regionHeight;
        }
    
        public Thread getThread()
        {
            return thread;
        }
    
        @Override
        public void run()
        {
            var decoder = Executors.newFixedThreadPool(32);
            var decodeQueue = new ConcurrentLinkedQueue<Future<String>>();
            try
            {
                Picture frame;
                while((frame = frameGrab.getNativeFrame()) != null)
                {
                    decodeQueue.add(decoder.submit(new Decoder(frame, regionWidth, regionHeight)));
                    while(!decodeQueue.isEmpty() && decodeQueue.peek().isDone())
                        addToArray(decodeQueue.remove().get());
                }
                while(!decodeQueue.isEmpty())
                {
                    addToArray(decodeQueue.remove().get());
                }
                addToArray(null);
            }
            catch(IOException | InterruptedException | ExecutionException e)
            {
                throw new RuntimeException(e);
            }
            decoder.shutdownNow();
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
        
        private void addToArray(String input) throws InterruptedException
        {
            synchronized(sync1)
            {
                while(size == buffer.length)
                {
                    synchronized(fullSync)
                    {
                        fullSync.notify();
                    }
                    sync1.wait();
                }
            }
            
            buffer[head] = input;
            head = (head + 1) % buffer.length;
            size++;
        }
        
        public String nextFrame() throws InterruptedException
        {
            if(size <= 0)
                fill();
            
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
