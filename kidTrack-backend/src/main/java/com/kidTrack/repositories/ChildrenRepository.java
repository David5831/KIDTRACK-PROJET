package com.kidTrack.repositories;

import com.kidTrack.models.Children;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ChildrenRepository extends JpaRepository<Children, Integer> {
    List<Children> findByParentId(Integer parentId);
    List<Children> findByGroupId(Integer groupId);
}
