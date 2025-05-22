#!/bin/bash
set -e

# Haveno Android Build Script
# This script builds the Haveno Android APK with all necessary components

echo "🚀 Building Haveno Android App"
echo "==============================="

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to print colored output
print_status() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Check prerequisites
print_status "Checking prerequisites..."

# Check Java version
if ! command -v java &> /dev/null; then
    print_error "Java is not installed. Please install Java 17 or later."
    exit 1
fi

JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
if [ "$JAVA_VERSION" -lt 17 ]; then
    print_error "Java 17 or later is required. Current version: $JAVA_VERSION"
    exit 1
fi

print_success "Java version check passed"

# Check Android SDK
if [ -z "$ANDROID_HOME" ]; then
    print_warning "ANDROID_HOME is not set. Please set it to your Android SDK path."
    print_warning "Example: export ANDROID_HOME=/path/to/android-sdk"
fi

# Navigate to project directory
cd "$(dirname "$0")/.."

# Clean previous builds
print_status "Cleaning previous builds..."
./gradlew clean

# Build debug APK
print_status "Building debug APK..."
./gradlew assembleDebug

if [ $? -eq 0 ]; then
    print_success "Debug APK built successfully!"
    DEBUG_APK=$(find app/build/outputs/apk/debug -name "*.apk" | head -1)
    print_status "Debug APK location: $DEBUG_APK"
else
    print_error "Debug build failed!"
    exit 1
fi

# Build release APK (if keystore is available)
if [ -f "keystore/release.keystore" ]; then
    print_status "Building release APK..."
    ./gradlew assembleRelease
    
    if [ $? -eq 0 ]; then
        print_success "Release APK built successfully!"
        RELEASE_APK=$(find app/build/outputs/apk/release -name "*.apk" | head -1)
        print_status "Release APK location: $RELEASE_APK"
    else
        print_error "Release build failed!"
    fi
else
    print_warning "Release keystore not found. Skipping release build."
    print_warning "To build release APK, create keystore at: keystore/release.keystore"
fi

# Build F-Droid variant
print_status "Building F-Droid variant..."
./gradlew assembleFdroid

if [ $? -eq 0 ]; then
    print_success "F-Droid APK built successfully!"
    FDROID_APK=$(find app/build/outputs/apk/fdroid -name "*.apk" | head -1)
    print_status "F-Droid APK location: $FDROID_APK"
else
    print_warning "F-Droid build failed (this is optional)"
fi

# Run tests
print_status "Running unit tests..."
./gradlew test

if [ $? -eq 0 ]; then
    print_success "Unit tests passed!"
else
    print_warning "Some unit tests failed. Check the reports."
fi

# Generate build report
print_status "Generating build report..."
BUILD_TIME=$(date)
BUILD_DIR="build-reports/$(date +%Y%m%d_%H%M%S)"
mkdir -p "$BUILD_DIR"

cat > "$BUILD_DIR/build_report.md" << EOF
# Haveno Android Build Report

**Build Time:** $BUILD_TIME
**Java Version:** $(java -version 2>&1 | head -n 1)
**Gradle Version:** $(./gradlew --version | grep "Gradle" | head -1)

## Build Artifacts

EOF

if [ -f "$DEBUG_APK" ]; then
    echo "- **Debug APK:** \`$DEBUG_APK\`" >> "$BUILD_DIR/build_report.md"
    cp "$DEBUG_APK" "$BUILD_DIR/"
fi

if [ -f "$RELEASE_APK" ]; then
    echo "- **Release APK:** \`$RELEASE_APK\`" >> "$BUILD_DIR/build_report.md"
    cp "$RELEASE_APK" "$BUILD_DIR/"
fi

if [ -f "$FDROID_APK" ]; then
    echo "- **F-Droid APK:** \`$FDROID_APK\`" >> "$BUILD_DIR/build_report.md"
    cp "$FDROID_APK" "$BUILD_DIR/"
fi

print_success "Build completed successfully!"
print_status "Build report saved to: $BUILD_DIR/build_report.md"

echo ""
echo "🎉 Haveno Android build complete!"
echo "==============================="
echo ""
echo "Next steps:"
echo "1. Install the APK on your Android device"
echo "2. Test the app functionality"
echo "3. Report any issues on GitHub"
echo ""
echo "APK Installation:"
echo "adb install $DEBUG_APK"
echo ""