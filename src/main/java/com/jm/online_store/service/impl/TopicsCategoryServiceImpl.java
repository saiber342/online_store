package com.jm.online_store.service.impl;

import com.jm.online_store.exception.aatest.ExceptionConstants;
import com.jm.online_store.exception.aatest.ExceptionEnums;
import com.jm.online_store.exception.topicService.TopicNotFoundException;
import com.jm.online_store.exception.topicsCategoryService.TopicCategoryAlreadyExists;
import com.jm.online_store.exception.topicsCategoryService.TopicCategoryNotFoundException;
import com.jm.online_store.exception.topicsCategoryService.TopicsCategoryExceptionConstants;
import com.jm.online_store.exception.topicsCategoryService.TopicsCategoryServiceException;
import com.jm.online_store.model.TopicsCategory;
import com.jm.online_store.repository.TopicsCategoryRepository;
import com.jm.online_store.service.interf.TopicsCategoryService;
import lombok.AllArgsConstructor;
import org.checkerframework.checker.nullness.Opt;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class TopicsCategoryServiceImpl implements TopicsCategoryService {

    private final TopicsCategoryRepository topicsCategoryRepository;

    @Override
    @Transactional
    public TopicsCategory create(TopicsCategory topicsCategory) {
        if (topicsCategoryRepository.existsByCategoryName(topicsCategory.getCategoryName()))
            throw new TopicCategoryAlreadyExists(ExceptionEnums.TOPIC_CATEGORY.getDescription() +
                    ExceptionConstants.ALREADY_EXISTS);
        return topicsCategoryRepository.saveAndFlush(topicsCategory);
    }

    @Override
    public List<TopicsCategory> findAll() {
        return topicsCategoryRepository.findAll();
    }

    @Override
    public List<TopicsCategory> findAllByActualIsTrue() {
        return topicsCategoryRepository.findAllByActualIsTrue();
    }

    @Override
    public TopicsCategory findById(Long id) {
        Optional <TopicsCategory> optional = topicsCategoryRepository.findById(id);
        if (optional.isEmpty()) {
            throw new TopicNotFoundException(ExceptionEnums.TOPIC_CATEGORY.getDescription()
                    + String.format(ExceptionConstants.WITH_SUCH_ID_NOT_FOUND, id));
        }
        return optional.get();
    }

    @Override
    public TopicsCategory findByCategoryName(String categoryName) {
        return topicsCategoryRepository.findByCategoryName(categoryName);
    }

    @Override
    public boolean existsById(long id) {
        return topicsCategoryRepository.existsById(id);
    }

    @Override
    public boolean existsByCategoryName(String categoryName) {

        return topicsCategoryRepository.existsByCategoryName(categoryName);
    }

    @Override
    @Transactional
    public TopicsCategory update(TopicsCategory topicsCategory) {
        return topicsCategoryRepository.saveAndFlush(topicsCategory);
    }

    @Override
    @Transactional
    public TopicsCategory updateById(Long id, TopicsCategory topicsCategory) {
        //ищем в базе данных по id
        Optional<TopicsCategory> optional = topicsCategoryRepository.findById(id);
        // если найден по id
        if (optional.isPresent()) {
            //сравниваем по имени
           if (optional.get().getCategoryName().equals(topicsCategory.getCategoryName())) {
               //сохраняем если по имени равны
               return topicsCategoryRepository.saveAndFlush(topicsCategory);
           } else {
               //бросаем исключение если по имени не равны
               throw new TopicCategoryNotFoundException(ExceptionEnums.TOPIC_CATEGORY.getDescription()
                       + ExceptionConstants.NOT_FOUND);
           }
        } else {
            // бросаем исключение если по id не найдено
            throw new TopicCategoryNotFoundException(ExceptionEnums.TOPIC_CATEGORY.getDescription() +
                    String.format(ExceptionConstants.WITH_SUCH_ID_NOT_FOUND, id));
        }
    }

    @Override
    @Transactional
    public TopicsCategory archive(Long id) {
        // вызываем метод (см. выше) который ищет по id, он вернет или бросит исключение TopicNotFoundException
        TopicsCategory toSave = findById(id);
        // на всякий случай проверим на null
        if (null != toSave) {
            //проверяем является ли категория не архивной
            if (toSave.getActual()) {
                //архивируем , меняя actual с true на false
                toSave.setActual(false);
                //делаем save
                topicsCategoryRepository.saveAndFlush(toSave);
            } else {
                //бросаем исключение если пытаются изменить actual без необходимости
                throw new TopicCategoryAlreadyExists(ExceptionEnums.TOPIC_CATEGORY.getDescription()  +
                        ExceptionConstants.ALREADY_ARCHIVED);
            }

        }
        return toSave;
    }



    @Override
    @Transactional
    public TopicsCategory unarchive(Long id) {
        // вызываем метод (см. выше) который ищет по id, он вернет или бросит исключение TopicNotFoundException
        TopicsCategory toSave = findById(id);
        // на всякий случай проверим на null
        if (null != toSave) {
            //проверяем является ли категория архивной
            if (!toSave.getActual()) {
                //деархивируем , меняя actual с false на true
                toSave.setActual(true);
                //делаем save
                topicsCategoryRepository.saveAndFlush(toSave);
            } else {
                //бросаем исключение если пытаются изменить actual без необходимости
                throw new TopicCategoryAlreadyExists(ExceptionEnums.TOPIC_CATEGORY.getDescription() +
                        ExceptionConstants.ALREADY_UNARCHIVED);
            }

        }
        return toSave;
    }


    @Override
    public TopicsCategory archive(TopicsCategory topicsCategory) {
        return null;
    }

    @Override
    public TopicsCategory unarchive(TopicsCategory topicsCategory) {
        return null;
    }
}
