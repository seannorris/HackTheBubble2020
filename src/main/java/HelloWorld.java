import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.common.io.NIOUtils;

import java.io.IOException;
import java.nio.file.Paths;

public class HelloWorld
{
    
    public static void main(String[] args) throws IOException, JCodecException
    {
        var frameGrab = FrameGrab.createFrameGrab(NIOUtils.readableChannel(Paths.get(args[0]).toFile()));
        var info = frameGrab.getMediaInfo();
        System.out.println(info.getDim().getHeight() + ", " + info.getDim().getWidth());
    }
}
