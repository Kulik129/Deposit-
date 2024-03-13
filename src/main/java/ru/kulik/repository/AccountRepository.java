package ru.kulik.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kulik.models.Account;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByUuid(UUID uuid);
    void deleteByUuid(UUID uuid);

}
