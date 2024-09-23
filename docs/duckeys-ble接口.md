# Duckeys BLE 接口



## 基本要求

| 类型 | UUID                                   | permission        | 说明                    |
| ---- | -------------------------------------- | ----------------- | ----------------------- |
| 服务 | "03B80E5A-EDE8-4B33-A751-6CE34EC4C700" | -                 | BLE MIDI 服务           |
| 特征 | "7772E5DB-3868-4112-A1A9-F2669D106BF3" | READ/WRITE/NOTIFY | BLE MIDI Data I/O       |
| 特征 | "8E5169B6-3642-41BD-9957-F0039FCA0280" | READ              | Duckeys MIDI "关于"信息 |
| 特征 | "6C2A6D6C-B649-47B2-B947-F04D1BC1D2FC" | WRITE             | Duckeys MIDI 上行数据   |
| 特征 | "55F891E6-22AB-44AA-B720-7510BB8F07FC" | READ/NOTIFY       | Duckeys MIDI 下行数据   |

## MIDI 数据流格式 

MIDI 数据流编码为 Hex Text 格式，每行结尾添加 '\n'，每行对应一条 MIDI 事件。

### “关于”信息格式

通过读取特征 "8E5169B6-3642-41BD-9957-F0039FCA0280" ，可以获得设备的“关于”信息。
它的值是一个字符串，一个带有如下格式的 URI：

    about://{domain}/{path/to/product}?r={revision}&v={version}&git={git_commit_id}

例如：

    about://github.com/xushifustudio/duckeys/m0?r=0&v=0.0.1&git=fffffff
