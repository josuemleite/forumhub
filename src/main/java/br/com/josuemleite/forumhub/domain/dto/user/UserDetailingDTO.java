package br.com.josuemleite.forumhub.domain.dto.user;

import br.com.josuemleite.forumhub.domain.dto.answer.AnswerListingDTO;
import br.com.josuemleite.forumhub.domain.dto.profile.ProfileListingDTO;
import br.com.josuemleite.forumhub.domain.dto.topic.TopicListingDTO;
import br.com.josuemleite.forumhub.domain.model.Answer;
import br.com.josuemleite.forumhub.domain.model.Profile;
import br.com.josuemleite.forumhub.domain.model.Topic;
import br.com.josuemleite.forumhub.domain.model.User;

import java.util.Set;
import java.util.stream.Collectors;

public record UserDetailingDTO(
        Long id,
        String name,
        String email,
        String password,
        Set<ProfileListingDTO> profiles,
        Set<TopicListingDTO> topics,
        Set<AnswerListingDTO> answers) {

    public UserDetailingDTO(User user) {
        this(user.getId(), user.getName(), user.getEmail(), user.getPassword(), convertProfilesToDTOs(user.getProfiles()), convertTopicsToDTOs(user.getTopics()), convertAnswersToDTOs(user.getAnswers()));
    }

    private static Set<ProfileListingDTO> convertProfilesToDTOs(Set<Profile> profiles) {
        return profiles.stream()
                .map(ProfileListingDTO::new)
                .collect(Collectors.toSet());
    }

    private static Set<TopicListingDTO> convertTopicsToDTOs(Set<Topic> topics) {
        return topics.stream()
                .map(TopicListingDTO::new)
                .collect(Collectors.toSet());
    }

    private static Set<AnswerListingDTO> convertAnswersToDTOs(Set<Answer> answers) {
        return answers.stream()
                .map(AnswerListingDTO::new)
                .collect(Collectors.toSet());
    }
}
