package meli.challenge.test.model;

import meli.challenge.test.utils.UUIDHelper;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "statistics")
public class Statistics implements Serializable {
    private static final long serialVersionUID = -7534142259639944319L;

    @Id
    private String statisticsId;

    private String ip;
    private Double distanceToVillavo;

    public Statistics() {
    }

    public Statistics(String ip, Double distanceToVillavo) {
        this.statisticsId = UUIDHelper.getUuid();
        this.ip = ip;
        this.distanceToVillavo = distanceToVillavo;
    }

    public String getStatisticsId() {
        return statisticsId;
    }

    public void setStatisticsId(String statisticsId) {
        this.statisticsId = statisticsId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Double getDistanceToVillavo() {
        return distanceToVillavo;
    }

    public void setDistanceToVillavo(Double distanceToVillavo) {
        this.distanceToVillavo = distanceToVillavo;
    }

}
