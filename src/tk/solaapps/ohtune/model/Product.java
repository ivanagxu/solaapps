package tk.solaapps.ohtune.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="products")

public class Product {
	public static final String STATUS_ENABLE = "有效";
	public static final String STATUS_DISABLED = "无效";
	
	private String name;
	private String name_eng;
	private String status;
	private String image;
	private String drawing;
	private String our_name;
	private String mold_rate;
	private String machining_pos;
	private String handwork_pos;
	private String polishing;
	private Integer finished;
	private Integer semi_finished;
	private Mold mold;
	
	@Id
	@Column(name="name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name="name_eng")
	public String getName_eng() {
		return name_eng;
	}
	public void setName_eng(String name_eng) {
		this.name_eng = name_eng;
	}
	
	@Column(name="status")
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	@Column(name="image")
	public String getImage()
	{
		return image;
	}
	public void setImage(String image)
	{
		this.image = image;
	}
	
	@Column(name="drawing")
	public String getDrawing()
	{
		return drawing;
	}
	public void setDrawing(String drawing)
	{
		this.drawing = drawing;
	}
	
	@Column(name="our_name")
	public String getOur_name() {
		return our_name;
	}
	public void setOur_name(String our_name) {
		this.our_name = our_name;
	}
	
	@Column(name="mold_rate")
	public String getMold_rate() {
		return mold_rate;
	}
	public void setMold_rate(String mold_rate) {
		this.mold_rate = mold_rate;
	}
	
	@Column(name="machining_pos")
	public String getMachining_pos() {
		return machining_pos;
	}
	public void setMachining_pos(String machining_pos) {
		this.machining_pos = machining_pos;
	}
	
	@Column(name="handwork_pos")
	public String getHandwork_pos() {
		return handwork_pos;
	}
	public void setHandwork_pos(String handwork_pos) {
		this.handwork_pos = handwork_pos;
	}
	
	@Column(name="polishing")
	public String getPolishing() {
		return polishing;
	}
	public void setPolishing(String polishing) {
		this.polishing = polishing;
	}
	
	@Column(name="finished")
	public Integer getFinished() {
		return finished;
	}
	public void setFinished(Integer finished) {
		this.finished = finished;
	}
	
	@Column(name="semi_finished")
	public Integer getSemi_finished() {
		return semi_finished;
	}
	public void setSemi_finished(Integer semi_finished) {
		this.semi_finished = semi_finished;
	}
	
	@OneToOne
	@JoinColumn(name="mold")
	public Mold getMold() {
		return mold;
	}
	public void setMold(Mold mold) {
		this.mold = mold;
	}
}
