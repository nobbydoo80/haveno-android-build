# Contributing to Haveno Android

Thank you for your interest in contributing to this project!

## Development Setup

### Prerequisites
- Android Studio Arctic Fox or later
- JDK 17 or later
- Android SDK with API level 26+

### Getting Started
1. Fork the repository
2. Clone your fork locally
3. Open in Android Studio
4. Build with `./gradlew assembleDebug`

## Project Structure

```
haveno-android/
├── app/                    # Main Android application
├── haveno-core/           # Core functionality
├── haveno-daemon/         # Service implementation
├── haveno-network/        # Network integration
└── scripts/               # Build scripts
```

## Coding Standards

### Kotlin Style
- Follow Android Kotlin Style Guide
- Use PascalCase for classes
- Use camelCase for functions and variables
- Use SCREAMING_SNAKE_CASE for constants

### Documentation
- Add KDoc comments for public APIs
- Update README for new features
- Keep documentation current

## Testing

### Unit Tests
Write tests for business logic and utilities:

```kotlin
@Test
fun `should validate input correctly`() {
    val result = validator.validate("test input")
    assertTrue(result.isValid)
}
```

### Integration Tests
Test service interactions and database operations.

## Pull Request Process

1. Ensure code compiles without warnings
2. All tests pass
3. Lint checks pass
4. Documentation updated
5. Submit PR with clear description

## Getting Help

- GitHub Issues for bug reports
- GitHub Discussions for questions
- Email: dev@haveno.network

Thank you for contributing!