package parking.com.slash.parking.model.ModelGetAddressFromMap;

import com.google.gson.annotations.SerializedName;

import java.sql.Types;
import java.util.List;

/**
 * Created by andre on 12-Feb-18.
 */

public class ModelGetAddressFromMap
{

    @SerializedName("results")
    private List<Results> results;
    @SerializedName("status")
    private String status;

    public List<Results> getResults()
    {
        return results;
    }

    public void setResults(List<Results> results)
    {
        this.results = results;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public static class Address_components
    {
        @SerializedName("long_name")
        private String long_name;
        @SerializedName("short_name")
        private String short_name;
        @SerializedName("types")
        private List<String> types;

        public String getLong_name()
        {
            return long_name;
        }

        public void setLong_name(String long_name)
        {
            this.long_name = long_name;
        }

        public String getShort_name()
        {
            return short_name;
        }

        public void setShort_name(String short_name)
        {
            this.short_name = short_name;
        }

        public List<String> getTypes()
        {
            return types;
        }

        public void setTypes(List<String> types)
        {
            this.types = types;
        }
    }

    public static class Results
    {
        @SerializedName("address_components")
        private List<Address_components> address_components;
        @SerializedName("formatted_address")
        private String formatted_address;

        @SerializedName("place_id")
        private String place_id;
        @SerializedName("types")
        private List<String> types;

        public List<Address_components> getAddress_components()
        {
            return address_components;
        }

        public void setAddress_components(List<Address_components> address_components)
        {
            this.address_components = address_components;
        }

        public String getFormatted_address()
        {
            return formatted_address;
        }

        public void setFormatted_address(String formatted_address)
        {
            this.formatted_address = formatted_address;
        }


        public String getPlace_id()
        {
            return place_id;
        }

        public void setPlace_id(String place_id)
        {
            this.place_id = place_id;
        }

        public List<String> getTypes()
        {
            return types;
        }

        public void setTypes(List<String> types)
        {
            this.types = types;
        }
    }
}
