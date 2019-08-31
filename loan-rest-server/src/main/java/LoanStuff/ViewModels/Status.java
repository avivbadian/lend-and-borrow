package LoanStuff.ViewModels;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public enum Status implements Serializable {
    @JsonProperty("pending")
    pending,
    @JsonProperty("approved")
    approved,
    @JsonProperty("declined")
    declined
}