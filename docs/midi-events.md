# MIDI 消息



| first byte | 功能  | 格式 | 参数  |
|----|----|----|----|
| 0x8n  | note-off            | 8n kk vv |  n=通道 , kk=按键 , vv=速度   |
| 0x9n  | note-on             | 9n kk vv |  n=通道 , kk=按键 , vv=速度   |
| 0xAn  | Poly Key Pressure   |||
| 0xBn  | Control Change (CC) |||
| 0xCn  | Program Change      |||
| 0xDn  | Channel Pressure    |||
| 0xEn  | Pitch Bend          |||
| -  ||||
| 0xF1  | MIDI Time Code Quarter Frame | f1 ||
| 0xF2  | Song Position Pointer        | f2 ||
| 0xF3  | Song Select                  |||
| 0xF6  | Tune Request                 |||
| 0xF7  | EOX (End of Exclusive)       |||
| 0xF8  | Timing Clock                 | f8 ||
| 0xFA  | Start                        | fa ||
| 0xFB  | Continue                     | fb ||
| 0xFC  | Stop                         | fc ||
| 0xFE  | Active Sensing               |||
| 0xFF  | System Reset                 |||
