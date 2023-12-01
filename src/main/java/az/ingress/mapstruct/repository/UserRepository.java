package az.ingress.mapstruct.repository;

import az.ingress.mapstruct.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
