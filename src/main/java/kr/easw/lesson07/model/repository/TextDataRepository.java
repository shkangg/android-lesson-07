package kr.easw.lesson07.model.repository;

import kr.easw.lesson07.model.dto.TextDataDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TextDataRepository extends JpaRepository<TextDataDto, Long> {

}
