# Haveno Android App

An open-source Android application for the Haveno decentralized exchange, enabling secure peer-to-peer cryptocurrency trading with Tor privacy and Monero integration.

## Features

### Core Trading Features
- **Order Book Display**: Real-time order book updates
- **Offer Creation**: Create buy/sell offers for various cryptocurrencies
- **Trade Management**: Complete trading lifecycle from offer to settlement
- **Real-time Chat**: Secure messaging between trading parties

### Privacy & Security
- **Tor Integration**: All network traffic routed through Tor
- **Monero Integration**: Full XMR wallet functionality
- **User Authentication**: JWT-based authentication with biometric support
- **Desktop Synchronization**: Seamless account sync between devices

### User Experience
- **Dual Mode Operation**: 
  - Standalone mode (complete Haveno instance)
  - Companion mode (sync with desktop instance)
- **Background Services**: Maintain connections while app is backgrounded
- **Push Notifications**: Trade updates and chat messages
- **Offline Order Management**: Queue orders for when connection resumes

## Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                    Android UI Layer                         │
├─────────────────────────────────────────────────────────────┤
│                  ViewModel Layer                            │
├─────────────────────────────────────────────────────────────┤
│                 Repository Layer                            │
├─────────────────────────────────────────────────────────────┤
│  Background Service (Adapted Haveno Daemon)                │
│  ├── gRPC Server (Core APIs)                               │
│  ├── P2P Network Layer (Tor)                               │
│  ├── Monero Wallet Service                                 │
│  └── Trade Engine                                          │
├─────────────────────────────────────────────────────────────┤
│              Data Layer (Room DB)                          │
└─────────────────────────────────────────────────────────────┘
```

## Development Requirements

### Prerequisites
- Android Studio Arctic Fox or later
- Android SDK 26+ (Target: Android 13+)
- Java 17+
- Gradle 8.0+

### Core Dependencies
- **Haveno Core**: Adapted from `haveno-xmr` repository
- **gRPC**: API communication layer
- **Tor**: Privacy network integration
- **Monero**: Cryptocurrency functionality
- **Firebase**: Push notifications
- **Biometric**: Fingerprint/face authentication

## Project Structure

```
haveno-android/
├── app/                          # Android application module
│   ├── src/main/java/            # Android-specific code
│   ├── src/main/res/             # Android resources
│   └── build.gradle              # Android build configuration
├── haveno-core/                  # Adapted Haveno core libraries
│   ├── api/                      # gRPC service definitions
│   ├── daemon/                   # Background service implementation
│   ├── network/                  # P2P and Tor networking
│   └── wallet/                   # Monero integration
├── docs/                         # Documentation
└── scripts/                      # Build and deployment scripts
```

## Building the App

### Quick Start
```bash
git clone https://github.com/haveno-dex/haveno-android.git
cd haveno-android
./gradlew assembleDebug
```

### Release Build
```bash
./gradlew assembleRelease
```

## Installation Options

### F-Droid (Recommended)
Download from F-Droid store for verified builds

### Direct APK
Download from [Releases](https://github.com/haveno-dex/haveno-android/releases)

### Google Play Store
Available on Play Store (pending review)

## User Guide

### First Time Setup
1. **Mode Selection**: Choose standalone or companion mode
2. **Security Setup**: Configure authentication (password + biometric)
3. **Network Setup**: Tor connection initialization
4. **Wallet Setup**: Create or import Monero wallet

### Desktop Synchronization
1. **Enable API**: Start Haveno daemon on desktop with API enabled
2. **Pair Devices**: Scan QR code or enter connection details
3. **Sync Account**: Authenticate and sync account data

### Creating Your First Trade
1. **Browse Order Book**: View available buy/sell offers
2. **Create Offer**: Set price, amount, and payment method
3. **Secure Chat**: Communicate with trading partner
4. **Complete Trade**: Follow escrow process to completion

## Development

### Contributing
1. Fork the repository
2. Create feature branch (`git checkout -b feature/amazing-feature`)
3. Commit changes (`git commit -m 'Add amazing feature'`)
4. Push to branch (`git push origin feature/amazing-feature`)
5. Open Pull Request

### Testing
```bash
# Unit tests
./gradlew test

# Integration tests
./gradlew connectedAndroidTest

# Build verification
./gradlew check
```

### Code Style
- Follow Android Kotlin style guide
- Use Ktlint for formatting
- Maintain 80% test coverage

## Security Considerations

### Privacy
- All network traffic routed through Tor
- No data collection or telemetry
- Local data encryption at rest

### Authentication
- Multi-factor authentication support
- Biometric authentication
- Secure key storage using Android Keystore

### Network Security
- Certificate pinning for API calls
- End-to-end encryption for chat
- Tor circuit isolation

## Technical Specifications

### Minimum Requirements
- Android 8.0+ (API 26)
- 2GB RAM
- 4GB storage space
- Network connectivity

### Recommended
- Android 13+ (API 33)
- 4GB RAM
- 8GB storage space
- WiFi + Mobile data

### Battery Optimization
- Doze mode compatibility
- Background execution limits
- Efficient Tor connection management

## License

This project is licensed under the AGPL-3.0 License - see the [LICENSE](LICENSE) file for details.

## Acknowledgments

- Haveno DEX team for the core protocol
- Monero community for cryptocurrency integration
- Tor Project for privacy network
- Android open source community

## Support

- **Issues**: [GitHub Issues](https://github.com/haveno-dex/haveno-android/issues)
- **Discussions**: [GitHub Discussions](https://github.com/haveno-dex/haveno-android/discussions)
- **Matrix**: #haveno-android:matrix.org

## Roadmap

- [x] Core architecture design
- [x] Haveno daemon adaptation
- [ ] Android UI implementation
- [ ] Desktop synchronization
- [ ] F-Droid submission
- [ ] Play Store submission
- [ ] iOS version (future)

---

**⚠️ Warning**: This is cryptocurrency trading software. Use at your own risk and ensure you understand the legal requirements in your jurisdiction.