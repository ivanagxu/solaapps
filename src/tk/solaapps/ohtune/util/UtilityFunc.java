package tk.solaapps.ohtune.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import javaQuery.j2ee.ImageResize;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import tk.solaapps.ohtune.model.Product;
import tk.solaapps.ohtune.model.ProductionLog;
import tk.solaapps.ohtune.pattern.JsonJob;
import tk.solaapps.ohtune.pattern.JsonOrder;
import tk.solaapps.ohtune.pattern.JsonProduct;
import tk.solaapps.ohtune.service.IOhtuneService;

public class UtilityFunc {
	public static void compressImage(int toWidth, int toHeight, String filePath) throws IOException
	{
		int height = 0;
		int width = 0;
		ImageInputStream in = ImageIO.createImageInputStream(new FileInputStream(filePath));
		try {
			final Iterator<ImageReader> readers = ImageIO.getImageReaders(in);
			if (readers.hasNext()) {
				ImageReader reader = (ImageReader) readers.next();
				try {
					reader.setInput(in);
					width = reader.getWidth(0);
					height = reader.getHeight(0);
				} finally {
					reader.dispose();
				}
			}
		} finally {
			if (in != null)
				in.close();
		}
		ImageResize ir = new ImageResize();
		if(width / height > 1f)
		{
			if(width > 1024)
			{
				ir.compressImage(filePath, toWidth , height * toWidth / width);
			}
		}
		else
		{
			if(height > 768)
			{
				ir.compressImage(filePath, width * toHeight / height , toHeight);
			}
		}
	}
	public static void fillImageDrawingForOrder(List<JsonOrder> orders, IOhtuneService service)
	{
		Product product = null;
		File file;
		for(int i = 0; i < orders.size(); i++)
		{
			product  = service.getProductByName(orders.get(i).product_name);
			if(product == null)
			{
				orders.get(i).image = "";
				orders.get(i).drawing = "";
			}
			else
			{
				 file = new File(product.getImage());
				 if(!file.exists())
				 {
					 orders.get(i).image = "";
				 }
				 else
				 {
					 orders.get(i).image = product.getName();
				 }
				 
				 file = new File(product.getDrawing());
				 if(!file.exists())
				 {
					 orders.get(i).drawing = "";
				 }
				 else
				 {
					 orders.get(i).drawing = product.getName();
				 }
			}
		}
	}
	
	public static void fillImageDrawingForJob(List<JsonJob> jobs, IOhtuneService service)
	{
		Product product = null;
		File file;
		for(int i = 0; i < jobs.size(); i++)
		{
			product  = service.getProductByName(jobs.get(i).product_name);
			if(product == null)
			{
				jobs.get(i).image = "";
				jobs.get(i).drawing = "";
			}
			else
			{
				 file = new File(product.getImage());
				 if(!file.exists())
				 {
					 jobs.get(i).image = "";
				 }
				 else
				 {
					 jobs.get(i).image = product.getName();
				 }
				 
				 
				 file = new File(product.getDrawing());
				 if(!file.exists())
				 {
					 jobs.get(i).drawing = "";
				 }
				 else
				 {
					 jobs.get(i).drawing = product.getName();
				 }
			}
		}
	}
	
	public static void fillImageDrawingForProduct(List<JsonProduct> products, IOhtuneService service)
	{
		Product product = null;
		File file;
		for(int i = 0; i < products.size(); i++)
		{
			product  = service.getProductByName(products.get(i).name);
			if(product == null)
			{
				products.get(i).image = "";
				products.get(i).drawing = "";
			}
			else
			{
				 file = new File(product.getImage());
				 if(!file.exists())
				 {
					 products.get(i).image = "";
				 }
				 else
				 {
					 products.get(i).image = product.getName();
				 }
				 
				 
				 file = new File(product.getDrawing());
				 if(!file.exists())
				 {
					 products.get(i).drawing = "";
				 }
				 else
				 {
					 products.get(i).drawing = product.getName();
				 }
			}
		}
	}
	
	public static void fillImageDrawingForProductLog(List<ProductionLog> products, IOhtuneService service)
	{
		Product product = null;
		File file;
		for(int i = 0; i < products.size(); i++)
		{
			product  = service.getProductByName(products.get(i).getProduct_name());
			if(product == null)
			{
				products.get(i).image = "";
				products.get(i).drawing = "";
			}
			else
			{
				 file = new File(product.getImage());
				 if(!file.exists())
				 {
					 products.get(i).image = "";
				 }
				 else
				 {
					 products.get(i).image = product.getName();
				 }
				 
				 
				 file = new File(product.getDrawing());
				 if(!file.exists())
				 {
					 products.get(i).drawing = "";
				 }
				 else
				 {
					 products.get(i).drawing = product.getName();
				 }
			}
		}
	}
}
