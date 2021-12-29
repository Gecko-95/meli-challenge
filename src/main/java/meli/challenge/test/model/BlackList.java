package meli.challenge.test.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "black_list")
public class BlackList implements Serializable {
    private static final long serialVersionUID = 4098292145984257842L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long listId;

    private String ip;

    public BlackList() {
    }

    public BlackList(String ip) {
        this.ip = ip;
    }

    public long getListId() {
        return listId;
    }

    public void setListId(long listId) {
        this.listId = listId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

}
