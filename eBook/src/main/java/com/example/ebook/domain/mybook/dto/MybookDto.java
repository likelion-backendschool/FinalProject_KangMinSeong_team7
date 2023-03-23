package com.example.ebook.domain.mybook.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MybookDto {

    private Long id;
    private Long productId;

    private String productSubject;
    private String productDescription;
    private String productWriter;
}
