package com.spring.auth.aggregate.jwt.store.jpa.jpe;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;


@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class ClientPK implements Serializable {

    @Column(name="CLIENT_REGISTRATION_ID")
    private String clientRegistrationId;

    @Column(name="PRINCIPAL_NAME")
    private String principalName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientPK clientPK = (ClientPK) o;
        return Objects.equals(clientRegistrationId, clientPK.clientRegistrationId) && Objects.equals(principalName, clientPK.principalName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientRegistrationId, principalName);
    }
}
