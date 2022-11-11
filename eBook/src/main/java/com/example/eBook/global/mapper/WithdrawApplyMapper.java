package com.example.eBook.global.mapper;

import com.example.eBook.domain.withdraw.dto.WithdrawApplyDto;
import com.example.eBook.domain.withdraw.entity.WithdrawApply;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface WithdrawApplyMapper {

    WithdrawApplyMapper INSTANCE = Mappers.getMapper(WithdrawApplyMapper.class);

    WithdrawApplyDto entityToWithdrawApplyDto(WithdrawApply withdrawApply);

    List<WithdrawApplyDto> entitiesToWithdrawApplyDtos(List<WithdrawApply> withdrawApply);
}
