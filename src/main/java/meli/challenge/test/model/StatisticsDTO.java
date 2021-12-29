package meli.challenge.test.model;

import java.io.Serializable;


public class StatisticsDTO implements Serializable {
    private static final long serialVersionUID = -79825899415219000L;


    private Double average;
    private Double maxDistanceToVillavo;
    private Double minDistanceToVillavo;
    private Integer quantity;

    public StatisticsDTO() {
    }

    public StatisticsDTO(Double average, Double maxDistanceToVillavo, Double minDistanceToVillavo, Integer quantity) {
        this.average = average;
        this.maxDistanceToVillavo = maxDistanceToVillavo;
        this.minDistanceToVillavo = minDistanceToVillavo;
        this.quantity = quantity;
    }

    public Double getAverage() {
        return average;
    }

    public void setAverage(Double average) {
        this.average = average;
    }

    public Double getMaxDistanceToVillavo() {
        return maxDistanceToVillavo;
    }

    public void setMaxDistanceToVillavo(Double maxDistanceToVillavo) {
        this.maxDistanceToVillavo = maxDistanceToVillavo;
    }

    public Double getMinDistanceToVillavo() {
        return minDistanceToVillavo;
    }

    public void setMinDistanceToVillavo(Double minDistanceToVillavo) {
        this.minDistanceToVillavo = minDistanceToVillavo;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
