package com.lom.futures.db.entity;

import com.lom.futures.enums.PositionSide;
import com.lom.futures.enums.Symbol;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Data
@Entity
@Table(name = "history_bet")
public class HistoryBet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "symbol")
    private String symbol;

    @Column(name = "position_side")
    private String positionSide;

    @Column(name = "status")
    private Integer status;

    @Column(name = "timestamp")
    private Instant timestamp;

    @Column(name = "tag")
    private UUID tag;




}
