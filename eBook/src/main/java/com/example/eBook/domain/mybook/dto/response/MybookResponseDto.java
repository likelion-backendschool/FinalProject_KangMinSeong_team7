package com.example.eBook.domain.mybook.dto.response;

import com.example.eBook.domain.mybook.entity.Mybook;
import com.example.eBook.domain.product.dto.response.ProductResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class MybookResponseDto {

    private Long id;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;

    private Long ownerId;
    private ProductResponseDto product;

    public static MybookResponseDto toResponse(Mybook mybook) {
        ProductResponseDto product = ProductResponseDto.toResponseDto(mybook.getProduct());

        return MybookResponseDto.builder()
                .id(mybook.getId())
                .createDate(mybook.getCreateDate())
                .modifyDate(mybook.getUpdateDate())
                .ownerId(mybook.getMember().getId())
                .product(product)
                .build();
    }
}
