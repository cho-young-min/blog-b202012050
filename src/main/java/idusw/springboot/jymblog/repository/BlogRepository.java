package idusw.springboot.jymblog.repository;

import idusw.springboot.jymblog.entity.BlogEntity;
import idusw.springboot.jymblog.model.BlogDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface BlogRepository extends JpaRepository<BlogEntity, Long> { // interface 상속,
    Optional<BlogEntity> findByIdx(BlogDto dto);
}
