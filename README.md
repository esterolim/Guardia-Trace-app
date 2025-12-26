# Guardian Trace - Stalking & Harassment Evidence Recorder

![Version](https://img.shields.io/badge/version-1.0.0-blue)
![Kotlin](https://img.shields.io/badge/kotlin-2.0.21-purple)
![Min API](https://img.shields.io/badge/minAPI-26-green)
![Target API](https://img.shields.io/badge/targetAPI-36-brightgreen)
![License](https://img.shields.io/badge/license-MIT-blue)

**Guardian Trace** empowers users to safely record incidents of stalking or online harassment with military-grade encryption, stealth capabilities, and emergency SOS features. All evidence is protected, encrypted at rest, and designed for legal use.

## 🎯 Key Features

### 🛡️ Security-First Design
- **AES-256-GCM Encryption**: Military-grade encryption for all sensitive data
- **SQLCipher Database**: Encrypted database with 100k PBKDF2 iterations
- **AndroidKeyStore Integration**: Hardware-backed key storage
- **Secure Credential Management**: PIN + Biometric authentication
- **Memory Zeroing**: Automatic clearing of sensitive data from memory
- **FLAG_SECURE**: Prevents screenshots and screen recording in production

### 📱 Emergency Capture
- Quick-tap emergency capture of screenshots, photos, or video
- One-touch SOS with emergency contact alerts
- Automatic location logging with timestamps
- Background event monitoring (with user consent)

### 👻 Stealth Mode
- Disguised app icon and appearance
- Hidden notification handling
- Configurable stealth behaviors
- Private file storage with encryption

### 💾 Evidence Management
- Organize incidents by type (harassment, stalking, threat)
- Add attachments (photos, videos, audio, documents)
- Automatic metadata preservation
- Secure file export with integrity verification

### 🔐 Privacy & Compliance
- NO cloud data collection without user consent
- Local-first architecture
- GDPR & privacy regulation compliant
- Data extraction rules configured
- Tamper detection & integrity checking

## 📋 Architecture

Guardian Trace follows **Clean Architecture** with **MVVM** pattern:

```
┌─────────────────────────────────────────────────┐
│         Presentation Layer (Jetpack Compose)    │
│  ├─ UI Screens (Auth, Home, Incident, Export)  │
│  ├─ ViewModels with StateFlow                   │
│  └─ Navigation & Routing                        │
├─────────────────────────────────────────────────┤
│         Domain Layer (Business Logic)           │
│  ├─ Use Cases                                   │
│  ├─ Repository Interfaces                       │
│  └─ Domain Models                               │
├─────────────────────────────────────────────────┤
│           Data Layer (Storage & APIs)           │
│  ├─ Room Database (SQLCipher encrypted)         │
│  ├─ Repositories Implementation                 │
│  ├─ Encryption Manager (AES-256-GCM)            │
│  ├─ Security Utilities                          │
│  └─ Local Data Sources                          │
└─────────────────────────────────────────────────┘
```

### Technology Stack
- **Language**: Kotlin 2.0.21
- **UI Framework**: Jetpack Compose with Material 3
- **Architecture**: MVVM + Clean Architecture
- **DI**: Hilt (Dagger 2)
- **Database**: Room 2.6.1 + SQLCipher 4.5.4
- **Encryption**: AES-256-GCM (AndroidKeyStore) + PBKDF2
- **Async**: Kotlin Coroutines + Flow
- **Compilation**: KSP (Kotlin Symbol Processing)

## 🔧 Installation & Setup

### Prerequisites
- Android Studio Hedgehog or later
- JDK 17+
- Android SDK 36 (Target API)
- Minimum API Level 26

### Clone & Setup

```bash
git clone https://github.com/yourusername/GuardianTrace.git
cd GuardianTrace

# Build project
./gradlew build

# Run on device/emulator
./gradlew installDebug
```

### First Launch
1. Set a 4-8 digit PIN for authentication
2. (Optional) Enable biometric authentication
3. Grant required permissions:
   - Camera (for evidence capture)
   - Microphone (for audio recording)
   - Location (for incident tracking)
   - Contacts (for emergency contacts)

## 🔐 Security Baseline

### Encryption Standards
- **Database**: SQLCipher with AES encryption + PBKDF2 (100,000 iterations)
- **In-Transit**: TLS 1.3 minimum (via network security config)
- **At-Rest**: AES-256-GCM for all sensitive files
- **Keys**: Hardware-backed AndroidKeyStore (API 26+)

### Authentication
- PIN-based access control (PBKDF2-SHA256)
- Biometric authentication (Fingerprint/Face)
- Constant-time comparison to prevent timing attacks
- Automatic session management

### Runtime Protection
- **Debugger Detection**: Prevents analysis on rooted devices
- **Root/Jailbreak Detection**: Runtime integrity checks
- **Certificate Pinning**: Network security policy configured
- **Memory Clearing**: Sensitive data zeroed after use
- **Frida Detection**: Anti-tampering measures

## 📚 Documentation

For detailed documentation, see:
- [SECURITY.md](./SECURITY.md) - Security policy and disclosure
- [ARCHITECTURE.md](./docs/ARCHITECTURE.md) - Detailed architecture guide
- [API_REFERENCE.md](./docs/API_REFERENCE.md) - Component API reference
- [CONTRIBUTING.md](./CONTRIBUTING.md) - Contribution guidelines

## 🧪 Testing

### Run Tests
```bash
# Unit tests
./gradlew test

# Instrumented tests
./gradlew connectedAndroidTest

# Lint checks
./gradlew lint
```

### Test Coverage
- Unit tests for ViewModels and Use Cases
- Integration tests for Room + repositories
- UI tests for Compose screens
- Security validation tests

## 📦 Build & Release

### Debug Build
```bash
./gradlew assembleDebug
```

### Release Build
```bash
./gradlew assembleRelease
```

**Note**: Release builds include:
- ProGuard obfuscation
- Resource shrinking
- Signing with release keystore
- Security hardening

## 🛠️ Development Workflow

### Branch Naming
- `feature/*` - New features
- `fix/*` - Bug fixes
- `security/*` - Security patches
- `docs/*` - Documentation

### Commit Standards
Follow conventional commits:
```
feat(auth): implement PIN authentication
fix(encryption): resolve SQLCipher key rotation
security(crypto): add timing attack prevention
docs(readme): update installation instructions
```

### Code Quality
- Kotlin style guide compliance (Ktlint)
- Static analysis (Lint + Detekt)
- Security scanning (Dependency Check)
- Code review required before merge

## 📄 License

This project is licensed under the **MIT License** - see [LICENSE](./LICENSE) file for details.

## 🔒 Security Disclosure

**If you discover a security vulnerability, please DO NOT open a public issue.** 

Instead, please report it to: `security@example.com`

See [SECURITY.md](./SECURITY.md) for full disclosure policy and supported versions.

## 🙋 Support & Contribution

### Report an Issue
- [GitHub Issues](https://github.com/yourusername/GuardianTrace/issues)
- Please include: device, Android version, reproduction steps, expected vs actual behavior

### Contribute Code
1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit changes (`git commit -m 'feat: add amazing feature'`)
4. Push to branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

See [CONTRIBUTING.md](./CONTRIBUTING.md) for detailed contribution guidelines.

## 🗺️ Roadmap

### Version 1.1 (Q2 2025)
- [ ] Cloud backup with user-controlled encryption keys
- [ ] Enhanced emergency contact system
- [ ] Export to legal report format (PDF)

### Version 1.2 (Q3 2025)
- [ ] AI-powered threat detection
- [ ] Multi-device sync
- [ ] Advanced analytics dashboard

### Version 2.0 (Q4 2025)
- [ ] Desktop companion app
- [ ] Legal integration (direct filing)
- [ ] Blockchain verification

## 👥 Authors

- **Developer**: Your Name (@github)

## 🙏 Acknowledgments

- Inspired by user safety and privacy concerns
- Built with security best practices from OWASP, NIST, and Google
- Thanks to all contributors and testers

---

**Disclaimer**: Guardian Trace is a tool for recording evidence. Users are responsible for complying with local laws regarding recording and privacy. Always consult legal counsel before using evidence in proceedings.

