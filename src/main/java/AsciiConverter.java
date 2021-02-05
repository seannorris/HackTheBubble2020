import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.codecs.aac.AACDecoder;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.logging.LogLevel;
import org.jcodec.common.logging.LogSink;
import org.jcodec.common.logging.Logger;
import org.jcodec.common.logging.Message;
import org.jcodec.common.model.Packet;
import org.jcodec.common.model.Picture;
import org.jcodec.containers.mp4.demuxer.MP4Demuxer;
import org.jcodec.scale.AWTUtil;
import swing.MainWindow;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
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
    private static SourceDataLine line;
    private static final Object soundSync = new Object();
    private static boolean audioWait = true;
    private static int height;
    
    public static void main(String[] args) throws IOException, JCodecException, InterruptedException, LineUnavailableException
    {
        if(args.length < 1)
        {
            System.out.println("Argument missing: path to video file.");
            return;
        }
        var video = Paths.get(args[0]).toFile();
        
        /*
        var audioInputStream = AudioSystem.(video);
        var clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        clip.start();
        
         */
        
        var channel = NIOUtils.readableChannel(video);
        var demuxer = MP4Demuxer.createMP4Demuxer(channel);
        var track = demuxer.getVideoTrack();
        var info = track.getMeta();
        var framerate = info.getTotalFrames() / info.getTotalDuration();
        var size = info.getVideoCodecMeta().getSize();
        
        var audioTrack = demuxer.getAudioTracks().get(0);
        var audioMeta = audioTrack.getMeta().getAudioCodecMeta();
    
        final Packet[] prevAudioFrame = {audioTrack.nextFrame()};
        var aacDecoder = new AACDecoder(prevAudioFrame[0].getData());
        var audioDecodedMeta = aacDecoder.getCodecMeta(prevAudioFrame[0].getData());
        var audioFormat = new AudioFormat(audioDecodedMeta.getSampleRate(), audioDecodedMeta.getSampleSize() * 8, audioDecodedMeta.getChannelCount(), true,  false);
        var audioInfo = new DataLine.Info(SourceDataLine.class, audioFormat);
        line = (SourceDataLine)AudioSystem.getLine(audioInfo);
        line.open();
        
    
        var byteBuffer = ByteBuffer.allocate(audioMeta.getBytesPerFrame());
        new Thread(() ->
        {
            try
            {
                do
                {
                    var audioBuffer = aacDecoder.decodeFrame(prevAudioFrame[0].getData(), byteBuffer).getData().array();
                    line.write(audioBuffer, 0, audioBuffer.length);
                    if(audioWait)
                    {
                        audioWait = false;
                        synchronized(soundSync)
                        {
                            soundSync.wait();
                        }
                    }
                }
                while((prevAudioFrame[0] = audioTrack.nextFrame()) != null);
                line.drain();
                line.stop();
                line.close();
            }
            catch(IOException | InterruptedException e)
            {
                throw new RuntimeException(e);
            }
        }).start();
        
        
        System.out.printf("%dx%d, %d frames @ %sfps - Audio: %d bit @ %dhz%n", size.getWidth(), size.getHeight(), info.getTotalFrames(), framerate, audioMeta.getSampleSize() * 8, audioMeta.getSampleRate());
        var timeBetweenFrames = (info.getTotalDuration() / info.getTotalFrames()) * 1000;
        var skipDelta = 2 * timeBetweenFrames;
        
        var aspectRatio = 2;
        var width = args.length < 2 ? 80 : Integer.parseInt(args[1]);
        var regionWidth = Math.max(size.getWidth() / width, 1);
        var regionHeight = regionWidth * aspectRatio;
        
        var buffer = new Buffer(video, Math.min((int)Math.ceil(framerate * 2), info.getTotalFrames()), regionWidth, regionHeight);
        buffer.fill();
        
        height = size.getHeight() / regionHeight;
        
        //System.out.print("\u001b[2J");
        for(var height = AsciiConverter.height; height > 0; height--)
            System.out.println();
    
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
            {
                startSound();
                lastFrameTime = System.currentTimeMillis();
            }
            
            System.out.print(frame);
           //Thread.sleep(10);
        }
        System.out.println("Dropped " + skipped + " frames.");
    }
    
    private static void startSound()
    {
        line.start();
        synchronized(soundSync)
        {
            soundSync.notify();
        }
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
        private String[] buffer;
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
            var decoder = Executors.newCachedThreadPool();
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
        
        private boolean buffering = false;
        public String nextFrame()
        {
            if(buffering || size <= 0)
            {
                if(!buffering)
                {
                    buffering = true;
                    line.stop();
                    audioWait = true;
                    var oldBuffer = buffer;
                    buffer = new String[buffer.length * 2];
                    for(int x = 0, size = this.size; x < size && buffer[x] == null; x++)
                        buffer[x] = oldBuffer[x];
                }
                if(size < buffer.length)
                {
    
                    return "\u001b[" + height + "A\r" +
                            "buffering..." +
                            String.valueOf(System.lineSeparator()).repeat(Math.max(0, AsciiConverter.height));
                }
                
                buffering = false;
                startSound();
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
