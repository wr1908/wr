package com.fh.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fh.model.DataTablePageBean;

public class OderVo extends DataTablePageBean {



    @TableField("addressId")
    private Integer addressId;

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }
}
