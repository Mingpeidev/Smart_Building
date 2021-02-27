package cn.mao.pojo;

public class Resident {
    private Integer id;

    private String residentname;

    private String doorid;

    private String sex;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getResidentname() {
        return residentname;
    }

    public void setResidentname(String residentname) {
        this.residentname = residentname == null ? null : residentname.trim();
    }

    public String getDoorid() {
        return doorid;
    }

    public void setDoorid(String doorid) {
        this.doorid = doorid == null ? null : doorid.trim();
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex == null ? null : sex.trim();
    }
}