package com.lom.futures.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FuturesBalance {

    String accountAlias;
    String asset;
    Double balance;
    Double crossWalletBalance;
    Double crossUnPnl;
    Double availableBalance;
    Double maxWithdrawAmount;
    Boolean marginAvailable;
    Long updateTime;

}
