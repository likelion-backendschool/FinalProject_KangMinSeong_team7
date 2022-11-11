package com.example.eBook.global.mapper;

import com.example.eBook.domain.withdraw.dto.AdmWithdrawApplyDto;
import com.example.eBook.domain.withdraw.dto.WithdrawApplyDto;
import com.example.eBook.domain.withdraw.entity.WithdrawApply;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface WithdrawApplyMapper {

    WithdrawApplyMapper INSTANCE = Mappers.getMapper(WithdrawApplyMapper.class);

    WithdrawApplyDto entityToWithdrawApplyDto(WithdrawApply withdrawApply);

    List<WithdrawApplyDto> entitiesToWithdrawApplyDtos(List<WithdrawApply> withdrawApplies);

    @Mapping(target = "canApply", source = "withdrawApply", qualifiedByName="canApply")
    @Mapping(target = "applicantName", source = "applicant.nickname")
    AdmWithdrawApplyDto entityToAdmWithdrawApplyDto(WithdrawApply withdrawApply);

    List<AdmWithdrawApplyDto> entitiesToAdmWithdrawApplyDtos(List<WithdrawApply> withdrawApplies);

    @Named("canApply")
    static boolean canApply(WithdrawApply withdrawApply) {
        return !withdrawApply.isDone();
    }
}
