package IChellquist.BudgetbergTerminal.repository;

import IChellquist.BudgetbergTerminal.models.ERole;
import IChellquist.BudgetbergTerminal.models.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RoleRepository extends MongoRepository <Role, String>{
    Optional<Role> findByName(ERole name);

    boolean existsByName(ERole name);
}
