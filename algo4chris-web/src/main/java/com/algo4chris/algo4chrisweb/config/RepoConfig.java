package com.algo4chris.algo4chrisweb.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 *
 *
 * */
@Configuration
@EnableTransactionManagement
@EntityScan("com.algo4chris.algo4chrisdal.models")
@EnableJpaRepositories("com.algo4chris.algo4chrisdal.repository")
public class RepoConfig {
}
