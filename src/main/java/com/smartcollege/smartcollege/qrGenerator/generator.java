package com.smartcollege.smartcollege.qrGenerator;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import static com.google.zxing.client.j2se.MatrixToImageWriter.writeToFile;


public class generator {
    public static void generateQRCode(String data, String path, String charset, Map map, int h, int w) throws WriterException, IOException {
        BitMatrix matrix = new MultiFormatWriter().encode(new String(data.getBytes(charset), charset), BarcodeFormat.QR_CODE, w, h);

        BufferedImage qrImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                qrImage.setRGB(x, y, matrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }

        File outputFile = new File(path);
        String imageFormat = path.substring(path.lastIndexOf('.') + 1);
        ImageIO.write(qrImage, imageFormat, outputFile);
    }
}