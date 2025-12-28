package com.example.article_api.async.events;

import com.example.article_api.async.message_queue.MessageQueueProducer;
import com.example.article_api.caching.PostCachingService;
import com.example.article_api.domain.PostMetadata;
import com.example.article_api.domain.UserModel;
import com.example.article_api.repository.PostMetadataRepositoryFacade;
import com.example.article_api.service.UserService;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
public class PostEventListeners {

    PostCachingService postCachingService;
    PostMetadataRepositoryFacade postMetadataRepositoryFacade;
    MessageQueueProducer messageQueueProducer;
    UserService userService;

    public PostEventListeners(PostCachingService postCachingService,
                              PostMetadataRepositoryFacade postMetadataRepositoryFacade,
                              MessageQueueProducer messageQueueProducer,
                              UserService userService) {
        this.postCachingService = postCachingService;
        this.postMetadataRepositoryFacade = postMetadataRepositoryFacade;
        this.messageQueueProducer = messageQueueProducer;
        this.userService = userService;
    }

    @EventListener
    @Async
    public void postLiked(PostLikeEvent postLikeEvent) {
        //update cache
        postCachingService.updateMetadata(postLikeEvent.postId(), postLikeEvent.likesCount(), PostMetadata.MetaDataKey.LIKES);

        //notifyMessageQueue
        String messageText = "Post liked by %s and %d others".formatted(postLikeEvent.likedBy().userName(), postLikeEvent.likesCount() - 1);
        UserModel userModel = userService.getUserInfo(postLikeEvent.authorId());

        for (Map.Entry<String, String> channelContactInfo : userModel.getContactSubscription().entrySet()) {
            MessagePayload messagePayload = new MessagePayload(messageText, channelContactInfo.getKey(), channelContactInfo.getValue());
            EventEnvelope envelope = new EventEnvelope(UUID.randomUUID().toString(), EventType.POST_LIKED, messagePayload);
            messageQueueProducer.sendMessage(envelope, EventType.POST_LIKED.value());
        }
    }

}
