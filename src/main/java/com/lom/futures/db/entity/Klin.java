package com.lom.futures.db.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@IdClass(KlinPK.class)
@Table(name = "klines")
public class Klin {

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

    @Column
    private Double open;

    @Column
    private Double high;

    @Column
    private Double low;

    @Column
    private Double close;

    @Column
    private Double volume;

    @Id
    @Column(name = "close_time")
    private Long closeTime;

    @Column(name = "quote_asset_volume")
    private Double quoteAssetVolume;

    @Column(name = "number_of_trades")
    private Integer numberOfTrades;

    @Column(name = "taker_buy_base_asset_volume")
    private Double takerBuyBaseAssetVolume;

    @Column(name = "taker_buy_quote_asset_volume")
    private Double takerBuyQuoteAssetVolume;

}
