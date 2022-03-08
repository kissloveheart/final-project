package com.hiep.finalproject.repository;

import com.hiep.finalproject.model.Campaign;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CampaignRepository extends CrudRepository<Campaign,Long> {
    @Query("select c from Campaign as c left join c.donationList")
    Page<Campaign> findAllCampaign(Pageable pageable);
    @Query("select c from Campaign as c left join c.donationList as d join d.account where c.id =?1")
    Optional<Campaign> findCampaignById(Long id);
}
