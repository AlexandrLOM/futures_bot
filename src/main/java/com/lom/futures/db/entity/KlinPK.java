package com.lom.futures.db.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class KlinPK implements Serializable {

    private String interval;

    private String symbol;

    private Long openTime;

    private Long closeTime;


}
