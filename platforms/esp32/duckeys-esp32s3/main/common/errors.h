
#ifndef __COMMON_ERRORS_H__
#define __COMMON_ERRORS_H__

typedef struct type_Error_1
{
    const char *Message;
} SimpleError;

typedef SimpleError *Error;

Error FormatError(const char *fmt, ...);

#endif
