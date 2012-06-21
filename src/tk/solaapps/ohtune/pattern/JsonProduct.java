package tk.solaapps.ohtune.pattern;

import tk.solaapps.ohtune.model.Product;

public class JsonProduct {
	public String name;
	public String name_eng;
	public String status;
	public String image;
	public String drawing;
	public String our_name;
	public String mold_rate;
	public String machining_pos;
	public String handwork_pos;
	public String polishing;
	public Integer finished;
	public Integer semi_finished;
	public String mold_code;
	public String mold_name;
	public String mold_stand_no;
	
	public JsonProduct(Product product)
	{
		this.name = product.getName();
		this.name_eng = product.getName_eng();
		this.drawing = product.getDrawing();
		this.finished = product.getFinished();
		this.handwork_pos = product.getHandwork_pos();
		this.image = product.getImage();
		this.machining_pos = product.getMachining_pos();
		this.mold_rate = product.getMold_rate();
		this.our_name = product.getOur_name();
		this.polishing = product.getPolishing();
		this.semi_finished = product.getSemi_finished();
		this.status = product.getStatus();
		this.mold_code = product.getMold() == null ? "" : product.getMold().getCode();
		this.mold_name = product.getMold() == null ? "" : product.getMold().getName();
		this.mold_stand_no = product.getMold() == null ? "" : product.getMold().getStand_no();
	}
}
