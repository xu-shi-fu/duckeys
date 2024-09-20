#include "common/hex.h"

ByteBuffer *HexParse(const char *str, ByteBuffer *dst)
{
    const int limit = 0xffff;
    int count = 0;
    int num = 0;
    for (int i = 0; i < limit; i++)
    {
        char ch = str[i];
        int n = 0;
        if (ch == 0)
        {
            break;
        }
        else if (('0' <= ch) && (ch <= '9'))
        {
            n = ch - '0';
        }
        else if (('a' <= ch) && (ch <= 'f'))
        {
            n = ch - 'a' + 0x0a;
        }
        else if (('A' <= ch) && (ch <= 'F'))
        {
            n = ch - 'A' + 0x0a;
        }
        else
        {
            continue;
        }

        if (count & 0x01)
        {
            // at: 13579...
            __uint8_t b = num | (n & 0x0f);
            ByteBufferWrite(dst, &b, sizeof(b));
            num = 0;
        }
        else
        {
            // at: 02468...
            num = (n << 4) & 0xf0;
        }

        count++;
    }

    if (dst->Overflow)
    {
        return Nil;
    }
    return dst;
}
