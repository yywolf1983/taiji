#!/bin/bash
# 太极 App 构建脚本
set -e

PROJECT_DIR="$(cd "$(dirname "$0")" && pwd)"
cd "$PROJECT_DIR"

print_usage() {
    echo "用法: ./build.sh [命令]"
    echo ""
    echo "命令:"
    echo "  debug     构建 Debug APK（默认）"
    echo "  release   构建 Release APK"
    echo "  clean     清理构建产物"
    echo "  install   构建并安装到设备"
    echo "  help      显示此帮助信息"
    echo ""
    echo "示例:"
    echo "  ./build.sh              # 构建 Debug APK"
    echo "  ./build.sh release      # 构建 Release APK"
    echo "  ./build.sh install      # 构建并安装到设备"
}

build_debug() {
    echo ">>> 构建 Debug APK..."
    ./gradlew assembleDebug
    APK="app/build/outputs/apk/debug/app-debug.apk"
    if [ -f "$APK" ]; then
        echo ">>> 构建成功: $APK"
        ls -lh "$APK"
    fi
}

build_release() {
    echo ">>> 构建 Release APK..."
    ./gradlew assembleRelease
    APK="app/build/outputs/apk/release/app-release-unsigned.apk"
    if [ -f "$APK" ]; then
        echo ">>> 构建成功: $APK"
        ls -lh "$APK"
    fi
}

clean_build() {
    echo ">>> 清理构建产物..."
    ./gradlew clean
    echo ">>> 清理完成"
}

install_debug() {
    echo ">>> 构建并安装到设备..."
    ./gradlew installDebug
    echo ">>> 安装完成"
}

case "${1:-debug}" in
    debug)    build_debug ;;
    release)  build_release ;;
    clean)    clean_build ;;
    install)  install_debug ;;
    help|-h)  print_usage ;;
    *)        echo "未知命令: $1"; print_usage; exit 1 ;;
esac
