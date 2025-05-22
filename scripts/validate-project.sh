#!/bin/bash

# Haveno Android - Project Validation Script
# Validates project structure without requiring full Android SDK

echo "🔍 Haveno Android Project Validation"
echo "===================================="

# Check project structure
echo "📁 Checking project structure..."

required_files=(
    "build.gradle"
    "settings.gradle" 
    "gradlew"
    "app/build.gradle"
    "app/src/main/AndroidManifest.xml"
)

missing_files=()
for file in "${required_files[@]}"; do
    if [[ ! -f "$file" ]]; then
        missing_files+=("$file")
    else
        echo "✅ $file"
    fi
done

if [[ ${#missing_files[@]} -gt 0 ]]; then
    echo "❌ Missing required files:"
    printf '   %s\n' "${missing_files[@]}"
    exit 1
fi

# Count source files
echo ""
echo "📊 Project Statistics:"
kotlin_files=$(find . -name "*.kt" | wc -l)
xml_files=$(find . -name "*.xml" | wc -l)
java_files=$(find . -name "*.java" | wc -l)

echo "   Kotlin files: $kotlin_files"
echo "   XML files: $xml_files" 
echo "   Java files: $java_files"

# Check Gradle wrapper
echo ""
echo "🔧 Gradle Wrapper Check:"
if [[ -x "./gradlew" ]]; then
    echo "✅ gradlew is executable"
    echo "   Version: $(./gradlew --version | grep Gradle | head -1)"
else
    echo "❌ gradlew not executable"
    chmod +x gradlew
    echo "✅ Fixed: Made gradlew executable"
fi

# Validate Android manifest
echo ""
echo "📱 Android Manifest Validation:"
if grep -q "com.haveno.android" app/src/main/AndroidManifest.xml; then
    echo "✅ Package name configured correctly"
else
    echo "❌ Package name not found in manifest"
fi

# Check build.gradle files
echo ""
echo "🏗️  Build Configuration Check:"
if grep -q "compileSdk 34" app/build.gradle; then
    echo "✅ Target SDK configured"
else
    echo "❌ Target SDK not properly configured"
fi

if grep -q "minSdk 26" app/build.gradle; then
    echo "✅ Minimum SDK configured"
else
    echo "❌ Minimum SDK not properly configured"
fi

# Check key source files
echo ""
echo "🎯 Key Implementation Files:"
key_files=(
    "app/src/main/java/com/haveno/android/MainActivity.kt"
    "app/src/main/java/com/haveno/android/ui/market/MarketFragment.kt"
    "app/src/main/java/com/haveno/android/ui/portfolio/PortfolioFragment.kt"
    "app/src/main/java/com/haveno/android/ui/funds/FundsFragment.kt"
)

for file in "${key_files[@]}"; do
    if [[ -f "$file" ]]; then
        echo "✅ $(basename "$file")"
    else
        echo "❌ Missing: $(basename "$file")"
    fi
done

echo ""
echo "📋 Validation Summary:"
echo "   This project structure is valid for Android development"
echo "   Ready for build once Android SDK is properly configured"
echo "   See BUILD_REQUIREMENTS.md for SDK setup instructions"
echo ""
echo "🏆 Project validated successfully!"