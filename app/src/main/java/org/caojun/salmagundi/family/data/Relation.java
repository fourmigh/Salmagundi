package org.caojun.salmagundi.family.data;

/**
 * Created by CaoJun on 2017/8/10.
 */

public class Relation {

    public static final int FZ = 0;//父子
    public static final int FQ = 1;//夫妻
    public static final int XD = 2;//兄弟

    private int rank;//辈分（0：平辈，1：长一辈，-1：小一辈，……）
    private boolean male;//性别（true：男性，false：女性）
    private int link;//关系（0：父子，1：夫妻，2：兄弟）
    private boolean elder;//true：年长，false：年幼
    private String name;//称谓

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

    public int getLink() {
        return link;
    }

    public void setLink(int link) {
        this.link = link;
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
