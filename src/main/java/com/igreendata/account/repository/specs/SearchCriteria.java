package com.igreendata.account.repository.specs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class SearchCriteria {

    private String key;
    private Object value;
    private SearchOperation operation;
}
