# 易经 App

[![Android CI/CD](https://github.com/yy/test/actions/workflows/android.yml/badge.svg)](https://github.com/yy/test/actions/workflows/android.yml)

一款优雅的易经学习和卜卦应用，融合现代设计与传统文化。本项目采用AI驱动开发（AI-Driven Development）模式，通过人工智能辅助完成代码编写、调试和优化，展现传统文化与现代技术的完美结合。

## ✨ 特色功能

- 🎴 **动态太极图**：独特的动态太极图展示，体现阴阳变化之道
- 📚 **六十四卦浏览**：完整展示易经六十四卦，包含卦象、卦辞和爻辞
- 🎲 **随机卜卦**：提供传统的随机卜卦功能，帮助用户寻求指引
- 🎨 **现代界面设计**：采用 Material Design 设计语言，提供流畅优雅的用户体验

## 🛠️ 技术实现

- 🤖 采用AI驱动开发模式，通过人工智能辅助完成代码编写、调试和优化
- 📱 基于 Android 原生开发
- 🎨 使用自定义 View 实现动态太极图和卦象展示
- 📖 采用 ViewPager2 实现页面滑动切换
- 📱 支持 Android 5.0 (API 21) 及以上版本

## 🤖 AI驱动开发

本项目采用创新的AI驱动开发方式，具有以下特点：

- **智能代码生成**：通过AI辅助生成高质量、符合最佳实践的代码
- **自动化调试**：利用AI分析和定位潜在问题，提高开发效率
- **代码优化建议**：AI提供性能优化和代码改进建议
- **文档自动化**：通过AI辅助生成清晰、准确的技术文档

## 📱 应用截图

[待添加应用截图]

## 🚀 快速开始

### 系统要求

- Android Studio Hedgehog 或更高版本
- JDK 17
- Android SDK API 34
- Gradle 8.9

### 构建步骤

1. 克隆项目
```bash
git clone https://github.com/your-username/your-repo.git
```

2. 使用 Android Studio 打开项目

3. 构建并运行
```bash
./gradlew assembleDebug    # 构建调试版本
./gradlew installDebug     # 安装到设备
```

## 📦 项目结构

### 核心类文件
- `MainActivity.java` - 应用程序入口，管理主界面和页面切换
- `ViewPagerAdapter.java` - 处理太极图和六十四卦列表页面的切换适配器
- `TaichiFragment.java` - 太极图页面，展示动态太极图
- `TaichiView.java` - 自定义视图组件，负责绘制太极图
- `HexagramListFragment.java` - 六十四卦列表页面，以网格形式展示所有卦象
- `HexagramView.java` - 自定义视图组件，负责绘制单个卦象的爻线
- `HexagramDetailActivity.java` - 卦象详情页面，展示单个卦象的详细信息
- `HexagramFragment.java` - 卦象详情的内容展示页面
- `HexagramUtils.java` - 工具类，提供卦象数据处理的通用方法

## 🤝 贡献指南

欢迎提交 Issue 和 Pull Request 来帮助改进项目。在提交 PR 之前，请确保：

1. 代码符合项目的编码规范
2. 所有测试用例通过
3. 提供必要的文档说明

## 📄 开源协议

本项目采用 MIT 协议开源，详见 [LICENSE](LICENSE) 文件。

## 📬 联系方式

如有问题或建议，欢迎通过以下方式联系：

- 提交 Issue
- 发送邮件至：[your-email@example.com]

## 🙏 致谢

感谢所有对项目做出贡献的开发者。