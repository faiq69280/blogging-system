package com.example.article_api.caching;

import com.example.article_api.domain.PostDomainModel;
import com.example.article_api.domain.PostMetadata;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;

@Service
public class PostCachingService {

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    /**
     *
     * @param postId
     * @param objectToCache
     * @return
     */
    public PostDomainModel putInCache(String postId, Object objectToCache) {
        String postDataKey = getPostDataKey(postId);

        //expected object: PostDomainModel
        PostDomainModel postDomainModel = (PostDomainModel) objectToCache;
        redisTemplate.opsForValue().set(postDataKey, new PostDomainRedisView(
                postDomainModel.getCaption(),
                postDomainModel.getTags(),
                postDomainModel.getUrl()
        ));

        updateMetadata(postId, postDomainModel.getPostMetadata().getLikesCount(), PostMetadata.MetaDataKey.LIKES);
        updateMetadata(postId, postDomainModel.getPostMetadata().getCommentCount(), PostMetadata.MetaDataKey.COMMENTS);
        updateMetadata(postId, postDomainModel.getPostMetadata().getShareCount(), PostMetadata.MetaDataKey.SHARES);
        updateMetadata(postId, postDomainModel.getPostMetadata().getViewsCount(), PostMetadata.MetaDataKey.VIEWS);

        return postDomainModel;
    }

    /**
     *
     * @param postId
     * @return
     */
    public PostDomainModel get(String postId) {
        String postKey = getPostDataKey(postId);

        Object objectFetched = redisTemplate.opsForValue().get(postKey);

        ObjectMapper objectMapper = new ObjectMapper();
        PostDomainModel postDomainModel = null;

        if (objectFetched != null) {
            postDomainModel = convertToPostDomainModel(objectMapper.convertValue(objectFetched, PostDomainRedisView.class));
            postDomainModel.setPostId(postId);

            PostMetadata postMetadata = getMetadata(postId);
            if (postMetadata != null) {
                postDomainModel.setPostMetadata(postMetadata);
                return postDomainModel;
            }
        }

        return postDomainModel;
    }

    /**
     *
     * @param objectFetched
     * @return
     */
    private PostDomainModel convertToPostDomainModel(PostDomainRedisView objectFetched) {
        return PostDomainModel.builder()
                .url(objectFetched.url)
                .caption(objectFetched.caption)
                .tags(objectFetched.tags)
                .build();
    }


    /**
     *
     * @param postId
     * @param count
     * @param metaDataKey
     */
    public void updateMetadata(String postId, int count, PostMetadata.MetaDataKey metaDataKey) {
        String postKey = getMetadataKey(postId);
        redisTemplate.opsForHash().put(postKey, metaDataKey.name(), count);
    }

    /**
     *
     * @param postId
     * @return
     */
    public PostMetadata getMetadata(String postId) {
        Map<Object, Object> metadataMap = redisTemplate.opsForHash().entries(getMetadataKey(postId));
        if (!ObjectUtils.isEmpty(metadataMap)) {
            return new PostMetadata(
                (int) metadataMap.get(PostMetadata.MetaDataKey.LIKES.name()),
                (int) metadataMap.get(PostMetadata.MetaDataKey.VIEWS.name()),
                (int) metadataMap.get(PostMetadata.MetaDataKey.SHARES.name()),
                (int) metadataMap.get(PostMetadata.MetaDataKey.COMMENTS.name())
            );
        }
        return null;
    }

    /**
     *
     * @param postId
     * @return
     */
    private String getMetadataKey(String postId) {
        return "post:metadata:".concat(postId);
    }

    /**
     *
     * @param postId
     * @return
     */
    private String getPostDataKey(String postId) {
        return "post:data:".concat(postId);
    }

    /**
     *
     * @param caption
     * @param tags
     * @param url
     */
    private record PostDomainRedisView(String caption, List<String> tags, String url) { }
}
