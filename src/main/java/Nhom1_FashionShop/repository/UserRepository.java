package Nhom1_FashionShop.repository;

import Nhom1_FashionShop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
