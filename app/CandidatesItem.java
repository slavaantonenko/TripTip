package null;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class CandidatesItem{

	@SerializedName("photos")
	private List<PhotosItem> photos;

	public void setPhotos(List<PhotosItem> photos){
		this.photos = photos;
	}

	public List<PhotosItem> getPhotos(){
		return photos;
	}

	@Override
 	public String toString(){
		return 
			"CandidatesItem{" + 
			"photos = '" + photos + '\'' + 
			"}";
		}
}