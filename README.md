# 📝 Microservices Blogging Platform

**Java 17 · Spring Boot / Spring Cloud · RabbitMQ · Redis · AWS S3**

A scalable, multi-user content creation platform supporting article publishing, media uploads, dynamic commenting, and real-time activity feeds.

---

## Overview

- Designed and developed a **scalable blogging platform** with support for posts, media uploads, comments, likes, and activity feeds.
- Architected the system using **independent microservices** (Auth Service, Content Service, Notification Service) to ensure fault isolation and high availability during traffic spikes.
- Implemented **asynchronous, event-driven notifications** using **RabbitMQ** to decouple user interactions (likes, comments) from notification delivery.
- Integrated **Redis caching** for feed and metadata reads, significantly reducing database load and improving response latency under high-read scenarios.
- Ensured the **article publishing workflow remains operational** during surges in engagement traffic by separating write-heavy and interaction-heavy concerns.
- Utilized **AWS S3** for scalable and durable media storage of user-uploaded images and assets.
- Followed **cloud-native and microservices best practices**, including stateless services, clear service boundaries, and horizontal scalability.

---

## Architecture
```mermaid
flowchart LR
  %% ===== Clients / Edge =====
  C[Client<br/>(Web / Mobile)]
  G[API Gateway<br/>(Spring Cloud Gateway)]

  %% ===== Services =====
  A[Auth Service]
  S[Content Service]
  N[Notification Service]

  %% ===== Data / Infra =====
  ADB[(Auth DB<br/>PostgreSQL)]
  SDB[(Content DB<br/>PostgreSQL)]
  R[(Redis Cache)]
  Q[(RabbitMQ<br/]()
```
