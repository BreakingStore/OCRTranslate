package me.niotgg.captureform;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class CaptureFrame extends JFrame {

    public BufferedImage firstScreen;
    public BufferedImage screen;
    public BufferedImage screenCopy;
    public JPanel jPanel;
    public Rectangle captureRect;


    public CaptureFrame(BufferedImage bufferedImage) {

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);



        firstScreen = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), bufferedImage.getType());

        Graphics g = firstScreen.getGraphics();
        g.drawImage(bufferedImage, 0, 0, null);
        g.dispose();

        screen = escalaDeCinza(bufferedImage);


        screenCopy = new BufferedImage(screen.getWidth(),
                screen.getHeight(), screen.getType());


        jPanel = new ScreenPanel(this);

        setAlwaysOnTop(true);
        add(jPanel);
        setSize(screen.getWidth(),screen.getHeight());


        setUndecorated(true);
        setBackground(new Color(0,255,0,0));

        setVisible(true);

        jPanel.addMouseListener(new MouseTrackClick(this));
        addKeyListener(new KeyTrack(this));
        jPanel.addMouseMotionListener(new MouseTrackMoviment(this));


    }

    public void repaint(BufferedImage orig, BufferedImage copy) {


        Graphics2D g = copy.createGraphics();
        g.drawImage(orig, 0, 0, null);
        g.setColor(Color.RED);
        if (captureRect == null) {
            return;
        }

        if (captureRect.height != 0 && captureRect.width != 0) {
            java.awt.Image image = firstScreen;
            java.awt.image.FilteredImageSource fis = new java.awt.image.FilteredImageSource(
                    image.getSource(), new java.awt.image.CropImageFilter(Math.round(captureRect.x), Math.round(captureRect.y), Math.round(captureRect.width), Math.round(captureRect.height))
            );
            image = null;
            image = java.awt.Toolkit.getDefaultToolkit().createImage(fis);


            BufferedImage bi = new BufferedImage
                    (image.getWidth(null),image.getHeight(null),BufferedImage.TYPE_INT_RGB);
            Graphics bg = bi.getGraphics();
            bg.drawImage(image, 0, 0, null);
            bg.dispose();

            g.drawImage(bi, captureRect.x, captureRect.y, null);
        }

        g.draw(captureRect);
        g.setColor(new Color(25, 25, 23, 10));
        g.fill(captureRect);
        g.dispose();
    }

    private BufferedImage escalaDeCinza(BufferedImage imagem) {
        //pegar largura e altura da imagem
        int width = imagem.getWidth();
        int height = imagem.getHeight();

        int media = 0;
        //laço para varrer a matriz de pixels da imagem
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {               //rgb recebe o valor RGB do pixel em questão
                int rgb = imagem.getRGB(i, j);
                int r = (int)((rgb&0x00FF0000)>>>16); //R
                int g = (int)((rgb&0x0000FF00)>>>8);  //G
                int b = (int) (rgb&0x000000FF);       //B

                //media dos valores do RGB
                //será o valor do pixel na nova imagem
                media = (r + g + b) / 3;

                //criar uma instância de Color
                Color color = new Color(media, media, media);
                //setar o valor do pixel com a nova cor
                imagem.setRGB(i, j, color.getRGB());
            }
        }
        return imagem;
    }


}
