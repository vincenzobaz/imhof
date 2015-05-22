package ch.epfl.imhof;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class BufferedMap {
    private BufferedImage map;
    
    public BufferedMap(BufferedImage map){
        this.map = map;
    }
    
    public void printOnFile(String extension, String path ) throws IOException{
        ImageIO.write(map, extension, new File(path));
    }
    
    protected BufferedImage image(){
        return map;
    }
    
    protected void setImage(BufferedImage picture){
        map = picture;
    }

}
