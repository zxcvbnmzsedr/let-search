package com.shiyisoushu.repo.mysql;

import com.shiyisoushu.repo.mysql.model.WebsiteModel;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WebsiteRepo extends PagingAndSortingRepository<WebsiteModel, Long> {

    List<WebsiteModel> findByEnableEquals(Boolean enable);


    /**
     * 获取站点
     *
     * @param site 站点
     * @return 站点信息
     */
    WebsiteModel findBySite(String site);
}
