# AndFixProject
使用三种方式实现代码的热修复：

1.基于阿里巴巴andFix热修复框架，实现代码的热修复，主要就是将ArtMethod结构体中的每一个成员进行替换

2.改进了AndFix，直接将ArtMethod整体进行替换，实现热修复

3.使用纯Java代码实现方法的热修复，主要用到了JDK中非常强大的两个类：sun.misc.Unsafe 和 libcore.io.Memory
