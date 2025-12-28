package com.example.article_api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.web.reactive.function.client.WebClient;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
@EnableAspectJAutoProxy
public class Config {

    @Bean S3Client s3Client(@Value("${aws.accessKeyId}") String awsAccessKeyId,
                      @Value("${aws.secretKey}") String awsSecretKey,
                      @Value("${aws.region}") String awsRegion) {
        StaticCredentialsProvider provider = StaticCredentialsProvider.create(AwsBasicCredentials.create(awsAccessKeyId, awsSecretKey));
        return S3Client.builder().credentialsProvider(provider)
                .region(Region.of(awsRegion))
                .build();
    }

    @Bean S3Presigner s3Presigner(@Value("${aws.accessKeyId}") String awsAccessKeyId,
                                @Value("${aws.secretKey}") String awsSecretKey,
                                @Value("${aws.region}") String awsRegion) {
        StaticCredentialsProvider provider = StaticCredentialsProvider.create(AwsBasicCredentials.create(awsAccessKeyId, awsSecretKey));
        return S3Presigner.builder()
                .region(Region.of(awsRegion))
                .credentialsProvider(provider)
                .build();
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        Jackson2JsonRedisSerializer<Object> serializer =
                new Jackson2JsonRedisSerializer<>(Object.class);

        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(serializer);
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(serializer);

        template.afterPropertiesSet();
        return template;
    }

    @Bean
    @Qualifier("userServiceClient")
    public WebClient webClient(@Value("${internal-secret}") String internalSecret,
                               @Value("${spring.application.name}") String serviceIdentifier) {
        return WebClient.builder()
                .baseUrl("http://localhost:8081")
                .defaultHeader("X-Internal-Secret", internalSecret)
                .defaultHeader("X-Service-Id", serviceIdentifier)
                .build();
    }
}
