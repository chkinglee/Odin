package org.chkinglee.norn.odin.mapper.zhihu;

import org.chkinglee.norn.odin.model.zhihu.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * TODO
 *
 * @Author: lilinzhen
 * @Version: 2020/7/30
 **/
public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findByTitleContainingAndTitleDetailContaining(String title, String titleDetail);
}
