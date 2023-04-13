package com.lom.futures.dto.context;

import com.lom.futures.dto.Kline;
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

    List<Kline> klinesForEthUsdt3m3limit;
}
