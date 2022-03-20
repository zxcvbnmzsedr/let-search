package com.shiyisoushu.web.service;

import com.shiyisoushu.repo.mysql.model.WebsiteModel;
import com.shiyisoushu.repo.mysql.WebsiteRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WebsiteService {
    @Autowired
    private WebsiteRepo websiteRepo;

    @Cacheable("website")
    public List<WebsiteModel> findEnableWebsite() {
        // 寻找启用的站点
        return websiteRepo.findByEnableEquals(true);
    }

}
