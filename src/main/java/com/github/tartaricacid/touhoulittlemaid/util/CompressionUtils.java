package com.github.tartaricacid.touhoulittlemaid.util;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;

import java.io.ByteArrayOutputStream;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class CompressionUtils {
    public static byte[] compress(byte[] rawData) {
        Deflater deflater = new Deflater(Deflater.BEST_COMPRESSION);
        deflater.setInput(rawData);
        deflater.finish();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int compressedSize = deflater.deflate(buffer);
            outputStream.write(buffer, 0, compressedSize);
        }
        deflater.end();

        return outputStream.toByteArray();
    }

    public static byte[] decompress(byte[] compressedData) {
        Inflater inflater = new Inflater();
        inflater.setInput(compressedData);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];

        try {
            while (!inflater.finished()) {
                int decompressedSize = inflater.inflate(buffer);
                outputStream.write(buffer, 0, decompressedSize);
            }
        } catch (DataFormatException e) {
            TouhouLittleMaid.LOGGER.error(e.getMessage());
        } finally {
            inflater.end();
        }

        return outputStream.toByteArray();
    }
}
