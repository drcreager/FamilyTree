package com.ko.na;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

/**
 * This class demonstrates how to load an Image from an external file
 */
public class Image extends Component implements ImageObserver {
	protected BufferedImage image;
	protected String fileType;
	
	protected String uri;
	protected String desc;

	protected Font  font;
	protected Color LowLight;
	protected Color HighLight;
	
	protected boolean renderingDone;
	protected boolean renderingError;
	
	protected int maxHeight;
	protected int maxWidth;

	private static final int DEF_HEIGHT = 382;
	private static final int DEF_WIDTH = 594;
	private static final long serialVersionUID = -2845161013377965953L;
	
	public Image() throws Exception {
		this(null, DEF_WIDTH, DEF_HEIGHT, null);
	} // null argument constructor

	public Image(String uri, int mWidth, int mHeight, String desc) throws Exception {
		font = new Font("Times New Roman", Font.BOLD, 16);
		LowLight = Color.getColor("",0x4e5944);
		HighLight = Color.LIGHT_GRAY;

		setUri(uri);
		setFileType(uri);
		setDesc(desc);
		setMaxWidth(mWidth);
		setMaxHeight(mHeight);

		image = loadFromURL(uri);
		image = caption(resize(image, getPreferredSize(image)), getDesc(), font, calcPixel(image));

	} // end constructor

	public Image(String uri, String desc) throws Exception {
		this(uri, DEF_WIDTH, DEF_HEIGHT, desc);
	}

	protected String block(String img) {
		StringBuffer buf = new StringBuffer();
		int linesize = 80;
		int i = 0;

		for (; i < img.length() - linesize; i += linesize) {
			buf.append(img.substring(i, i + linesize) + "\n");
		} // end for
		buf.append(img.substring(i));
		return buf.toString();
	}
	
	protected Pixel calcPixel(BufferedImage image){
		return calcPixel(image, 0, 0, image.getWidth(), image.getHeight());
	}

	protected Pixel calcPixel(BufferedImage image, int x, int y, int w, int h) {
		int redTotal = 0, greenTotal = 0, blueTotal = 0;

		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				int rgb = image.getRGB(x + i, y + j);
				redTotal   += Pixel.getRed(rgb);
				greenTotal += Pixel.getGreen(rgb);
				blueTotal  += Pixel.getBlue(rgb);
			} // end for 
		} // end for 
		
		int count = w * h;
		return new Pixel(redTotal / count, greenTotal / count, blueTotal / count);
	} // end calcPixel() method

	/*
	 * Inscribe a caption on the provided image
	 */
	protected BufferedImage caption(BufferedImage image, String caption, Font font, Pixel pixel) {
		/*
		 * Create a graphics context and a caption font.
		 */
		Graphics2D g = image.createGraphics();
		g.setFont(font);

		/*
		 * Center the caption
		 */
		Dimension dOrig = new Dimension(image.getWidth(), image.getHeight());
		Dimension dString = getStrDimensions(font, caption);
		Dimension dCentered = center(dOrig, dString);

		/*
		 * Inscribe the caption on the image
		 */
		g.setColor(Color.getColor("",pixel.getValue()));
		g.setColor(LowLight);
		g.fillRoundRect(dCentered.width - 6, dOrig.height - dString.height -
		                15, dString.width + 15, dString.height + 10, 15, 15);

		g.setXORMode(HighLight);
		g.drawString(caption, dCentered.width, dOrig.height - dString.height+3);

		// Clean up -- dispose the graphics context that was created.
		g.dispose();
		return image;
	}

	protected Dimension center(Dimension frame, Dimension item) {
		return new Dimension((frame.width - item.width) / 2, (frame.height - item.height) / 2);
	} // end center() method

	protected BufferedImage decode(String encodedImage) throws Exception {
		byte[] bytes = Base64.getDecoder().decode(encodedImage);
		return ImageIO.read(new ByteArrayInputStream(bytes));
	}

	public void display() {
		JFrame f = new JFrame(getDesc());
		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		f.add(this);
		f.pack();
		f.setVisible(true);
	}

	protected String encode(BufferedImage image, String fileType) throws Exception {
		String encodedImage = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(image, fileType, baos);

		baos.flush();
		encodedImage = Base64.getEncoder().encodeToString(baos.toByteArray());
		baos.close();

		return encodedImage;
	}

	public String getDesc() {
		return desc;
	} // end getDesc() method

	public String getEncodedImage() throws Exception {
		return block(encode(getImage(), getFileType()));
	}

	public String getFileType() {
		return fileType;
	}

	public BufferedImage getImage() {
		return image;
	}

	public int getMaxHeight() {
		return maxHeight;
	}

	public int getMaxWidth() {
		return maxWidth;
	}

	public Dimension getPreferredSize() {
		return getPreferredSize(getImage());
	}

	public Dimension getPreferredSize(BufferedImage image) {
		Dimension dim = new Dimension();
		if (image == null) {
			return new Dimension(100, 100);
		} else {
			double sfact = getScalingFactor(image);
			dim.setSize(image.getWidth(null) / sfact, image.getHeight(null) / sfact);
			return dim;
		} // end if/else
	} // end getPreferredSize() method

	/*
	 * Establish Scaling factor to fit this image into the frame.
	 */
	public double getScalingFactor(BufferedImage image) {
		double[] scalingFactor = { (double) image.getHeight(null) / maxHeight,
				(double) image.getWidth(null) / maxWidth };
		return (scalingFactor[0] > scalingFactor[1]) ? scalingFactor[0] : scalingFactor[1];
	} // end getScalingFactor() method

	protected Dimension getStrDimensions(Font font, String text) {
		FontRenderContext frc = new FontRenderContext(new AffineTransform(), true, true);
		Dimension dim = new Dimension((int) (font.getStringBounds(text, frc).getWidth()),
				(int) (font.getStringBounds(text, frc).getHeight()));
		return dim;
	} // end getStrDimensions() method

	public String getUri() {
		return uri;
	}

	@Override
	public boolean imageUpdate(java.awt.Image img, int infoflags, int x, int y, int width, int height) {
		setRenderingError((0 != (infoflags & ImageObserver.ERROR)));
		if (!isRenderingError())
			setRenderingError((0 != (infoflags & ImageObserver.ABORT)));

		if (isRenderingError()) {
			setRenderingDone(true);
		} else {
			setRenderingDone((0 != (infoflags & ImageObserver.ALLBITS)));
		} // end if

		return (!isRenderingDone());
	}// end imageUpdate() method

	public boolean isRenderingDone() {
		return renderingDone;
	}

	public boolean isRenderingError() {
		return renderingError;
	}

	public BufferedImage loadFromFile(String fileName) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File(fileName));

		} catch (Exception ex1) {
			ex1.printStackTrace();
		} // end try/catch
		return image;
	} // end loadFromFile() method

	public BufferedImage loadFromURL(String uri) throws MalformedURLException {
		BufferedImage image = null;
		URL url = new URL(uri);
		try {
			image = ImageIO.read(url);

		} catch (Exception ex1) {
			ex1.printStackTrace();
		} // end try/catch
		return image;
	} // end loadFromUrl() method

	public void paint(Graphics g) {
		g.drawImage(image, 0, 0, null);
	}

	/*
	 * Resize the image
	 */
	protected BufferedImage resize(BufferedImage image, Dimension newDim) throws InterruptedException {
		return toBufferedImage(image.getScaledInstance(newDim.width, newDim.height, BufferedImage.SCALE_REPLICATE));
	} // end resize() method

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public void setFileType(String uri) {
		if (uri != null) {
			fileType = uri.substring(uri.lastIndexOf('.') + 1);
		} // end if
	} // end setFileType() method

	public void setImage(BufferedImage image) {
		this.image = image;
	}

	public void setMaxHeight(int maxHeight) {
		this.maxHeight = maxHeight;
	}

	public void setMaxWidth(int maxWidth) {
		this.maxWidth = maxWidth;
	}

	public void setRenderingDone(boolean renderingDone) {
		this.renderingDone = renderingDone;
	}

	public void setRenderingError(boolean renderingError) {
		this.renderingError = renderingError;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	/**
	 * Converts a given Image into a BufferedImage
	 * 
	 * @param image   The Image to be converted
	 * @return The converted BufferedImage
	 * @throws InterruptedException  xxxxxxxxx
	 */
	public BufferedImage toBufferedImage(java.awt.Image image) throws InterruptedException {

		// Create a buffered image with transparency
		BufferedImage result = new BufferedImage(image.getWidth(null), 
				                                 image.getHeight(null),
				                                 (getFileType().startsWith("j")) // JPEG has NO Alpha channel
				                                    ? BufferedImage.TYPE_INT_RGB
				                                    : BufferedImage.TYPE_INT_ARGB);
		
		// Draw the image on to the buffered image
		Graphics2D bGr = result.createGraphics();
		boolean ready = bGr.drawImage(image, 0, 0, this);
		if (!ready) {
			while (!isRenderingDone()) 
				Thread.sleep(5000L);
		} // end if 
		bGr.dispose();

		// Return the buffered image
		return result;
	}

	public String toEmbeddedHTML() throws Exception {
		return "<img src=\"data:image/" + getFileType() + ";base64," + getEncodedImage() + "\" alt=\"" + getDesc()
				+ "\" width=\"" + image.getWidth() + "\" height=\"" + image.getHeight() + "\"/>";
	}


	public String toHTML() {
		return "<img src=\"" + this.getUri() + "\" alt=\"" + getDesc() + "\" width=\"" + image.getWidth() + "\" height=\""
				+ image.getHeight() + "\"/>";
	}
	
	public String toString() {
		return image.toString();
	}

	public void writeImage(String fileName) {
		try {
			File outputfile = new File(fileName);
			outputfile.setWritable(true);
			ImageIO.write(getImage(), getFileType(), outputfile);

		} catch (IOException ex1) {
			ex1.printStackTrace();
		}
	}
	public static class Pixel{
		public int r,g,b;
	
		public Pixel(int r, int g, int b) {
			this.r = r;
			this.g = g;
			this.b = b;
		}
		
		public static int getBlue(int pixel){
			return  pixel & 0xff;
		}

		public static int getGreen(int pixel){
			return  (pixel >>> 8) & 0xff;
		}
		
		public static int getRed(int pixel){
			return  (pixel >>> 16) & 0xff;
		}
		
		public int getBlue(){
			return  this.b;
		}

		public int getGreen(){
			return  this.g;
		}
		
		public int getRed(){
			return  this.r;
		}

		public void setBlue(int blue){
			 this.b = blue & 0xff;
		}

		public void setGreen(int green){
			this.g = (green & 0xff) << 8;
		}
		
		public void setRed(int red){
			this.r = (red & 0xff) << 16;
		}
		
		public int getValue(){
			return ((this.r << 16) & (this.g << 8) & (this.b));
		}

		@Override
		public String toString() {
			return r + "." + g + "." + b;
		}
	} // end Pixel class
	public static void main(String[] args) throws Exception {
		Image image = new Image(args[0], args[1]);
		image.display();
		System.out.println(image.toHTML());
	} // end main() method

} // end class
