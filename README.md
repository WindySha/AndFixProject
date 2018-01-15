# AndFixProject
使用三种方式实现代码的热修复：

1.基于阿里巴巴andFix热修复框架，实现代码的热修复，主要就是将ArtMethod结构体中的每一个成员进行替换

2.改进了AndFix，直接将ArtMethod整体进行替换，实现热修复

3.使用纯Java代码实现方法的热修复，主要用到了JDK中非常强大的两个类：sun.misc.Unsafe 和 libcore.io.Memory

代码解读: [Android热修复框架AndFix核心代码分析以及纯Java代码实现AndFix](https://windysha.github.io/2018/01/15/Android%E7%83%AD%E4%BF%AE%E5%A4%8D%E6%A1%86%E6%9E%B6AndFix%E6%A0%B8%E5%BF%83%E4%BB%A3%E7%A0%81%E5%88%86%E6%9E%90%E4%BB%A5%E5%8F%8A%E7%BA%AFJava%E4%BB%A3%E7%A0%81%E5%AE%9E%E7%8E%B0AndFix/)
