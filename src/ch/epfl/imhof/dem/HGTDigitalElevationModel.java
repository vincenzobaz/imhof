package ch.epfl.imhof.dem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class HGTDigitalElevationModel {
    private final MappedByteBuffer model;

    public HGTDigitalElevationModel(File model) throws FileNotFoundException {
        FileChannel channel = new FileInputStream(model).getChannel();

    }

}