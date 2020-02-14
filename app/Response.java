package null;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class Response{

	@SerializedName("candidates")
	private List<CandidatesItem> candidates;

	@SerializedName("status")
	private String status;

	public void setCandidates(List<CandidatesItem> candidates){
		this.candidates = candidates;
	}

	public List<CandidatesItem> getCandidates(){
		return candidates;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}

	@Override
 	public String toString(){
		return 
			"Response{" + 
			"candidates = '" + candidates + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}