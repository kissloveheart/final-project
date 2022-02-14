package com.hiep.finalproject.repository;

import com.hiep.finalproject.dto.IOrganizationDto;
import com.hiep.finalproject.model.Organization;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrganizationRepository extends CrudRepository<Organization,Long> {
    @Query("select o.id as id, o.name as name, o.description as description, " +
            "count(distinct(c.id)) as countCampaign, sum(d.amount) as totalMoneyDonation " +
            "from Organization o left join o.campaignList c " +
            "left join c.donationList d " +
            "group by  o.id, o.name, o.description")
    List<IOrganizationDto> findAllOrganization();

}
