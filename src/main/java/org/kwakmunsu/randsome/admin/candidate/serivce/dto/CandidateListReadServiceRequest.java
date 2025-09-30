package org.kwakmunsu.randsome.admin.candidate.serivce.dto;

import org.kwakmunsu.randsome.domain.candidate.enums.CandidateStatus;

public record CandidateListReadServiceRequest(CandidateStatus status, int page) {

}