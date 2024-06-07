package com.shadoww.BookLibraryApp.dto.request.books;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class BookFilterRequest {
    private String searchText;
    private Integer fromAmount;
    private Integer toAmount;
    private Integer fromYear;
    private Integer toYear;

    public BookFilterRequest(String searchText, Integer fromAmount, Integer toAmount, Integer fromYear, Integer toYear) {
        this.searchText = searchText;
        this.fromAmount = fromAmount;
        this.toAmount = toAmount;
        this.fromYear = fromYear;
        this.toYear = toYear;
    }


}
