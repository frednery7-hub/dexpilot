# 05 — Docker e Ambientes Isolados

Docker is not required for MVP 1 local execution.

MVP 1 must work locally with Gradle:

```bash
./gradlew test
./gradlew run
./gradlew build
```

Docker may be introduced later for:

- clean Linux validation;
- isolated CI execution;
- external tool execution;
- future DEX writer validation.

When Docker is introduced, it must follow these rules:

- use multi-stage build;
- keep the final image small;
- avoid copying build cache into the final image;
- include no secrets;
- execute as a non-root user;
- copy only the necessary final artifact;
- never become mandatory for normal local development.
