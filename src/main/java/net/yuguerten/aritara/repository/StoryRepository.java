package net.yuguerten.aritara.repository;

import net.yuguerten.aritara.model.Story;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface StoryRepository extends JpaRepository<Story, Long> {
    List<Story> findAll();
    List<Story> findByUserId(Long userId);
    Optional<Story> findById(Long id);
    void deleteById(Long id);
}
