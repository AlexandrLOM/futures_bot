package com.lom.futures.dto.context;

import com.lom.futures.dto.Kline;
import com.lom.futures.dto.Order;
import com.lom.futures.dto.OrderBook;
import com.lom.futures.dto.Position;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ContextBot {

    List<Kline> klinesForBtcUsdt1h;
    List<Position> positions;
    OrderBook orderBook;
}
