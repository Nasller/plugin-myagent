# plugin-myagent

### A plugin for the [ja-netfilter](https://gitee.com/ja-netfilter/ja-netfilter), it allows you to use remote-run with jumpServer and crack in ja-netfilter.

#### Use the `mvn clean package` command to compile and use `MyAgent-vX.X.X.jar` file!



## 当前支持的功能

+ AES密钥结果替换

  + ```
    [AES]
    EQUAL,NONE,{aesKey},{customData}
    ;实现一个一直在刷新的customData，需要修改com.nasller.myagent.aes.TypeEnum类
    EQUAL,LEET_CODE,{aesKey}
    
    ```

+ Remote SSH External Tools工具支持初始化指令，类似于登录jumpServer时需要选择目标机器的指令，需要配合自定义字节码（有需要找我）

  + ```
    [REMOTE]
    EQUAL,{class file path eg: F:/ ja-netfilter/classPath/remote}
    EQUAL,jp.nasller.com,p,4,1
    ```

+ 修复Run on ssh target，windows环境下 slash上传的问题（详细请见 [youtrack](https://youtrack.jetbrains.com/issue/IDEA-270106/Run-Targets-cant-run-JUnit-RC-on-ssh-target-with-rsync-on-Windows)）

  + 配置规则自动

+ 重定向URL，类似于请求http://www.abc.com/test 转到 http://localhost:8080/test (只支持http 或 https)

  + ```
    [URL]
    EQUAL,http://www.abc.com/test,http://localhost:8080/test
    ```
