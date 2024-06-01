package stocks.ui.components.panels;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import stocks.model.StockConstants;

public class ConCallNotesPanel extends JPanel{
    public static final ConCallNotesPanel INSTANCE = new ConCallNotesPanel();

    private ConCallNotesPanel(){


    }

    private String getImageFileLocation(String baseFileLocation,String fileName){
        return baseFileLocation + "/img/"+fileName.substring(0,fileName.lastIndexOf(".pdf")) + ".jpg";
    }
    public void renderReport (String baseFileLocation, String fileName){
        File imageFile = new File(getImageFileLocation(baseFileLocation,fileName));
        if(!imageFile.exists()){
            savePdfToImage(baseFileLocation,fileName);
        }
        String imageFileLocation = getImageFileLocation(baseFileLocation, fileName);
        JFrame f = new JFrame("Add an image to JFrame");
        ImageIcon icon = new ImageIcon(imageFileLocation);
        f.add(new JLabel(icon));
        f.pack();
        f.setVisible(true);
    }
    private String savePdfToImage(String baseFileLocation,String fileName){
        PDDocument document = null;
        try {
            document = Loader.loadPDF(new File(baseFileLocation + "/pdf/" + fileName  ));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //Instantiating the PDFRenderer class
        PDFRenderer renderer = new PDFRenderer(document);

        //Rendering an image from the PDF document
        BufferedImage image = null;
        try {
            image = renderer.renderImage(0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String imageFile = getImageFileLocation(baseFileLocation,fileName);
        //Writing the image to a file
        try {
            ImageIO.write(image, "JPEG", new File(imageFile));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            document.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return imageFile;
    }
    public static  void main(String args[]){

    //Loading an existing PDF document
        PDDocument document = null;
        try {
            document = Loader.loadPDF(new File("/Users/tusharmohanty/Analysis/AartiPharmaLabs/Q4ConCall.pdf"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //Instantiating the PDFRenderer class
    PDFRenderer renderer = new PDFRenderer(document);

    //Rendering an image from the PDF document
        BufferedImage image = null;
        try {
            image = renderer.renderImage(0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //Writing the image to a file
        try {
            ImageIO.write(image, "JPEG", new File("/Users/tusharmohanty/Analysis/AartiPharmaLabs/myimage.jpg"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Image created");

        JFrame f = new JFrame("Add an image to JFrame");
        ImageIcon icon = new ImageIcon("/Users/tusharmohanty/Analysis/AartiPharmaLabs/myimage.jpg");
        f.add(new JLabel(icon));
        f.pack();
        f.setVisible(true);

    //Closing the document
        try {
            document.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
