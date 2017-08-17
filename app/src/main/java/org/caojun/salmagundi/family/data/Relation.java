package org.caojun.salmagundi.family.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CaoJun on 2017/8/10.
 */

public class Relation {

    public static final int FZ = 1;//父子
    public static final int XD = 2;//兄弟
    public static final int FQ = 3;//夫妻

    private int rank;//辈分（0：平辈，1：长一辈，-1：小一辈，……）
    private Boolean male;//性别（true：男性，false：女性，null：未知）
    private List<Integer> link;//关系（0：父子，1：夫妻，2：兄弟）
    private Boolean elder;//true：年长，false：年幼，null：未知
    private String name;//称谓

    public Relation() {}

    public Relation(int rank, boolean male, int link, String name) {
        setRank(rank);
        setMale(male);
        setLink(link);
        setName(name);
    }

    public Relation(int rank, boolean male, int link, String name, boolean elder) {
        this(rank, male, link, name);
        setElder(elder);
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public boolean isMale() {
        return male;
    }

    public void setMale(boolean male) {
        this.male = male;
    }

    public List<Integer> getLink() {
        return link;
    }

    public int getLink(int index) {
        if (link == null || index < 0 || index >= link.size()) {
            return 0;
        }
        return link.get(index);
    }

    public void setLink(int link) {
        if (this.link == null) {
            this.link = new ArrayList<>();
        }
        this.link.add(link);
    }

    public void setLink(List<Integer> link) {
        if (this.link == null) {
            this.link = new ArrayList<>();
        }
        this.link.addAll(link);
    }

    public boolean isElder() {
        return elder;
    }

    public void setElder(boolean elder) {
        this.elder = elder;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
