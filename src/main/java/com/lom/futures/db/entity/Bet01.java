package com.lom.futures.db.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@IdClass(KlinPK.class)
@Table(name = "bet_01")
public class Bet01 {

    @Id
    @Column
    private String interval;

    @Id
    @Column(name = "symbol")
    private String symbol;

    @Id
    @Column(name = "open_time")
    private Long openTime;

    @Column(name = "open_time_str")
    private String openTimeStr;

    @Id
    @Column(name = "close_time")
    private Long closeTime;

    @Column(name = "long_buy")
    private Long longBuy;

    @Column(name = "short_sell")
    private Long shortSell;


}
