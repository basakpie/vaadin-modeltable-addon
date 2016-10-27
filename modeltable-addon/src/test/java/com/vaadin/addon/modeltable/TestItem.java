package com.vaadin.addon.modeltable;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by gmind on 2016-10-27.
 */
public class TestItem implements Serializable {

    private static final long serialVersionUID = 1L;

    private String field01 = "1";
    private Integer field02 = 2;
    private Long field03 = 3L;
    private Float field04 = 4f;
    private Double field05 = 5d;
    private Date field06 = new Date();
    private boolean field07 = true;
    private String field08 = "8";
    private String field09 = "9";
    private String field10 = "10";
    private String field11 = "11";
    private String field12 = "12";
    private String field13 = "13";
    private String field14 = "14";
    private String field15 = "15";
    private String field16 = "16";

    public String getField01() {
        return field01;
    }

    public void setField01(String field01) {
        this.field01 = field01;
    }

    public Integer getField02() {
        return field02;
    }

    public void setField02(Integer field02) {
        this.field02 = field02;
    }

    public Long getField03() {
        return field03;
    }

    public void setField03(Long field03) {
        this.field03 = field03;
    }

    public Float getField04() {
        return field04;
    }

    public void setField04(Float field04) {
        this.field04 = field04;
    }

    public Double getField05() {
        return field05;
    }

    public void setField05(Double field05) {
        this.field05 = field05;
    }

    public Date getField06() {
        return field06;
    }

    public void setField06(Date field06) {
        this.field06 = field06;
    }

    public boolean isField07() {
        return field07;
    }

    public void setField07(boolean field07) {
        this.field07 = field07;
    }

    public String getField08() {
        return field08;
    }

    public void setField08(String field08) {
        this.field08 = field08;
    }

    public String getField09() {
        return field09;
    }

    public void setField09(String field09) {
        this.field09 = field09;
    }

    public String getField10() {
        return field10;
    }

    public void setField10(String field10) {
        this.field10 = field10;
    }

    public String getField11() {
        return field11;
    }

    public void setField11(String field11) {
        this.field11 = field11;
    }

    public String getField12() {
        return field12;
    }

    public void setField12(String field12) {
        this.field12 = field12;
    }

    public String getField13() {
        return field13;
    }

    public void setField13(String field13) {
        this.field13 = field13;
    }

    public String getField14() {
        return field14;
    }

    public void setField14(String field14) {
        this.field14 = field14;
    }

    public String getField15() {
        return field15;
    }

    public void setField15(String field15) {
        this.field15 = field15;
    }

    public String getField16() {
        return field16;
    }

    public void setField16(String field16) {
        this.field16 = field16;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("TestItem{");
        sb.append("field01='").append(field01).append('\'');
        sb.append(", field02=").append(field02);
        sb.append(", field03=").append(field03);
        sb.append(", field04=").append(field04);
        sb.append(", field05=").append(field05);
        sb.append(", field06=").append(field06);
        sb.append(", field07=").append(field07);
        sb.append(", field08='").append(field08).append('\'');
        sb.append(", field09='").append(field09).append('\'');
        sb.append(", field10='").append(field10).append('\'');
        sb.append(", field11='").append(field11).append('\'');
        sb.append(", field12='").append(field12).append('\'');
        sb.append(", field13='").append(field13).append('\'');
        sb.append(", field14='").append(field14).append('\'');
        sb.append(", field15='").append(field15).append('\'');
        sb.append(", field16='").append(field16).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
