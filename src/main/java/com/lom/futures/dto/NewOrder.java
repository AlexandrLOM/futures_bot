package com.lom.futures.dto;

import com.lom.futures.enums.OrderType;
import com.lom.futures.enums.PositionSide;
import com.lom.futures.enums.Side;
import com.lom.futures.enums.Symbol;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewOrder {

    Long orderId;
    Symbol symbol;
    String status;
    String clientOrderId;
    Double price;
    Double avgPrice;
    Double origQty;
    Double executedQty;
    Double cumQty;
    Double cumQuote;
    String timeInForce;
    OrderType type;
    Boolean reduceOnly;
    Boolean closePosition;
    Side side;
    PositionSide positionSide;
    Double stopPrice;
    String workingType;
    Boolean priceProtect;
    OrderType origType;
    Long updateTime;
    String priceMatch;
    String selfTradePreventionMode;
    String goodTillDate;

}
