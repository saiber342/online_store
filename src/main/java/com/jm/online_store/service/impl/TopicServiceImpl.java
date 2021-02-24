package com.jm.online_store.service.impl;

import com.jm.online_store.exception.constants.ExceptionConstants;
import com.jm.online_store.enums.ExceptionEnums;
import com.jm.online_store.exception.TopicAlreadyExists;
import com.jm.online_store.exception.TopicNotFoundException;
import com.jm.online_store.model.Topic;
import com.jm.online_store.repository.TopicRepository;
import com.jm.online_store.service.interf.TopicService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class TopicServiceImpl implements TopicService {
    private final TopicRepository topicRepository;

    @Override
    @Transactional
    public Topic create(Topic topic) {
        if (existsByTopicName(topic.getTopicName())) {
            throw new TopicAlreadyExists(ExceptionEnums.TOPIC.getText() + String.format(ExceptionConstants.ALREADY_EXISTS, topic.getTopicName()));
        }
        return topicRepository.saveAndFlush(topic);
    }

    @Override
    public List<Topic> getTopicsByCategoryId(long id) {
        return topicRepository.findTopicByTopicsCategoryId(id);
    }

    @Override
    public boolean existsByTopicName(String topicName) {
        return topicRepository.existsByTopicName(topicName);
    }

    @Override
    public boolean existsById(long id) {
        return topicRepository.existsById(id);
    }

    @Override
    public Topic findByTopicName(String topicName) {
        return topicRepository.findByTopicName(topicName);
    }

    @Override
    public Topic findById(long id) {
        return topicRepository.findById(id).orElseThrow(() -> new TopicNotFoundException(ExceptionEnums.TOPIC.getText() +
                String.format(ExceptionConstants.WITH_SUCH_ID_NOT_FOUND, id)));
    }

    @Override
    @Transactional
    public Topic update(Topic topic) {
        if (!existsById(topic.getId())) {
            throw new TopicNotFoundException(ExceptionEnums.TOPIC.getText() + ExceptionConstants.NOT_FOUND);
        }
        return topicRepository.saveAndFlush(topic);
    }
}
