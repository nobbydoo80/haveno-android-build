# Build Requirements - Haveno Android

## Android SDK Setup Required

The project requires a complete Android SDK installation to build successfully.

### Quick Setup (Recommended)

#### Option 1: Install Android Studio
1. Download Android Studio from https://developer.android.com/studio
2. Install and run initial setup
3. Update `local.properties` with your SDK path:
   ```
   sdk.dir=/home/[username]/Android/Sdk
   ```

#### Option 2: Command Line Tools
```bash
# Download command line tools
wget https://dl.google.com/android/repository/commandlinetools-linux-10406996_latest.zip
unzip commandlinetools-linux-10406996_latest.zip -d ~/android-sdk
cd ~/android-sdk/cmdline-tools && mkdir latest && mv * latest/ 2>/dev/null || true

# Accept licenses and install required packages
export ANDROID_HOME=~/android-sdk
export PATH=$PATH:$ANDROID_HOME/cmdline-tools/latest/bin
yes | sdkmanager --licenses
sdkmanager "platforms;android-34" "build-tools;33.0.1"

# Update local.properties
echo "sdk.dir=$HOME/android-sdk" > local.properties
```

### Current Build Status
- ✅ Gradle wrapper configured
- ✅ Project structure complete
- ❌ Android SDK licenses need acceptance
- ❌ Missing Android SDK Platform 34
- ❌ Missing Build Tools 33.0.1

### Build Commands
Once SDK is properly set up:
```bash
./gradlew clean
./gradlew assembleDebug
```

### Alternative: Docker Build
For reproducible builds without local SDK setup:
```bash
# Build using official Android Docker image
docker run --rm -v $(pwd):/workspace -w /workspace thyrlian/android-sdk:latest \
  sh -c "yes | sdkmanager --licenses && ./gradlew assembleDebug"
```

### Verification
A successful build will create:
- `app/build/outputs/apk/debug/app-debug.apk`
- Build log showing "BUILD SUCCESSFUL"