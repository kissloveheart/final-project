package com.hiep.finalproject.repository;

import com.hiep.finalproject.dto.IDonationDto;
import com.hiep.finalproject.model.Donation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface DonationRepository extends CrudRepository<Donation,Long> {
    @Query("select d.id as id,d.date as date, d.amount as amount,c.name as campaign, o.name as organization" +
            " from Donation d join d.account a join d.campaign c join c.organization o where a.id =?1")
    Page<IDonationDto> getAllByAccountId(Long id, Pageable pageable);

}
