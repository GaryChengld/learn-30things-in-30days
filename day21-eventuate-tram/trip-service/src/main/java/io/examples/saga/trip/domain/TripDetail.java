package io.examples.saga.trip.domain;

import javax.persistence.Embeddable;

/**
 * @author Gary Cheng
 */
@Embeddable
public class TripDetail {
    private String customerName;
    private String fromCity;
    private String toCity;
    private String tripDate;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getFromCity() {
        return fromCity;
    }

    public void setFromCity(String fromCity) {
        this.fromCity = fromCity;
    }

    public String getToCity() {
        return toCity;
    }

    public void setToCity(String toCity) {
        this.toCity = toCity;
    }

    public String getTripDate() {
        return tripDate;
    }

    public void setTripDate(String tripDate) {
        this.tripDate = tripDate;
    }
}
