package parking.com.slash.parking.model.ModelCarBrand;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Model
{
    @Expose
    @SerializedName("name")
    private String name;
    @Expose
    @SerializedName("id")
    private String id;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }
}
