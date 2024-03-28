package com.spring.auth.aggregate.jwt.store.jpa.repository;

import com.spring.auth.aggregate.jwt.store.jpa.jpe.AuthorizedClientJpe;
import com.spring.auth.aggregate.jwt.store.jpa.jpe.ClientPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaAuthorizedClientRepository extends JpaRepository<AuthorizedClientJpe, ClientPK> {
    AuthorizedClientJpe findByClientPkClientRegistrationIdAndClientPkPrincipalName(String clientRegistrationId, String principalName);
    void deleteByClientPkClientRegistrationIdAndClientPkPrincipalName(String clientRegistrationId, String principalName);
}
