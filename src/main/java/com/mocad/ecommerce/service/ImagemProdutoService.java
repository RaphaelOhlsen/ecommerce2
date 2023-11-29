package com.mocad.ecommerce.service;

import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class ImagemProdutoService {

    public String miniaturaImagem(String image) throws IOException {
        String base64Image  = "";
        String miniImgBase64 = "";

        if (image.contains("data:image")) {
            base64Image = image.split(",")[1];
        } else {
            base64Image = image;
        }

        byte[] imageBytes = DatatypeConverter.parseBase64Binary(base64Image);

        BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imageBytes));

        if (bufferedImage != null) {
            int type = bufferedImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : bufferedImage.getType();
            int largura = Integer.parseInt("800");
            int altura = Integer.parseInt("600");

            BufferedImage resizedImage = new BufferedImage(largura, altura, type);
            Graphics2D g = resizedImage.createGraphics();
            g.drawImage(bufferedImage, 0, 0, largura, altura, null);
            g.dispose();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(resizedImage, "png", baos);

            miniImgBase64 = "data:image/png;base64," + DatatypeConverter.printBase64Binary(baos.toByteArray());

            bufferedImage.flush();
            resizedImage.flush();
            baos.flush();
            baos.close();
        }

        return miniImgBase64;
    }
}
