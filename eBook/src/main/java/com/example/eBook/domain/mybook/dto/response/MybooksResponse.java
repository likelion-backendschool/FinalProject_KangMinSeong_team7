package com.example.eBook.domain.mybook.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class MybooksResponse {
    private List<MybookResponseDto> myBooks;
}
